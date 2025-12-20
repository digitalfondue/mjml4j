package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

// see https://github.com/mjmlio/mjml/blob/master/packages/mjml-spacer/src/index.js
class MjmlComponentSpacer extends BaseComponent.BodyComponent {

  MjmlComponentSpacer(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          entry("border", of(null)),
          entry("border-bottom", of(null)),
          entry("border-left", of(null)),
          entry("border-right", of(null)),
          entry("border-top", of(null)),
          entry("container-background-color", of(null, AttributeType.COLOR)),
          entry("padding-bottom", of(null)),
          entry("padding-left", of(null)),
          entry("padding-right", of(null)),
          entry("padding-top", of(null)),
          entry("padding", of(null)),
          entry("height", of("20px")));

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    cssStyleLibraries.add(
        "div",
        mapOf(
            "height", getAttribute("height"),
            "line-height", getAttribute("height")));
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    var res = new StringBuilder();
    res.append("<div ").append(htmlAttributes(mapOf("style", "div"))).append(">");
    res.append("&#8202;");
    res.append("</div>\n");
    return res;
  }
}
