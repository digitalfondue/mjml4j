package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.*;
import static java.util.Map.entry;

class MjmlComponentImage extends BaseComponent.BodyComponent {

    MjmlComponentImage(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }


    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("alt", of("")),
            entry("href", of(null)),
            entry("name", of(null)),
            entry("src", of(null)),
            entry("srcset", of(null)),
            entry("sizes", of(null)),
            entry("title", of(null)),
            entry("rel", of(null)),
            entry("align", of("center")),
            entry("border", of("0")),
            entry("border-bottom", of(null)),
            entry("border-left", of(null)),
            entry("border-right", of(null)),
            entry("border-top", of(null)),
            entry("border-radius", of(null)),
            entry("container-background-color", of(null, AttributeType.COLOR)),
            entry("fluid-on-mobile", of(null)),
            entry("padding", of("10px 25px")),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("target", of("_blank")),
            entry("width", of(null)),
            entry("height", of("auto")),
            entry("max-height", of(null)),
            entry("font-size", of("13px")),
            entry("usemap", of(null))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }


    private boolean isFullWidth() {
        return hasAttribute("full-width") && getAttribute("full-width").equalsIgnoreCase("full-width");
    }

    private String getContentWidth() {
        var width = hasAttribute("width") ?
                CssUnitParser.parse(getAttribute("width")) :
                CssUnitParser.parse(999999 + "px");

        return floatToString(Math.min(width.value, getContainerInnerWidth()));
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        var width = getContentWidth();
        var isFullWidth = isFullWidth();

        cssStyleLibraries.add("img", mapOf(
                "border", getAttribute("border"),
                "border-left", getAttribute("border-left"),
                "border-right", getAttribute("border-right"),
                "border-top", getAttribute("border-top"),
                "border-bottom", getAttribute("border-bottom"),
                "border-radius", getAttribute("border-radius"),
                "display", "block",
                "outline", "none",
                "text-decoration", "none",
                "height", getAttribute("height"),
                "max-height", getAttribute("max-height"),
                "min-width", isFullWidth ? "100%" : null,
                "width", "100%",
                "max-width", isFullWidth ? "100%" : null,
                "font-size", getAttribute("font-size")
        ));

        cssStyleLibraries.add("td", mapOf(
                "width", isFullWidth ? null : width + "px"
        ));

        cssStyleLibraries.add("table", mapOf(
                "min-width", isFullWidth ? "100%" : null,
                "max-width", isFullWidth ? "100%" : null,
                "width", isFullWidth ? width : null,
                "border-collapse", "collapse",
                "border-spacing", "0px"
        ));
    }

    @Override
    String headStyle() {
        String res = "    @media only screen and (max-width:" + makeLowerBreakpoint(context.breakpoint) + ") {\n" +
                "      table.mj-full-width-mobile {\n" +
                "        width: 100% !important;\n" +
                "      }\n\n" +
                "      td.mj-full-width-mobile {\n" +
                "        width: auto !important;\n" +
                "      }\n" +
                "    }\n";
        return res;
    }

    private void renderImage(HtmlRenderer renderer, StringBuilder res) {
        var bHasHeight = hasAttribute("height");
        var height = getAttribute("height");
        var hasHref = hasAttribute("href");
        if (hasHref) {
            renderer.openTag("a", htmlAttributes(mapOf(
                    "href", getAttribute("href"),
                    "target", getAttribute("target"),
                    "rel", getAttribute("rel"),
                    "name", getAttribute("name")
            )), res);
        }
        renderer.openCloseTag("img", htmlAttributes(mapOf(
                "alt", getAttribute("alt"),
                "src", getAttribute("src"),
                "srcset", getAttribute("srcset"),
                "sizes", getAttribute("sizes"),
                "style", "img",
                "title", getAttribute("title"),
                "width", getContentWidth(),
                "usemap", getAttribute("usemap"),
                "height", bHasHeight && height.equalsIgnoreCase("auto") ? height : floatToString(CssUnitParser.parse(height).value)
        )), res);
        if (hasHref) {
            renderer.closeTag("a", res);
        }
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var bIsFluidMobile = hasAttribute("fluid-on-mobile");
        var res = new StringBuilder();
        renderer.openTag("table", htmlAttributes(mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "table",
                "class", bIsFluidMobile ? "mj-full-width-mobile" : null
        )), res);
        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);
        renderer.openTag("td", htmlAttributes(mapOf(
                "style", "td",
                "class", bIsFluidMobile ? "mj-full-width-mobile" : null
        )), res);
        renderImage(renderer, res);
        renderer.closeTag("td", res);
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        return res;
    }
}
