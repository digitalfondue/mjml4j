package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentHeadPreview extends BaseComponent.HeadComponent {

  MjmlComponentHeadPreview(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return Utils.EMPTY_MAP;
  }

  @Override
  void handler() {
    var content = getContent();

    if (!Utils.isNullOrWhiteSpace(content)) {
      context.previewText = content;
    }
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    handler();
    return new StringBuilder(0);
  }
}
