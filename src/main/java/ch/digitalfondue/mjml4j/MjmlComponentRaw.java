package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentRaw extends BaseComponent.BodyComponent {

  private final String content;

  MjmlComponentRaw(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
    this.content = null;
  }

  MjmlComponentRaw(Element element, BaseComponent parent, GlobalContext context, String content) {
    super(element, parent, context);
    this.content = content;
  }

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return Utils.EMPTY_MAP;
  }

  @Override
  StringBuilder renderChildren(HtmlRenderer renderer) {
    if (content != null) {
      return new StringBuilder(content);
    }
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
