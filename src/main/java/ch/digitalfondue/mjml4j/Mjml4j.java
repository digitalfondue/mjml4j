package ch.digitalfondue.mjml4j;

import ch.digitalfondue.jfiveparse.Document;
import ch.digitalfondue.jfiveparse.JFiveParse;
import ch.digitalfondue.jfiveparse.Option;
import ch.digitalfondue.jfiveparse.W3CDom;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ch.digitalfondue.mjml4j.Utils.isNullOrWhiteSpace;

/**
 * mjml java implementation.
 * 
 * <p>You can use the default methods {@link Mjml4j#render(String)} or for a little bit more control {@link #render(String, Configuration)} if you
 * specify the language (recommended).</p>
 *
 * See the record {@link Configuration}.
 */
public final class Mjml4j {

    private Mjml4j() {
        //
    }

    public enum TextDirection {

        LTR("ltr"), RTL("rtl"), AUTO("auto");
        private String value;
        TextDirection(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    interface IncludeResolver {
        String resolveAsString(String resolvedResourcePath);
        org.w3c.dom.Document resolveAsDocument(String resolvedResourcePath);
        String resolvePath(String name, String base, Collection<String> parents);
    }

    public record Configuration(
            String language, TextDirection dir,
            IncludeResolver includeResolver, String basePath, String currentResourcePath
    ) {

        public Configuration(String language, TextDirection dir) {
            this(language, dir, null, null, null);
        }

        public Configuration(String language) {
            this(language, TextDirection.AUTO);
        }
    }

    private static final Configuration DEFAULT_CONFIG = new Configuration("und", TextDirection.AUTO);

    /**
     * Render the given template with the provided configuration.
     *
     * @param template
     * @param configuration
     * @return
     */
    public static String render(String template, Configuration configuration) {
        var nodes = JFiveParse.parseFragment(template, EnumSet.of(Option.DISABLE_IGNORE_TOKEN_IN_BODY_START_TAG, Option.INTERPRET_SELF_CLOSING_ANYTHING_ELSE, Option.DONT_TRANSFORM_ENTITIES));

        var rootElemMaybe = nodes.stream().filter(node -> "mjml".equals(node.getNodeName())).findFirst();
        if (rootElemMaybe.isEmpty()) {
            throw new IllegalStateException("no root element mjml found");
        }

        var rootElement = rootElemMaybe.get();
        var doc = new Document();
        doc.appendChild(rootElement);
        var dom = W3CDom.toW3CDocument(doc);
        return render(dom, configuration);
    }

    /**
     * Render the template with the default configuration.
     *
     * @param template
     * @return
     */
    public static String render(String template) {
        return render(template, DEFAULT_CONFIG);
    }

    private static StringBuilder renderHead(MjmlComponent.MjmlRootComponent rootComponent, HtmlRenderer renderer) {
        return rootComponent
                .getChildren()
                .stream()
                .filter(c -> c.getTagName().equals("mj-head"))
                .findFirst().map(component -> component.renderMjml(renderer)).orElseGet(StringBuilder::new);

    }

    private static String renderBody(MjmlComponent.MjmlRootComponent rootComponent, HtmlRenderer renderer) {
        renderer.increaseDepth();
        return rootComponent
                .getChildren()
                .stream()
                .filter(c -> c.getTagName().equals("mj-body"))
                .findFirst().map(component -> component.renderMjml(renderer).toString())
                .orElse("");
    }

    /**
     * Render a given Document with the default configuration.
     *
     * @param document
     * @return
     */
    public static String render(org.w3c.dom.Document document) {
        return render(document, DEFAULT_CONFIG);
    }

    /**
     * Render a given Document with a specified configuration.
     *
     * @param document
     * @param configuration
     * @return
     */
    public static String render(org.w3c.dom.Document document, Configuration configuration) {
        var context = new GlobalContext(document, configuration);
        var rootComponent = buildMjmlDocument(document, context);
        var renderer = new HtmlRenderer();

        var headRaw = renderHead(rootComponent, renderer);
        var body = Utils.minifyOutlookConditionals(renderBody(rootComponent, renderer));
        var forceOWADesktop = false;

        var mjOutsideRaws = rootComponent.getChildren().stream()
                .filter(MjmlComponentRaw.class::isInstance)
                .map(MjmlComponentRaw.class::cast)
                .filter(c -> "file-start".equals(c.getAttribute("position")));

        var renderedFileStart = mjOutsideRaws.map(c -> c.renderMjml(renderer));
        //
        var res = new StringBuilder();
        res.append(renderedFileStart.collect(Collectors.joining(" ")));
        res.append("<!doctype html>\n");
        res.append("<html lang=\"").append(context.language).append("\" dir=\"").append(context.dir).append("\" ");
        res.append("xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n");
        res.append("  <head>\n");
        res.append("    <title>").append(context.title).append("</title>\n");
        res.append("""
                    <!--[if !mso]><!-->
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <!--<![endif]-->
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style type="text/css">
                      #outlook a { padding:0; }
                      body { margin:0;padding:0;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%; }
                      table, td { border-collapse:collapse;mso-table-lspace:0pt;mso-table-rspace:0pt; }
                      img { border:0;height:auto;line-height:100%; outline:none;text-decoration:none;-ms-interpolation-mode:bicubic; }
                      p { display:block;margin:13px 0; }
                    </style>           
                    <!--[if mso]>
                    <noscript>
                    <xml>
                    <o:OfficeDocumentSettings>
                      <o:AllowPNG/>
                      <o:PixelsPerInch>96</o:PixelsPerInch>
                    </o:OfficeDocumentSettings>
                    </xml>
                    </noscript>
                    <![endif]-->     
                    <!--[if lte mso 11]>
                    <style type="text/css">
                      .mj-outlook-group-fix { width:100% !important; }
                    </style>
                    <![endif]-->
                """);
        buildFontsTags(body, context, res);
        res.append(buildMediaQueriesTags(forceOWADesktop, context));
        if (!context.componentsHeadStyle.isEmpty() || !context.headStyle.isEmpty()) {
            res.append("    \n    \n    <style type=\"text/css\">\n");
            res.append(buildComponentsHeadStyle(context));
            res.append(buildHeadStyle(context));
            res.append("    \n    \n    </style>\n");
        }
        if (!context.styles.isEmpty()) {
            res.append("    <style type=\"text/css\">\n");
            res.append(buildStyles(context));
            res.append("    \n    </style>\n");
        }
        res.append(headRaw);
        res.append("    \n  </head>\n");
        res.append("  <body style=\"word-spacing:normal;"); // TODO: see https://github.com/mjmlio/mjml/blob/master/packages/mjml-core/src/helpers/skeleton.js
        if (!isNullOrWhiteSpace(context.backgroundColor)) {
            res.append("background-color:").append(context.backgroundColor).append(";");
        }
        res.append("\">\n");
        buildPreview(context, res);
        res.append(body);
        res.append("  </body>\n");
        res.append("</html>\n  ");

        //
        return Utils.mergeOutlookConditionals(res);
    }

    private static void buildPreview(GlobalContext context, StringBuilder res) {
        if (isNullOrWhiteSpace(context.previewText)) {
            return;
        }

        res.append("<div style=\"display:none;font-size:1px;color:#ffffff;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;\">");
        res.append(context.previewText);
        res.append("</div>\n");
    }

    private static StringBuilder buildComponentsHeadStyle(GlobalContext context) {
        var sb = new StringBuilder();
        for (var css : context.componentsHeadStyle) {
            sb.append(css).append("\n");
        }
        return sb;
    }

    private static StringBuilder buildHeadStyle(GlobalContext context) {
        var sb = new StringBuilder();
        for (var css : context.headStyle.entrySet()) {
            if (isNullOrWhiteSpace(css.getValue()))
                continue;
            sb.append(css.getValue()).append("\n");
        }
        return sb;
    }

    private static StringBuilder buildStyles(GlobalContext context) {
        var sb = new StringBuilder();
        for (var css : context.styles) {
            sb.append(css).append("\n");
        }
        return sb;
    }

    private static StringBuilder buildMediaQueriesTags(boolean forceOWADesktop, GlobalContext context) {
        var sb = new StringBuilder();
        if (context.mediaQueries.isEmpty())
            return sb;

        sb.append("  <style type=\"text/css\">\n");
        sb.append("    @media only screen and (min-width:").append(context.breakpoint).append(") {\n");
        for (var mediaQuery : context.mediaQueries.entrySet()) {
            sb.append("      .").append(mediaQuery.getKey()).append(" ").append(mediaQuery.getValue()).append("\n");
        }
        sb.append("    }\n");
        sb.append("\n");
        sb.append("  </style>\n");

        // thunderbird media queries
        sb.append("  <style media=\"screen and (min-width:").append(context.breakpoint).append(")\">\n");
        for (var mediaQuery : context.mediaQueries.entrySet()) {
            sb.append("    .moz-text-html .").append(mediaQuery.getKey()).append(" ").append(mediaQuery.getValue()).append("\n");
        }
        sb.append("\n");
        sb.append("  </style>\n");

        if (forceOWADesktop) {
            sb.append("  <style type=\"text/css\">\n");
            for (var mediaQuery : context.mediaQueries.entrySet()) {
                sb.append("      [owa] .").append(mediaQuery.getKey()).append(" ").append(mediaQuery.getValue()).append("\n");
            }
            sb.append("  </style>\n");
        }
        return sb;
    }

    // https://github.com/mjmlio/mjml/blob/master/packages/mjml-core/src/helpers/fonts.js
    private static void buildFontsTags(String content, GlobalContext context, StringBuilder sb) {
        var fontsToImport = new ArrayList<String>();
        for (var font : context.fonts.entrySet()) {
            var quotedKey = Pattern.quote(font.getKey());
            var pattern = Pattern.compile("\"[^\"]*font-family:[^\"]*" + quotedKey + "[^\"]*\"", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            var inlinePattern = Pattern.compile("font-family:[^;}]*" + quotedKey, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(content).find() || inlinePattern.matcher(content).find()) {
                fontsToImport.add(font.getValue());
            }
        }

        if (fontsToImport.isEmpty())
            return;


        sb.append("  <!--[if !mso]><!-->\n");

        for (var fontUrl : fontsToImport) {
            sb.append("  <link href=\"").append(fontUrl).append("\" rel=\"stylesheet\" type=\"text/css\">\n");
        }

        sb.append("  <style type=\"text/css\">\n");
        for (var fontUrl : fontsToImport) {
            sb.append("    @import url(").append(fontUrl).append(");\n\n");
        }
        sb.append("  </style>\n");
        sb.append("  <!--<![endif]-->\n");
    }

    private static MjmlComponent.MjmlRootComponent buildMjmlDocument(org.w3c.dom.Document document, GlobalContext context) {
        var root = (Element) document.getElementsByTagName("mjml").item(0);
        var rootComponent = new MjmlComponent.MjmlRootComponent(root, null, context);
        traverseTree(rootComponent.getElement(), rootComponent, document,  context);
        rootComponent.doSetupPostConstruction();
        return rootComponent;
    }

    private static void traverseTree(Node element, BaseComponent parentComponent, org.w3c.dom.Document document, GlobalContext context) {
        if (parentComponent.isEndingTag()) {
            return;
        }
        var childNodes = element.getChildNodes();
        var count = childNodes.getLength();
        if (count == 0) {
            return;
        }
        for (var i = 0; i < count; i++) {
            var childNode = childNodes.item(i);
            switch (childNode.getNodeType()) {
                case Node.ELEMENT_NODE: {
                    var childComponent = createMjmlComponent((Element) childNode, parentComponent, context);
                    parentComponent.getChildren().add(childComponent);
                    traverseTree(childNode, childComponent, document, context);
                    break;
                }
                case Node.COMMENT_NODE: {
                    var childCommentNode = (Comment) childNode;
                    var commentElement = document.createElement("html-comment");
                    commentElement.setTextContent(childCommentNode.getTextContent());
                    var childComponent = createMjmlComponent(commentElement, parentComponent, context);
                    parentComponent.getChildren().add(childComponent);
                    break;
                }
                case Node.TEXT_NODE: {
                    var childElementText = (Text) childNode;
                    if (childElementText.getWholeText().isEmpty())
                        continue;
                    var textElement = document.createElement("html-text");
                    textElement.setNodeValue(childNode.getTextContent());
                    textElement.setTextContent(childNode.getTextContent());
                    var childComponent = createMjmlComponent(textElement, parentComponent, context);
                    parentComponent.getChildren().add(childComponent);
                    break;
                }
                default:
                    break;
            }
        }
    }


    private static BaseComponent createMjmlComponent(Element element, BaseComponent parent, GlobalContext context) {
        var elementTag = element.getNodeName().toLowerCase(Locale.ROOT);
        return switch (elementTag) {
            case "mjml" -> new MjmlComponent.MjmlRootComponent(element, parent, context);
            case "mj-head" -> new MjmlComponentHead(element, parent, context);
            case "mj-title" -> new MjmlComponentHeadTitle(element, parent, context);
            case "mj-preview" -> new MjmlComponentHeadPreview(element, parent, context);
            case "mj-attributes" -> new MjmlComponentHeadAttributes(element, parent, context);
            case "mj-breakpoint" -> new MjmlComponentHeadBreakpoint(element, parent, context);
            case "mj-font" -> new MjmlComponentHeadFont(element, parent, context);
            case "mj-style" -> new MjmlComponentHeadStyle(element, parent, context);
            case "mj-body" -> new MjmlComponentBody(element, parent, context);
            case "mj-wrapper" -> new MjmlComponentWrapper(element, parent, context);
            case "mj-section" -> new MjmlComponentSection(element, parent, context);
            case "mj-group" -> new MjmlComponentGroup(element, parent, context);
            case "mj-column" -> new MjmlComponentColumn(element, parent, context);
            case "mj-text" -> new MjmlComponentText(element, parent, context);
            case "mj-spacer" -> new MjmlComponentSpacer(element, parent, context);
            case "mj-divider" -> new MjmlComponentDivider(element, parent, context);
            case "mj-raw" -> new MjmlComponentRaw(element, parent, context);
            case "mj-image" -> new MjmlComponentImage(element, parent, context);
            case "mj-include" -> handleInclude(element, parent, context);
            case "mj-button" -> new MjmlComponentButton(element, parent, context);
            case "mj-hero" -> new MjmlComponentHero(element, parent, context);
            case "mj-social" -> new MjmlComponentSocial(element, parent, context);
            case "mj-social-element" -> new MjmlComponentSocialElement(element, parent, context);
            case "mj-table" -> new MjmlComponentTable(element, parent, context);
            case "mj-navbar" -> new MjmlComponentNavbar(element, parent, context);
            case "mj-navbar-link" -> new MjmlComponentNavbarLink(element, parent, context);
            case "mj-accordion" -> new MjmlComponentAccordion(element, parent, context);
            case "mj-accordion-title" -> new MjmlComponentAccordionTitle(element, parent, context);
            case "mj-accordion-text" -> new MjmlComponentAccordionText(element, parent, context);
            case "mj-accordion-element" -> new MjmlComponentAccordionElement(element, parent, context);
            case "mj-carousel" -> new MjmlComponentCarousel(element, parent, context);
            case "mj-carousel-image" -> new MjmlComponentCarouselImage(element, parent, context);
            case "html-text" -> new HtmlComponent.HtmlTextComponent(element, parent, context);
            case "html-comment" -> new HtmlComponent.HtmlCommentComponent(element, parent, context);
            default -> new HtmlComponent.HtmlRawComponent(element, parent, context);
        };
    }

    private static BaseComponent handleInclude(Element element, BaseComponent parent, GlobalContext context) {

        var path = element.getAttribute("path");
        if (context.includeResolver == null || path == null) {
            return new HtmlComponent.HtmlRawComponent(element, parent, context);
        }

        var includeResolver = context.includeResolver;
        var resolvedPath = includeResolver.resolvePath(path, context.basePath, context.currentResourcePaths);

        var attributeType = element.getAttribute("type");
        if ("html".equals(attributeType) || "css".equals(attributeType)) {
            var resource = includeResolver.resolveAsString(resolvedPath);
            return new HtmlComponent.HtmlRawComponent(element, parent, context);
        } else {
            context.currentResourcePaths.push(resolvedPath);
            try {
                // read document as mjml
                throw new IllegalStateException("");
            } finally {
                context.currentResourcePaths.pop();
            }
        }
    }
}
