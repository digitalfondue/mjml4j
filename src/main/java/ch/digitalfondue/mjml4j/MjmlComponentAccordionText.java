package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.Map;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

class MjmlComponentAccordionText extends BaseComponent.BodyComponent {
    MjmlComponentAccordionText(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    @Override
    boolean isEndingTag() {
        return true;
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            Map.entry("background-color", of(null, AttributeType.COLOR)),
            Map.entry("font-size", of("13px")),
            Map.entry("font-family", of(null)),
            Map.entry("font-weight", of(null)),
            Map.entry("letter-spacing", of(null)),
            Map.entry("line-height", of("1")),
            Map.entry("color", of(null, AttributeType.COLOR)),
            Map.entry("padding-bottom", of(null)),
            Map.entry("padding-left", of(null)),
            Map.entry("padding-right", of(null)),
            Map.entry("padding-top", of(null)),
            Map.entry("padding", of("16px"))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("td", mapOf(
                "background", getAttribute("background-color"),
                "font-size", getAttribute("font-size"),
                "font-family", getAttribute("font-family"),
                "font-weight", getAttribute("font-weight"),
                "letter-spacing", getAttribute("letter-spacing"),
                "line-height", getAttribute("line-height"),
                "color", getAttribute("color"),
                "padding-bottom", getAttribute("padding-bottom"),
                "padding-left", getAttribute("padding-left"),
                "padding-right", getAttribute("padding-right"),
                "padding-top", getAttribute("padding-top"),
                "padding", getAttribute("padding")
        ));

        cssStyleLibraries.add("table", mapOf(
                "width", "100%",
                "border-bottom", getAttribute("border")
        ));
    }


    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();

        renderer.openTag("div", htmlAttributes(mapOf("class", "mj-accordion-content")), res);
        renderer.openTag("table", htmlAttributes(mapOf(
                "cellspacing", "0",
                "cellpadding", "0",
                "style", "table"
        )), res);

        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);

        // render content
        renderer.openTag("td", htmlAttributes(mapOf(
                "class", getAttribute("css-class"),
                "style", "td"
        )), res);
        DOMSerializer.serializeInner(getElement(), res);
        renderer.closeTag("td", res);
        //
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        renderer.closeTag("div", res);
        return res;
    }
}
