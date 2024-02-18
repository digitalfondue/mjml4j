package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

class MjmlComponentHeadFont extends BaseComponent.HeadComponent {

    MjmlComponentHeadFont(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            "name", of(null),
            "href", of(null)
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void handler() {
        if (hasAttribute("name") && hasAttribute("href")) {
            context.addFont(getAttribute("name"), getAttribute("href"));
        }
    }
}
