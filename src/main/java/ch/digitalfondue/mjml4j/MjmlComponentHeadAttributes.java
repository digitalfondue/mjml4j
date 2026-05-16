package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class MjmlComponentHeadAttributes extends BaseComponent.HeadComponent {

  MjmlComponentHeadAttributes(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
    process();
  }

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return Utils.EMPTY_MAP;
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    handler();
    return new StringBuilder(0);
  }

  @Override
  void handler() {}

  private void process() {
    var childNodes = getElement().getChildNodes();
    var count = childNodes.getLength();
    for (var i = 0; i < count; i++) {
      var childNode = childNodes.item(i);
      if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode instanceof Element element) {
        handleElement(element);
      }
    }
  }

  private void handleElement(Element element) {
    if ("mj-class".equals(element.getTagName())) {
      var className = element.getAttribute("name");
      if (Utils.isNullOrWhiteSpace(className)) {
        return;
      }

      var attributesLength = element.getAttributes().getLength();
      var attributes = element.getAttributes();
      for (var i = 0; i < attributesLength; i++) {
        var attribute = (Attr) attributes.item(i);
        var attributeName = attribute.getName();
        var attributeValue = attribute.getValue();
        if (!"name".equals(attributeName)) {
          context.setClassAttribute(attributeName, className, attributeValue);
        }
      }
      // handle child elements
      if (element.hasChildNodes()) {
        var childNodes = element.getChildNodes();
        int childCount = childNodes.getLength();
        for (var i = 0; i < childCount; i++) {
          var childNode = childNodes.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE
              && childNode instanceof Element childElement) {
            String childTagName = childElement.getTagName();
            var childAttributesLength = childElement.getAttributes().getLength();
            var childAttributes = childElement.getAttributes();
            for (var j = 0; j < childAttributesLength; j++) {
              var childAttribute = (Attr) childAttributes.item(j);
              var attributeName = childAttribute.getName();
              var attributeValue = childAttribute.getValue();
              System.err.println(childTagName + " " + attributeName + " " + attributeValue);
            }
          }
        }
      }
      //
    } else {
      var tagName = element.getTagName();
      var attributesLength = element.getAttributes().getLength();
      var attributes = element.getAttributes();
      for (var i = 0; i < attributesLength; i++) {
        var attribute = (Attr) attributes.item(i);
        var attributeName = attribute.getName();
        var attributeValue = attribute.getValue();
        context.setTypeAttribute(attributeName, tagName, attributeValue);
      }
    }
  }

  @Override
  boolean isEndingTag() {
    return true;
  }
}
