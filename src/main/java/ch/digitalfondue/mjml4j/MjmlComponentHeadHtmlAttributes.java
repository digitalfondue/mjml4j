package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import java.util.Locale;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class MjmlComponentHeadHtmlAttributes extends BaseComponent.HeadComponent {

  MjmlComponentHeadHtmlAttributes(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return Utils.EMPTY_MAP;
  }

  @Override
  void handler() {
    var childNodes = getElement().getChildNodes();
    int count = childNodes.getLength();
    for (int i = 0; i < count; i++) {
      var childNode = childNodes.item(i);
      if (childNode.getNodeType() == Node.ELEMENT_NODE
          && childNode instanceof Element element
          && "mj-selector".equals(element.getTagName().toLowerCase(Locale.ROOT))) {
        handleSelector(element);
      }
    }
  }

  private void handleSelector(Element element) {
    String selectorPath = element.getAttribute("path");
    var selectorChildNodes = element.getChildNodes();
    int selectorChildNodesCount = selectorChildNodes.getLength();
    for (int i = 0; i < selectorChildNodesCount; i++) {
      var childNode = selectorChildNodes.item(i);
      if (childNode.getNodeType() == Node.ELEMENT_NODE
          && childNode instanceof Element attrElem
          && "mj-html-attribute".equals(attrElem.getTagName().toLowerCase(Locale.ROOT))) {
        String attributeName = attrElem.getAttribute("name");
        String value = attrElem.getTextContent();
        context.addHtmlAttributes(selectorPath, attributeName, value);
      }
    }
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    handler();
    return new StringBuilder(0);
  }
}
