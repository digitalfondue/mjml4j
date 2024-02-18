package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

class MjmlComponentHeadStyle extends BaseComponent.HeadComponent {

    MjmlComponentHeadStyle(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            "inline", of(null)
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void handler() {
        var css = getContent();
        if (Utils.isNullOrWhiteSpace(css)) {
            return;
        }
        context.addStyle(css, hasAttribute("inline"));
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        this.handler();
        return new StringBuilder(0);
    }
}
