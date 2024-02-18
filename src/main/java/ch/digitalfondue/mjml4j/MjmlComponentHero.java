package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.floatToString;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

class MjmlComponentHero extends BaseComponent.BodyComponent {

    MjmlComponentHero(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("mode", of("fixed-height")),
            entry("height", of("0px")),
            entry("background-url", of(null)),
            entry("background-width", of(null)),
            entry("background-height", of(null)),
            entry("background-position", of("center center")),
            entry("border-radius", of(null)),
            entry("container-background-color", of(null, AttributeType.COLOR)),
            entry("inner-background-color", of(null, AttributeType.COLOR)),
            entry("inner-padding", of(null)),
            entry("inner-padding-top", of(null)),
            entry("inner-padding-left", of(null)),
            entry("inner-padding-right", of(null)),
            entry("inner-padding-bottom", of(null)),
            entry("padding", of("0px")),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("background-color", of("#ffffff", AttributeType.COLOR)),
            entry("vertical-align", of("top"))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    private String getBackground() {
        var backgroundColor = getAttribute("background-color");
        var hasBackgroundUrl = getElement().hasAttribute("background-url");
        var res = new StringBuilder();
        res.append(backgroundColor);
        if (hasBackgroundUrl) {
            res.append(" ");
            res.append("url('").append(getAttribute("background-url")).append("')");
            res.append(" no-repeat ").append(getAttribute("background-position")).append(" / cover");
        }
        return res.toString();
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        var backgroundHeight = CssUnitParser.parse(getAttribute("background-height"));
        var backgroundWidth = CssUnitParser.parse(getAttribute("background-width"));
        var backgroundRatio = Math.round(backgroundHeight.value / backgroundWidth.value * 100d);
        var width = getElement().hasAttribute("background-width") ? getAttribute("background-width") : Utils.floatToString(getContainerInnerWidth()) + "px";

        cssStyleLibraries.add("div", mapOf(
                "margin", "0 auto",
                "max-width", Utils.floatToString(getContainerInnerWidth()) + "px"
        ));

        cssStyleLibraries.add("table", mapOf(
                "width", "100%"
        ));

        cssStyleLibraries.add("tr", mapOf(
                "vertical-align", "top"
        ));

        cssStyleLibraries.add("td-fluid", mapOf(
                "width", "0.01%",
                "padding-bottom", backgroundRatio + "%",
                "mso-padding-bottom-alt", "0"
        ));

        cssStyleLibraries.add("hero", mapOf(
                "background", getBackground(),
                "background-position", getAttribute("background-position"),
                "background-repeat", "no-repeat",
                "padding", getAttribute("padding"),
                "padding-top", getAttribute("padding-top"),
                "padding-left", getAttribute("padding-left"),
                "padding-right", getAttribute("padding-right"),
                "padding-bottom", getAttribute("padding-bottom"),
                "vertical-align", getAttribute("vertical-align")
        ));

        cssStyleLibraries.add("outlook-table", mapOf(
                "width", floatToString(getContainerInnerWidth()) + "px"
        ));

        cssStyleLibraries.add("outlook-td", mapOf(
                "line-height", "0",
                "font-size", "0",
                "mso-line-height-rule", "exactly"
        ));

        cssStyleLibraries.add("outlook-inner-table", mapOf(
                "width", floatToString(getContainerInnerWidth()) + "px"
        ));

        cssStyleLibraries.add("outlook-image", mapOf(
                "border", "0",
                "height", getAttribute("background-height"),
                "mso-position-horizontal", "center",
                "position", "absolute",
                "top", "0",
                "width", width,
                "z-index", "-3"
        ));

        cssStyleLibraries.add("outlook-inner-td", mapOf(
                "background-color", getAttribute("inner-background-color"),
                "padding", getAttribute("inner-padding"),
                "padding-top", getAttribute("inner-padding-top"),
                "padding-left", getAttribute("inner-padding-left"),
                "padding-right", getAttribute("inner-padding-right"),
                "padding-bottom", getAttribute("inner-padding-bottom")
        ));

        cssStyleLibraries.add("inner-table", mapOf(
                "width", "100%",
                "margin", "0px"
        ));

        cssStyleLibraries.add("inner-div", mapOf(
                "background-color", getAttribute("inner-background-color"),
                "float", getAttribute("align"),
                "margin", "0px auto",
                "width", getAttribute("width")
        ));
    }

    @Override
    StringBuilder renderChildren(HtmlRenderer renderer) {
        var res = new StringBuilder();
        for (var childComponent : getChildren()) {
            if (childComponent.isRawElement()) {
                res.append(childComponent.renderMjml(renderer));
            } else if (childComponent instanceof BodyComponent bodyComponent) {
                renderer.openTag("tr", res);
                renderer.openTag("td", bodyComponent.htmlAttributes(mapOf(
                        "align", bodyComponent.getAttribute("align"),
                        "background", bodyComponent.getAttribute("container-background-color"),
                        "class", bodyComponent.getAttribute("css-class"),
                        "style", inlineCss(mapOf(
                                "background", bodyComponent.getAttribute("container-background-color"),
                                "font-size", "0px",
                                "padding", bodyComponent.getAttribute("padding"),
                                "padding-top", bodyComponent.getAttribute("padding-top"),
                                "padding-right", bodyComponent.getAttribute("padding-right"),
                                "padding-bottom", bodyComponent.getAttribute("padding-bottom"),
                                "padding-left", bodyComponent.getAttribute("padding-left"),
                                "word-break", "break-word"
                        ))
                )), res);
                res.append(childComponent.renderMjml(renderer));
                renderer.closeTag("td", res);
                renderer.closeTag("tr", res);
            }
        }
        return res;
    }

    private StringBuilder renderContent(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.appendCurrentSpacing(res);
        res.append("<!--[if mso | IE]>");
        res.append("<table ").append(htmlAttributes(mapOf(
                "align", getAttribute("align"),
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "style", "outlook-inner-table",
                "width", floatToString(getContainerInnerWidth())
        ))).append(" >");
        res.append("<tr>");
        res.append("<td ").append(htmlAttributes(mapOf("style", "outlook-inner-td"))).append(">");
        res.append("<![endif]-->\n");
        renderer.openTag("div", htmlAttributes(mapOf(
                "align", getAttribute("align"),
                "class", "mj-hero-content",
                "style", "inner-div"
        )), res);
        renderer.openTag("table", htmlAttributes(mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "inner-table"
        )), res);
        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);
        renderer.openTag("td", htmlAttributes(mapOf("style", "inner-td")), res);
        renderer.openTag("table", htmlAttributes(mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "inner-table")), res);
        renderer.openTag("tbody", res);
        res.append(renderChildren(renderer));
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        renderer.closeTag("td", res);
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        renderer.closeTag("div", res);
        renderer.appendCurrentSpacing(res);
        res.append("<!--[if mso | IE]>");
        res.append("</td></tr></table>");
        res.append("<![endif]-->\n");
        return res;
    }

    private StringBuilder renderMode(HtmlRenderer renderer) {
        var res = new StringBuilder();
        var background = getAttribute("background-url");
        if ("fluid-height".equals(getAttribute("mode"))) {
            var magicTd = htmlAttributes(mapOf("style", "td-fluid")); // TODO: FIXME
            renderer.openCloseTag("td", magicTd, res);
            renderer.openTag("td", htmlAttributes(mapOf(
                    "background", background,
                    "style", "hero"
            )), res);
            //res.append("<td ").append().append(">\n");
            res.append(renderContent(renderer));
            renderer.closeTag("td", res);
            renderer.openCloseTag("td", magicTd, res);

        } else {
            var heightCss = CssUnitParser.parse(getAttribute("height"));
            var paddingTop = getShorthandAttributeValue("padding", "top");
            var paddingBottom = getShorthandAttributeValue("padding", "bottom");
            var height = heightCss.value;
            if (heightCss.unit.equals("%")) {
                height = getContainerInnerWidth() / 100 * height;
            }
            height -= paddingTop + paddingBottom;
            renderer.openTag("td", htmlAttributes(mapOf(
                    "background", background,
                    "style", "hero",
                    "height", floatToString(height)
            ), mapOf("height", floatToString(height) + "px")), res);
            res.append(renderContent(renderer));
            renderer.closeTag("td", res);
        }
        return res;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.appendCurrentSpacing(res);
        res.append("<!--[if mso | IE]>");
        res.append("<table ").append(htmlAttributes(mapOf(
                "align", "center",
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "outlook-table",
                "width", floatToString(getContainerInnerWidth())
        ))).append(" >");
        res.append("<tr>");
        res.append("<td ").append(htmlAttributes(mapOf(
                "style", "outlook-td"
        ))).append(">");
        res.append("<v:image ").append(htmlAttributes(mapOf(
                "style", "outlook-image",
                "src", getAttribute("background-url"),
                "xmlns:v", "urn:schemas-microsoft-com:vml"
        ))).append(" />");
        res.append("<![endif]-->\n");
        renderer.openTag("div", htmlAttributes(mapOf(
                "align", getAttribute("align"),
                "class", getAttribute("css-class"),
                "style", "div"
        )), res);
        renderer.openTag("table", htmlAttributes(mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "table"
        )), res);
        renderer.openTag("tbody", res);
        renderer.openTag("tr", htmlAttributes(mapOf(
                "style", "tr"
        )), res);
        res.append(renderMode(renderer));
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        renderer.closeTag("div", res);
        renderer.appendCurrentSpacing(res);
        res.append("<!--[if mso | IE]>");
        res.append("</td>");
        res.append("</tr>");
        res.append("</table>");
        res.append("<![endif]-->\n");
        return res;
    }
}
