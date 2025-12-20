package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import org.w3c.dom.Element;

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
