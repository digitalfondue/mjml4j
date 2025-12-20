package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import java.util.LinkedHashMap;
import java.util.Set;
import org.w3c.dom.Element;

class MjmlComponentSocial extends BaseComponent.BodyComponent {

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          entry("align", of("center")),
          entry("border-radius", of("3px")),
          entry("container-background-color", of(null, AttributeType.COLOR)),
          entry("color", of("#333333", AttributeType.COLOR)),
          entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
          entry("font-size", of("13px")),
          entry("font-style", of(null)),
          entry("font-weight", of(null)),
          entry("icon-size", of("20px")),
          entry("icon-height", of(null)),
          entry("icon-padding", of(null)),
          entry("inner-padding", of(null)),
          entry("line-height", of("22px")),
          entry("mode", of("horizontal")),
          entry("padding-bottom", of(null)),
          entry("padding-left", of(null)),
          entry("padding-right", of(null)),
          entry("padding-top", of(null)),
          entry("padding", of("10px 25px")),
          entry("table-layout", of(null)),
          entry("text-padding", of(null)),
          entry("text-decoration", of("none")),
          entry("vertical-align", of(null)));

  MjmlComponentSocial(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    cssStyleLibraries.add("tableVertical", mapOf("margin", "0px"));
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    return "horizontal".equals(getAttribute("mode"))
        ? renderHorizontal(renderer)
        : renderVertical(renderer);
  }

  private static final Set<String> INHERITING_ATTRIBUTES =
      Set.of(
          "border-radius",
          "color",
          "font-family",
          "font-size",
          "font-style",
          "font-weight",
          "icon-height",
          "icon-padding",
          "icon-size",
          "padding",
          "line-height",
          "text-padding",
          "text-decoration");

  @Override
  String getInheritingAttribute(String attributeName) {
    if ("padding".equals(attributeName)) {
      attributeName = "inner-padding";
    }
    return INHERITING_ATTRIBUTES.contains(attributeName) ? getAttribute(attributeName) : null;
  }

  private StringBuilder renderVertical(HtmlRenderer renderer) {
    var res = new StringBuilder();
    renderer.openTag(
        "table",
        htmlAttributes(
            mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "tableVertical")),
        res);
    renderer.openTag("tbody", res);
    res.append(renderChildren(renderer));
    renderer.closeTag("tbody", res);
    renderer.closeTag("table", res);

    return res;
  }

  private StringBuilder renderHorizontal(HtmlRenderer renderer) {
    var res = new StringBuilder();
    renderer.appendCurrentSpacing(res);
    renderer.openIfMsoIE(res, true);
    res.append("<table ")
        .append(
            htmlAttributes(
                mapOf(
                    "align", getAttribute("align"),
                    "border", "0",
                    "cellpadding", "0",
                    "cellspacing", "0",
                    "role", "presentation")))
        .append(" >\n");
    renderer.openTag("tr", res);
    renderer.closeEndif(res, true);
    for (var childComponent : getChildren()) {
      var childContent = childComponent.renderMjml(renderer);

      if (Utils.isNullOrWhiteSpace(childContent)) continue;

      if (childComponent.isRawElement()) {
        res.append(childContent);
      } else if (childComponent instanceof MjmlComponentSocialElement socialElementComponent) {
        renderer.appendCurrentSpacing(res);
        renderer.openIfMsoIE(res, true);
        renderer.openTag("td", res);
        renderer.closeEndif(res, true);
        renderer.openTag(
            "table",
            socialElementComponent.htmlAttributes(
                mapOf(
                    "align", getAttribute("align"),
                    "border", "0",
                    "cellpadding", "0",
                    "cellspacing", "0",
                    "role", "presentation",
                    "style", "float:none;display:inline-table;")),
            res);
        renderer.openTag("tbody", res);
        res.append(childContent);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        renderer.appendCurrentSpacing(res);
        renderer.openIfMsoIE(res, true);
        renderer.closeTag("td", res);
        renderer.closeEndif(res, true);
      }
    }
    renderer.openIfMsoIE(res, true);
    renderer.closeTag("tr", res);
    renderer.closeTag("table", res);
    renderer.closeEndif(res, true);
    return res;
  }
}
