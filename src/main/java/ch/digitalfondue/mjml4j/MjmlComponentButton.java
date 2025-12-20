package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.doubleToString;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentButton extends BaseComponent.BodyComponent {

  MjmlComponentButton(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          entry("align", of("center")),
          entry("background-color", of("#414141", AttributeType.COLOR)),
          entry("border-bottom", of(null)),
          entry("border-left", of(null)),
          entry("border-radius", of("3px")),
          entry("border-right", of(null)),
          entry("border-top", of(null)),
          entry("border", of("none")),
          entry("color", of("#ffffff", AttributeType.COLOR)),
          entry("container-background-color", of(null, AttributeType.COLOR)),
          entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
          entry("font-size", of("13px")),
          entry("font-style", of(null)),
          entry("font-weight", of("normal")),
          entry("height", of(null)),
          entry("href", of(null)),
          entry("name", of(null)),
          entry("title", of(null)),
          entry("inner-padding", of("10px 25px")),
          entry("letter-spacing", of(null)),
          entry("line-height", of("120%")),
          entry("padding-bottom", of(null)),
          entry("padding-left", of(null)),
          entry("padding-right", of(null)),
          entry("padding-top", of(null)),
          entry("padding", of("10px 25px")),
          entry("rel", of(null)),
          entry("target", of("_blank")),
          entry("text-decoration", of("none")),
          entry("text-transform", of("none")),
          entry("vertical-align", of("middle")),
          entry("text-align", of(null)),
          entry("width", of(null)));

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    cssStyleLibraries.add(
        "table",
        mapOf(
            "border-collapse", "separate",
            "width", getAttribute("width"),
            "line-height", "100%"));

    cssStyleLibraries.add(
        "td",
        mapOf(
            "border", getAttribute("border"),
            "border-bottom", getAttribute("border-bottom"),
            "border-left", getAttribute("border-left"),
            "border-radius", getAttribute("border-radius"),
            "border-right", getAttribute("border-right"),
            "border-top", getAttribute("border-top"),
            "cursor", "auto",
            "font-style", getAttribute("font-style"),
            "height", getAttribute("height"),
            "mso-padding-alt", getAttribute("inner-padding"),
            "text-align", getAttribute("text-align"),
            "background", getAttribute("background-color")));

    cssStyleLibraries.add(
        "content",
        mapOf(
            "display", "inline-block",
            "width", calculateAWidth(getAttribute("width")),
            "background", getAttribute("background-color"),
            "color", getAttribute("color"),
            "font-family", getAttribute("font-family"),
            "font-size", getAttribute("font-size"),
            "font-style", getAttribute("font-style"),
            "font-weight", getAttribute("font-weight"),
            "line-height", getAttribute("line-height"),
            "letter-spacing", getAttribute("letter-spacing"),
            "margin", "0",
            "text-decoration", getAttribute("text-decoration"),
            "text-transform", getAttribute("text-transform"),
            "padding", getAttribute("inner-padding"),
            "mso-padding-alt", "0px",
            "text-align", getAttribute("text-align"),
            "border-radius", getAttribute("border-radius")));
  }

  private String calculateAWidth(String content) {
    if (Utils.isNullOrWhiteSpace(content)) {
      return null;
    }

    var parsedWidth = CssUnitParser.parse(content);

    if (!parsedWidth.isPx()) {
      return null;
    }

    var borders = cssBoxModel.borderWidth();
    var innerPaddings =
        getShorthandAttributeValue("inner-padding", "left")
            + getShorthandAttributeValue("inner-padding", "right");

    var calculatedWidth = parsedWidth.value() - innerPaddings - borders;
    return doubleToString(calculatedWidth) + "px";
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    var tag = hasAttribute("href") ? "a" : "p";
    var res = new StringBuilder();
    renderer.openTag(
        "table",
        htmlAttributes(
            mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "table")),
        res);
    renderer.openTag("tbody", res);
    renderer.openTag("tr", res);
    renderer.openTag(
        "td",
        htmlAttributes(
            mapOf(
                "align", "center",
                "bgcolor", getAttribute("background-color"),
                "role", "presentation",
                "style", "td",
                "valign", getAttribute("vertical-align"))),
        res);
    renderer.appendCurrentSpacing(res);
    res.append("<")
        .append(tag)
        .append(" ")
        .append(
            htmlAttributes(
                mapOf(
                    "href", getAttribute("href"),
                    "rel", getAttribute("rel"),
                    "name", getAttribute("name"),
                    "style", "content",
                    "target", tag.equals("a") ? getAttribute("target") : null)))
        .append("> ");
    DOMSerializer.serializeInner(getElement(), res);
    res.append(" </").append(tag).append(">\n");
    renderer.closeTag("td", res);
    renderer.closeTag("tr", res);
    renderer.closeTag("tbody", res);
    renderer.closeTag("table", res);
    return res;
  }

  @Override
  boolean isEndingTag() {
    return true;
  }
}
