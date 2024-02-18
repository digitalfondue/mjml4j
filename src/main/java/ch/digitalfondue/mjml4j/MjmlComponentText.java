package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

class MjmlComponentText extends BaseComponent.BodyComponent {

    MjmlComponentText(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("align", of("left")),
            entry("background-color", of(null, AttributeType.COLOR)),
            entry("color", of("#000000", AttributeType.COLOR)),
            entry("container-background-color", of(null, AttributeType.COLOR)),
            entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
            entry("font-size", of("13px")),
            entry("font-style", of(null)),
            entry("font-weight", of(null)),
            entry("height", of(null)),
            entry("letter-spacing", of(null)),
            entry("line-height", of("1")),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("padding", of("10px 25px")),
            entry("text-decoration", of(null)),
            entry("text-transform", of(null)),
            entry("vertical-align", of(null))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("text", mapOf(
                "font-family", getAttribute("font-family"),
                "font-size", getAttribute("font-size"),
                "font-style", getAttribute("font-style"),
                "font-weight", getAttribute("font-weight"),
                "letter-spacing", getAttribute("letter-spacing"),
                "line-height", getAttribute("line-height"),
                "text-align", getAttribute("align"),
                "text-decoration", getAttribute("text-decoration"),
                "text-transform", getAttribute("text-transform"),
                "color", getAttribute("color"),
                "height", getAttribute("height")
        ));
    }

    private StringBuilder renderContent() {
        var res = new StringBuilder();
        res.append("<div ").append(htmlAttributes(mapOf("style", "text"))).append(">");
        DOMSerializer.serializeInner(getElement(), res);
        res.append("</div>\n");
        return res;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {

        var height = getAttribute("height");
        if (Utils.isNullOrWhiteSpace(height)) {
            return renderContent();
        }
        var res = new StringBuilder();

        res.append(Utils.conditionalTag("<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "<tr>\n" +
                "<td height=\"" + height + "\" style=\"vertical-align:top;height:" + height + ";\">\n"));
        res.append(renderContent());
        res.append(Utils.conditionalTag("""
                </td>
                </tr>
                </table>
                """));

        return res;
    }

    @Override
    boolean isEndingTag() {
        return true;
    }
}
