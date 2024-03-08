package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.Locale;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.doubleToString;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

class MjmlComponentDivider extends BaseComponent.BodyComponent {
    MjmlComponentDivider(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }


    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("border-color", of("#000000", AttributeType.COLOR)),
            entry("border-style", of("solid")),
            entry("border-width", of("4px")),
            entry("container-background-color", of(null, AttributeType.COLOR)),
            entry("padding", of("10px 25px")),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("width", of("100%")),
            entry("align", of("center"))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    private String getOutlookWidth() {
        var containerWidth = CssUnitParser.parse(doubleToString(getContainerOuterWidth()));
        var paddingSize =
                getShorthandAttributeValue("padding", "left") +
                        getShorthandAttributeValue("padding", "right");

        var parsedWidth = CssUnitParser.parse(getAttribute("width"));

        switch (parsedWidth.unit.toLowerCase(Locale.ROOT)) {
            case "%": {
                var effectiveWidth = containerWidth.value - paddingSize;
                var percentMultiplier = parsedWidth.value / 100;
                return doubleToString(effectiveWidth * percentMultiplier) + "px";
            }
            case "px":
                return parsedWidth.toString();
            default:
                return doubleToString(containerWidth.value - paddingSize) + "px";
        }
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {

        var pStyle = mapOf(
                "border-top", getAttribute("border-style") + " " + getAttribute("border-width") + " " + getAttribute("border-color"),
                "font-size", "1px",
                "margin", "0px auto",
                "width", getAttribute("width")
        );

        cssStyleLibraries.add("p", pStyle);

        var outlookStyle = new LinkedHashMap<>(pStyle);
        outlookStyle.put("width", getOutlookWidth());

        cssStyleLibraries.add("outlook", outlookStyle);
    }

    private StringBuilder renderAfter(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.appendCurrentSpacing(res);
        res.append("<!--[if mso | IE]>");
        res.append("<table ").append(htmlAttributes(mapOf(
                "align", "center",
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "style", "outlook",
                "role", "presentation",
                "width", getOutlookWidth()
        ))).append(" >");
        res.append("<tr>");
        res.append("<td style=\"height:0;line-height:0;\">");
        res.append(" &nbsp;\n");
        res.append("</td>");
        res.append("</tr>");
        res.append("</table>");
        res.append("<![endif]-->\n");
        return res;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.openTag("p", htmlAttributes(mapOf("style", "p")), res);
        renderer.closeTag("p", res);
        res.append(renderAfter(renderer));
        return res;
    }
}
