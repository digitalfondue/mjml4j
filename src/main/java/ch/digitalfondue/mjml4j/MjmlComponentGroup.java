package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.floatToString;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

class MjmlComponentGroup extends BaseComponent.BodyComponent {

    private String containerWidth = null;


    MjmlComponentGroup(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            "background-color", of(null, AttributeType.COLOR),
            "direction", of("ltr"),
            "vertical-align", of(null),
            "width", of(null)
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("div", mapOf(
                "font-size", "0",
                "line-height", "0",
                "text-align", "left",
                "display", "inline-block",
                "width", "100%",
                "direction", getAttribute("direction"),
                "vertical-align", getAttribute("vertical-align"),
                "background-colour", getAttribute("background-colour")
        ));

        cssStyleLibraries.add("tdOutlook", mapOf(
                "vertical-align", getAttribute("vertical-align"),
                "width", getWidthAsPixel()
        ));
    }

    @Override
    CssBoxModel getBoxModel() {

        var containerWidth = CssUnitParser.parse(context.containerWidth);


        if (hasParentComponent()) {
            var parent = (BodyComponent) getParentComponent();


            var parentSectionColumnCount = getSectionColumnCount();


            var sectionWidth = parent.cssBoxModel.boxWidth;


            if (hasAttribute("width")) {
                this.containerWidth = getAttribute("width");
            } else {
                this.containerWidth = floatToString(sectionWidth / parentSectionColumnCount) + "px";
            }

            var parsedWidth = CssUnitParser.parse(this.containerWidth);


            if (parsedWidth.unit.equals("%")) {
                parsedWidth.value = sectionWidth * parsedWidth.value / 100;
                this.containerWidth = floatToString(parsedWidth.value) + "px";
            } else {
                this.containerWidth = floatToString(parsedWidth.value) + "px";
            }

            var columnWidth = CssUnitParser.parse(this.containerWidth);

            return new CssBoxModel(parsedWidth.value, 0, 0, columnWidth.value);
        }

        return new CssBoxModel(containerWidth.value, 0, 0, containerWidth.value);
    }

    private int getSectionColumnCount() {
        var childNodes = getElement().getParentNode().getChildNodes();

        int count = 0;
        for (int i = 0; i < childNodes.getLength(); i++) {
            var child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                count++;
            }
        }
        return count;
    }

    private CssUnitParser.CssParsedUnit getParsedWidth() {
        String width;
        if (hasAttribute("width"))
            width = getAttribute("width");
        else
            width = floatToString(100 / getSectionColumnCount()) + "%";
        var parsedWidth = CssUnitParser.parse(width);
        return parsedWidth;
    }

    private String getWidthAsPixel() {
        var parsedWidth = getParsedWidth();
        var parsedContainerWidth = CssUnitParser.parse(containerWidth);

        if (parsedWidth.unit.equals("%"))
            return floatToString(parsedContainerWidth.value * parsedWidth.value / 100) + "px";

        return parsedWidth.toString();
    }

    private String getColumnClass() {
        var parsedWidth = getParsedWidth();
        var formattedClassNb = floatToString(parsedWidth.value).replace('.', '-');


        var className = "mj-column-px-" + formattedClassNb;


        if (parsedWidth.unit.equals("%")) {
            className = "mj-column-per-" + formattedClassNb;
        }

        context.addMediaQuery(className, parsedWidth);
        return className;
    }

    private String getElementWidth(String width) {
        if (Utils.isNullOrWhiteSpace(width)) {
            var parsedContainerWidth = CssUnitParser.parse(containerWidth);
            float columnWidth = parsedContainerWidth.value / getSectionColumnCount();
            return floatToString(columnWidth) + "px";
        }

        var parsedWidth = CssUnitParser.parse(width);

        if (parsedWidth.unit.equals("%"))
            return floatToString(100 * parsedWidth.value / getContainerInnerWidth()) + "px";

        return parsedWidth.toString();
    }

    @Override
    StringBuilder renderChildren(HtmlRenderer renderer) {
        var sb = new StringBuilder();
        for (var childComponent : getChildren()) {
            var childContent = childComponent.renderMjml(renderer);
            if (Utils.isNullOrWhiteSpace(childContent))
                continue;
            if (childComponent.isRawElement()) {
                sb.append(childContent);
            } else if (childComponent instanceof BodyComponent component){
                sb.append("<!--[if mso | IE]>\n");
                sb.append("<td ").append(component.htmlAttributes(mapOf(
                        "style", component.inlineCss(mapOf(
                                        "align", component.getAttribute("align"),
                                        "vertical-align", component.getAttribute("vertical-align"),
                                        "width", getElementWidth(component.hasAttribute("width") ? component.getAttribute("width") : ((MjmlComponentColumn) component).getWidthAsPixel())
                                )
                        )
                ))).append(" >\n");
                sb.append("<![endif]-->\n");
                sb.append(childContent);
                sb.append("<!--[if mso | IE]>\n");
                sb.append("</td>\n");
                sb.append("<![endif]-->\n");
            }
        }
        return sb;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();

        var classesName = getColumnClass() + " mj-outlook-group-fix";
        if (hasAttribute("css-class"))
            classesName += " " + getAttribute("css-class");

        res.append("<div ").append(htmlAttributes(mapOf(
                "class", classesName,
                "style", "div"))).append(">\n");
        res.append("<!--[if mso | IE]>\n");
        res.append("<table ").append(htmlAttributes(mapOf(
                "bgcolor", hasAttribute("background-color") ? getAttribute("background-color").equals("none") ? getAttribute("background-color") : null : null,
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation"
        ))).append(" >\n");
        res.append("<tr>\n");
        res.append("<![endif]-->\n");
        res.append(renderChildren(renderer));
        res.append("<!--[if mso | IE]>\n</tr>\n</table>\n<![endif]-->\n");
        res.append("</div>\n");
        return res;
    }

    @Override
    String getInheritingAttribute(String attributeName) {
        return "mobileWidth".equals(attributeName) ? "mobileWidth" : null;
    }
}
