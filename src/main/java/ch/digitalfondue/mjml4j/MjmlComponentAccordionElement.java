package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.conditionalTag;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

class MjmlComponentAccordionElement extends BaseComponent.BodyComponent {
    MjmlComponentAccordionElement(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    @Override
    LocalContext getChildContext() {
        return localContext.withElementFontFamily(getAttribute("font-family"));
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("background-color", of(null, AttributeType.COLOR)),
            entry("border", of(null)),
            entry("font-family", of(null)),
            entry("icon-align", of(null)),
            entry("icon-width", of(null)),
            entry("icon-height", of(null)),
            entry("icon-wrapped-url", of(null)),
            entry("icon-wrapped-alt", of(null)),
            entry("icon-unwrapped-url", of(null)),
            entry("icon-unwrapped-alt", of(null)),
            entry("icon-position", of(null))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("td", mapOf(
                "padding", "0px",
                "background-color", getAttribute("background-color")
        ));

        cssStyleLibraries.add("label", mapOf(
                "font-size", "13px",
                "font-family", getAttribute("font-family")
        ));

        cssStyleLibraries.add("input", mapOf(
                "display", "none"
        ));

    }

    @Override
    String getInheritingAttribute(String attributeName) {
        return switch (attributeName) {
            case "border",
                    "icon-align",
                    "icon-height",
                    "icon-position",
                    "icon-width",
                    "icon-unwrapped-url",
                    "icon-unwrapped-alt",
                    "icon-wrapped-url",
                    "icon-wrapped-alt" -> getAttribute(attributeName);
            default -> null;
        };
    }

    private void ensureMissingElements() {

        var addTitle = getChildren().stream().noneMatch(MjmlComponentAccordionTitle.class::isInstance);
        var addText = getChildren().stream().noneMatch(MjmlComponentAccordionText.class::isInstance);

        if (addTitle) {
            var element = context.document.createElement("mj-accordion-title");
            var acc = new MjmlComponentAccordionTitle(element, this, context);
            acc.doSetupPostConstruction();
            getChildren().add(0, acc);
        }

        if (addText) {
            var element = context.document.createElement("mj-accordion-text");
            var acc = new MjmlComponentAccordionText(element, this, context);
            acc.doSetupPostConstruction();
            getChildren().add(acc);
        }
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        ensureMissingElements();
        var res = new StringBuilder();

        renderer.openTag("tr", htmlAttributes(mapOf("class", getAttribute("css-class"))), res);
        renderer.openTag("td", htmlAttributes(mapOf("style", "td")), res);
        renderer.openTag("label", htmlAttributes(mapOf("class", "mj-accordion-element", "style", "label")), res);

        var tmp = new StringBuilder();
        renderer.openCloseTag("input", htmlAttributes(mapOf("class", "mj-accordion-checkbox", "type", "checkbox", "style", "input")), tmp);
        res.append(conditionalTag(tmp, true));
        renderer.openTag("div", res);
        //
        for (var child : getChildren()) {
            res.append(child.renderMjml(renderer));
        }
        //
        renderer.closeTag("div", res);
        renderer.closeTag("label", res);
        renderer.closeTag("td", res);
        renderer.closeTag("tr", res);
        return res;
    }
}
