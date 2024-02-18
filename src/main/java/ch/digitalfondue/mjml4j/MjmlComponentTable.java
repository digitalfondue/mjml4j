package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static ch.digitalfondue.mjml4j.Utils.mergeLeft;
import static java.util.Map.entry;

class MjmlComponentTable extends BaseComponent.BodyComponent {
    MjmlComponentTable(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("align", of("left")),
            entry("border", of("none")),
            entry("cellpadding", of("0")),
            entry("cellspacing", of("0")),
            entry("container-background-color", of(null, AttributeType.COLOR)),
            entry("color", of("#000000", AttributeType.COLOR)),
            entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
            entry("font-size", of("13px")),
            entry("font-weight", of(null)),
            entry("line-height", of("22px")),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("padding", of("10px 25px")),
            entry("role", of(null)),
            entry("table-layout", of("auto")),
            entry("vertical-align", of(null)),
            entry("width", of("100%"))
    );

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("table", mapOf(
                "color", getAttribute("color"),
                "font-family", getAttribute("font-family"),
                "font-size", getAttribute("font-size"),
                "line-height", getAttribute("line-height"),
                "table-layout", getAttribute("table-layout"),
                "width", getAttribute("width"),
                "border", getAttribute("border")
        ));
    }

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var width = getAttribute("width");
        var parsedWidth = CssUnitParser.parse(width);
        var res = new StringBuilder();

        var tableAttributes = mapOf(
                "cellpadding", getAttribute("cellpadding"),
                "cellspacing", getAttribute("cellspacing"),
                "role", getAttribute("role")
        );

        renderer.openTag("table", htmlAttributes(
                mergeLeft(tableAttributes, mapOf(
                        "width", parsedWidth.isPercent() ? width : parsedWidth.toString(),
                        "border", "0",
                        "style", "table"
                ))), res);
        DOMSerializer.serializeInner(getElement(), res);
        renderer.closeTag("table", res);
        return res;
    }

    @Override
    boolean isEndingTag() {
        return true;
    }
}
