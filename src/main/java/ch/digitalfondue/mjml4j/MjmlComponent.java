package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;

class MjmlComponent {

    // root component
    static class MjmlRootComponent extends BaseComponent.BodyComponent {

        MjmlRootComponent(Element element, BaseComponent parent, GlobalContext context) {
            super(element, parent, context);
        }

        @Override
        LinkedHashMap<String, AttributeValueType> allowedAttributes() {
            return Utils.EMPTY_MAP;
        }

    }
}
