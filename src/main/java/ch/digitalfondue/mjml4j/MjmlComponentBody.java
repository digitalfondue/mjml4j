package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentBody extends BaseComponent.BodyComponent {
  MjmlComponentBody(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);

    if (hasAttribute("width")) {
      context.containerWidth = getAttribute("width");
    }

    if (hasAttribute("background-color")) {
      context.backgroundColor = getAttribute("background-color");
    }
  }

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          "width", of("600px", AttributeType.PIXELS),
          "background-color", of(null, AttributeType.COLOR));

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    cssStyleLibraries.add("div", mapOf("background-color", getAttribute("background-color")));
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    var res = new StringBuilder();
    var divAttrs = new LinkedHashMap<String, String>();
    if (!context.title.isEmpty()) {
      divAttrs.put("aria-label", context.title);
    }
    divAttrs.put("aria-roledescription", "email");
    divAttrs.put("class", getAttribute("class"));
    divAttrs.put("style", "div");
    divAttrs.put("role", "article");
    divAttrs.put("lang", context.language);
    divAttrs.put("dir", context.dir);
    renderer.openTag("div", htmlAttributes(divAttrs), res);
    res.append(renderChildren(renderer));
    renderer.closeTag("div", res);
    return res;
  }
}
