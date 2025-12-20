package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.conditionalTag;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentAccordionTitle extends BaseComponent.BodyComponent {
  MjmlComponentAccordionTitle(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  @Override
  boolean isEndingTag() {
    return true;
  }

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          entry("background-color", of(null, AttributeType.COLOR)),
          entry("color", of(null, AttributeType.COLOR)),
          entry("font-size", of("13px")),
          entry("font-family", of(null)),
          entry("font-weight", of(null)),
          entry("padding-bottom", of(null)),
          entry("padding-left", of(null)),
          entry("padding-right", of(null)),
          entry("padding-top", of(null)),
          entry("padding", of("16px")));

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    cssStyleLibraries.add(
        "td",
        mapOf(
            "width", "100%",
            "background-color", getAttribute("background-color"),
            "color", getAttribute("color"),
            "font-size", getAttribute("font-size"),
            "font-family", resolveFontFamily(),
            "font-weight", getAttribute("font-weight"),
            "padding", getAttribute("padding"),
            "padding-bottom", getAttribute("padding-bottom"),
            "padding-left", getAttribute("padding-left"),
            "padding-right", getAttribute("padding-right"),
            "padding-top", getAttribute("padding-top")));

    cssStyleLibraries.add("table", mapOf("width", "100%", "border-bottom", getAttribute("border")));

    cssStyleLibraries.add(
        "td2",
        mapOf(
            "padding", "16px",
            "background", getAttribute("background-color"),
            "vertical-align", getAttribute("icon-align")));

    cssStyleLibraries.add(
        "img",
        mapOf(
            "display", "none",
            "width", getAttribute("icon-width"),
            "height", getAttribute("icon-height")));
  }

  private String resolveFontFamily() {
    if (hasRawAttribute("font-family")) {
      return getAttribute("font-family");
    }
    if (localContext.elementFontFamily() != null) {
      return localContext.elementFontFamily();
    } else if (localContext.accordionFontFamily() != null) {
      return localContext.accordionFontFamily();
    } else {
      return getAttribute("font-family");
    }
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    var res = new StringBuilder();

    renderer.openTag("div", htmlAttributes(mapOf("class", "mj-accordion-title")), res);

    renderer.openTag(
        "table",
        htmlAttributes(
            mapOf(
                "cellspacing", "0",
                "cellpadding", "0",
                "style", "table")),
        res);
    renderer.openTag("tbody", res);
    renderer.openTag("tr", res);

    if ("right".equals(getAttribute("icon-position"))) {
      res.append(renderTitle(renderer));
      res.append(renderIcons(renderer));
    } else {
      res.append(renderIcons(renderer));
      res.append(renderTitle(renderer));
    }

    renderer.closeTag("tr", res);
    renderer.closeTag("tbody", res);
    renderer.closeTag("table", res);
    renderer.closeTag("div", res);
    return res;
  }

  private StringBuilder renderIcons(HtmlRenderer renderer) {
    var res = new StringBuilder();
    renderer.openTag(
        "td",
        htmlAttributes(
            mapOf(
                "class", "mj-accordion-ico",
                "style", "td2")),
        res);

    renderer.openCloseTag(
        "img",
        htmlAttributes(
            mapOf(
                "src",
                getAttribute("icon-wrapped-url"),
                "alt",
                getAttribute("icon-wrapped-alt"),
                "class",
                "mj-accordion-more",
                "style",
                "img")),
        res);

    renderer.openCloseTag(
        "img",
        htmlAttributes(
            mapOf(
                "src",
                getAttribute("icon-unwrapped-url"),
                "alt",
                getAttribute("icon-unwrapped-alt"),
                "class",
                "mj-accordion-less",
                "style",
                "img")),
        res);

    renderer.closeTag("td", res);

    return conditionalTag(res, true);
  }

  private StringBuilder renderTitle(HtmlRenderer renderer) {
    var res = new StringBuilder();

    renderer.openTag(
        "td", htmlAttributes(mapOf("class", getAttribute("css-class"), "style", "td")), res);
    DOMSerializer.serializeInner(getElement(), res);
    renderer.closeTag("td", res);
    return res;
  }
}
