package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentHead extends BaseComponent.HeadComponent {

  MjmlComponentHead(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return Utils.EMPTY_MAP;
  }

  @Override
  void handler() {}
}
