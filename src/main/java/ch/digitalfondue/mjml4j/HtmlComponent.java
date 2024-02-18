package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;

class HtmlComponent {

    static class HtmlRawComponent extends BaseComponent.BodyComponent {
        HtmlRawComponent(Element element, BaseComponent parent, GlobalContext context) {
            super(element, parent, context);
        }

        @Override
        boolean isRawElement() {
            return true;
        }

        @Override
        LinkedHashMap<String, AttributeValueType> allowedAttributes() {
            return Utils.EMPTY_MAP;
        }

        @Override
        void setAttributes() {
            var attributes = getElement().getAttributes();
            var attributesCount = attributes.getLength();
            for (var i = 0; i < attributesCount; i++) {
                var attribute = attributes.item(i);
                getAttributes().put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }

        @Override
        StringBuilder renderMjml(HtmlRenderer renderer) {
            var nodeName = getElement().getNodeName();
            return new StringBuilder("<").append(nodeName).append(">\n")
                    .append(renderChildren(renderer))
                    .append("</").append(nodeName).append(">");
        }
    }

    static class HtmlCommentComponent extends BaseComponent.BodyComponent {
        HtmlCommentComponent(Element element, BaseComponent parent, GlobalContext context) {
            super(element, parent, context);
        }

        @Override
        boolean isRawElement() {
            return true;
        }

        @Override
        void setupPostConstruction() {
            // do nothing
        }

        @Override
        LinkedHashMap<String, AttributeValueType> allowedAttributes() {
            return Utils.EMPTY_MAP;
        }

        @Override
        StringBuilder renderMjml(HtmlRenderer renderer) {
            return new StringBuilder("<!--").append(getElement().getTextContent()).append("-->\n");
        }
    }

    static class HtmlTextComponent extends BaseComponent.BodyComponent {

        HtmlTextComponent(Element element, BaseComponent parent, GlobalContext context) {
            super(element, parent, context);
        }

        @Override
        void setupPostConstruction() {
            // nothing
        }

        @Override
        boolean isRawElement() {
            return true;
        }

        @Override
        LinkedHashMap<String, AttributeValueType> allowedAttributes() {
            return Utils.EMPTY_MAP;
        }

        @Override
        StringBuilder renderMjml(HtmlRenderer renderer) {
            return new StringBuilder(getElement().getTextContent());
        }
    }
}
