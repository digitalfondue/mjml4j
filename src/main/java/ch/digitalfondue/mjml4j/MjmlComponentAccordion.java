package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

import java.util.LinkedHashMap;
import java.util.Map;
import org.w3c.dom.Element;

class MjmlComponentAccordion extends BaseComponent.BodyComponent {
  MjmlComponentAccordion(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  @Override
  LocalContext getChildContext() {
    return localContext.withAccordionFontFamily(getAttribute("font-family"));
  }

  @Override
  String headStyle() {
    return """
                  noinput.mj-accordion-checkbox { display:block!important; }

                  @media yahoo, only screen and (min-width:0) {
                    .mj-accordion-element { display:block; }
                    input.mj-accordion-checkbox, .mj-accordion-less { display:none!important; }
                    input.mj-accordion-checkbox + * .mj-accordion-title { cursor:pointer; touch-action:manipulation; -webkit-user-select:none; -moz-user-select:none; user-select:none; }
                    input.mj-accordion-checkbox + * .mj-accordion-content { overflow:hidden; display:none; }
                    input.mj-accordion-checkbox + * .mj-accordion-more { display:block!important; }
                    input.mj-accordion-checkbox:checked + * .mj-accordion-content { display:block; }
                    input.mj-accordion-checkbox:checked + * .mj-accordion-more { display:none!important; }
                    input.mj-accordion-checkbox:checked + * .mj-accordion-less { display:block!important; }
                  }

                  .moz-text-html input.mj-accordion-checkbox + * .mj-accordion-title { cursor: auto; touch-action: auto; -webkit-user-select: auto; -moz-user-select: auto; user-select: auto; }
                  .moz-text-html input.mj-accordion-checkbox + * .mj-accordion-content { overflow: hidden; display: block; }
                  .moz-text-html input.mj-accordion-checkbox + * .mj-accordion-ico { display: none; }

                  @goodbye { @gmail }
                """;
  }

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          Map.entry("container-background-color", of(null, AttributeValueType.AttributeType.COLOR)),
          Map.entry("border", of("2px solid black")),
          Map.entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
          Map.entry("icon-align", of("middle")),
          Map.entry("icon-width", of("32px")),
          Map.entry("icon-height", of("32px")),
          Map.entry("icon-wrapped-url", of("https://i.imgur.com/bIXv1bk.png")),
          Map.entry("icon-wrapped-alt", of("+")),
          Map.entry("icon-unwrapped-url", of("https://i.imgur.com/w4uTygT.png")),
          Map.entry("icon-unwrapped-alt", of("-")),
          Map.entry("icon-position", of("right")),
          Map.entry("padding-bottom", of(null)),
          Map.entry("padding-left", of(null)),
          Map.entry("padding-right", of(null)),
          Map.entry("padding-top", of(null)),
          Map.entry("padding", of("10px 25px")));

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    cssStyleLibraries.add(
        "table",
        mapOf(
            "width", "100%",
            "border-collapse", "collapse",
            "border", getAttribute("border"),
            "border-bottom", "none",
            "font-family", getAttribute("font-family")));
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    var res = new StringBuilder();
    renderer.openTag(
        "table",
        htmlAttributes(
            mapOf(
                "cellspacing", "0",
                "cellpadding", "0",
                "class", "mj-accordion",
                "style", "table")),
        res);
    renderer.openTag("tbody", res);
    for (var child : getChildren()) {
      res.append(child.renderMjml(renderer));
    }
    renderer.closeTag("tbody", res);
    renderer.closeTag("table", res);

    return res;
  }

  @Override
  String getInheritingAttribute(String name) {
    return switch (name) {
      case "border",
          "icon-align",
          "icon-height",
          "icon-position",
          "icon-width",
          "icon-unwrapped-url",
          "icon-unwrapped-alt",
          "icon-wrapped-url",
          "icon-wrapped-alt" ->
          getAttribute(name);
      default -> null;
    };
  }
}
