package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;

class MjmlComponentRaw extends BaseComponent.BodyComponent {

    MjmlComponentRaw(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return Utils.EMPTY_MAP;
    }

    @Override
    StringBuilder renderChildren(HtmlRenderer renderer) {
        var res = new StringBuilder();
        DOMSerializer.serializeInner(getElement(), res);
        return res;
    }

    @Override
    boolean isRawElement() {
        return true;
    }

    @Override
    boolean isEndingTag() {
        return true;
    }
}
