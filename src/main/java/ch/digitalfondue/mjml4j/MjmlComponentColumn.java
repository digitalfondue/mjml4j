package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.doubleToString;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import java.util.LinkedHashMap;
import org.w3c.dom.Element;

class MjmlComponentColumn extends BaseComponent.BodyComponent {

  private String containerWidth;
  private int parentSectionColumnCount;

  MjmlComponentColumn(Element element, BaseComponent parent, GlobalContext context) {
    super(element, parent, context);
  }

  private boolean hasBorderRadius() {
    return hasAttribute("border-radius");
  }

  private boolean hasInnerBorderRadius() {
    return hasAttribute("inner-border-radius");
  }

  private boolean hasGutter() {
    return hasAttribute("padding")
        || hasAttribute("padding-bottom")
        || hasAttribute("padding-left")
        || hasAttribute("padding-right")
        || hasAttribute("padding-top");
  }

  private int getSectionColumnCount() {
    return getParent().getChildren().stream()
        .filter(f -> !f.isRawElement())
        .mapToInt((v) -> 1)
        .sum();
  }

  @Override
  CssBoxModel getBoxModel() {

    var paddings =
        getShorthandAttributeValue("padding", "right")
            + getShorthandAttributeValue("padding", "left");
    var borders =
        getShorthandBorderValue("border", "right") + getShorthandBorderValue("border", "left");
    var innerBorders =
        getShorthandBorderValue("inner-border", "left")
            + getShorthandBorderValue("inner-border", "right");
    var allPaddings = paddings + borders + innerBorders;

    if (hasParentComponent()) {
      var parent = (BodyComponent) getParentComponent();
      parentSectionColumnCount = getSectionColumnCount();
      var sectionWidth = parent.cssBoxModel.boxWidth();
      if (hasAttribute("width")) {
        containerWidth = getAttribute("width");
      } else {
        containerWidth = doubleToString(sectionWidth / parentSectionColumnCount) + "px";
      }
      var childContainerWidth = containerWidth;

      var parsedWidth = CssUnitParser.parse(containerWidth);
      if (parsedWidth.isPercent()) {
        parsedWidth = parsedWidth.withValue((sectionWidth * parsedWidth.value() / 100));
      }
      containerWidth = doubleToString(parsedWidth.valueFullPrecision()) + "px";
      childContainerWidth = doubleToString(parsedWidth.value() - allPaddings) + "px";

      var columnWidth = CssUnitParser.parse(childContainerWidth);
      return new CssBoxModel(parsedWidth.value(), borders, paddings, columnWidth.value());
    }

    var parsedContainerWidth = CssUnitParser.parse(context.containerWidth);
    return new CssBoxModel(
        parsedContainerWidth.value(), borders, paddings, parsedContainerWidth.value());
  }

  private CssUnitParser.CssParsedUnit getParsedWidth() {
    String width =
        hasAttribute("width")
            ? getAttribute("width")
            : doubleToString(100. / getSectionColumnCount()) + "%";
    return CssUnitParser.parse(width);
  }

  private String getMobileWidth() {
    var width = getAttribute("width");
    var mobileWidth = getAttribute("mobileWidth");

    if (!"mobileWidth".equals(mobileWidth)) {
      return "100%";
    }

    if (Utils.isNullOrWhiteSpace(width)) {
      return doubleToString(100. / parentSectionColumnCount) + "%";
    }

    var parsedWidth = CssUnitParser.parse(width);

    return parsedWidth.isPercent()
        ? width
        : doubleToString(parsedWidth.value() / CssUnitParser.parse(containerWidth).value()) + "%";
  }

  String getWidthAsPixel() {

    var parsedWidth = getParsedWidth();
    var parsedContainerWidth = CssUnitParser.parse(containerWidth);

    // we don't want to cut off the precision if percent
    return parsedWidth.isPercent()
        ? parsedContainerWidth.toFullPrecisionString()
        : parsedWidth.toString();
  }

  private String getColumnClass() {
    var parsedWidth = getParsedWidth();
    var formattedClassNb = doubleToString(parsedWidth.value()).replace('.', '-');

    var className =
        parsedWidth.isPercent()
            ? "mj-column-per-" + formattedClassNb
            : "mj-column-px-" + formattedClassNb;

    context.addMediaQuery(className, parsedWidth);
    return className;
  }

  private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES =
      mapOf(
          entry("background-color", of(null, AttributeType.COLOR)),
          entry("border", of(null)),
          entry("border-bottom", of(null)),
          entry("border-left", of(null)),
          entry("border-radius", of(null)),
          entry("border-right", of(null)),
          entry("border-top", of(null)),
          entry("direction", of("ltr")),
          entry("inner-background-color", of(null, AttributeType.COLOR)),
          entry("padding-bottom", of(null)),
          entry("padding-left", of(null)),
          entry("padding-right", of(null)),
          entry("padding-top", of(null)),
          entry("inner-border", of(null)),
          entry("inner-border-bottom", of(null)),
          entry("inner-border-left", of(null)),
          entry("inner-border-radius", of(null)),
          entry("inner-border-right", of(null)),
          entry("inner-border-top", of(null)),
          entry("padding", of(null)),
          entry("vertical-align", of("top")),
          entry("width", of(null)));

  @Override
  LinkedHashMap<String, AttributeValueType> allowedAttributes() {
    return ALLOWED_DEFAULT_ATTRIBUTES;
  }

  @Override
  void setupStyles(CssStyleLibraries cssStyleLibraries) {
    var bHasGutter = hasGutter();
    cssStyleLibraries.add(
        "div",
        mapOf(
            "font-size",
            "0px",
            "text-align",
            "left",
            "direction",
            getAttribute("direction"),
            "display",
            "inline-block",
            "vertical-align",
            getAttribute("vertical-align"),
            "width",
            getMobileWidth()));

    var tableStyle =
        mapOf(
            "background-color", getAttribute("background-color"),
            "border", getAttribute("border"),
            "border-bottom", getAttribute("border-bottom"),
            "border-left", getAttribute("border-left"),
            "border-radius", getAttribute("border-radius"),
            "border-right", getAttribute("border-right"),
            "border-top", getAttribute("border-top"),
            "vertical-align", getAttribute("vertical-align"));
    if (hasBorderRadius()) {
      tableStyle.put("border-collapse", "separate");
    }

    var realTableStyle =
        bHasGutter
            ? mapOf(
                "background-color", getAttribute("inner-background-color"),
                "border", getAttribute("inner-border"),
                "border-bottom", getAttribute("inner-border-bottom"),
                "border-left", getAttribute("inner-border-left"),
                "border-radius", getAttribute("inner-border-radius"),
                "border-right", getAttribute("inner-border-right"),
                "border-top", getAttribute("inner-border-top"))
            : new LinkedHashMap<>(tableStyle);
    if (hasInnerBorderRadius()) {
      realTableStyle.put("border-collapse", "separate");
    }
    cssStyleLibraries.add("table", realTableStyle);

    cssStyleLibraries.add(
        "tdOutlook",
        mapOf(
            "vertical-align", getAttribute("vertical-align"),
            "width", getWidthAsPixel()));

    var gutterStyle = new LinkedHashMap<>(tableStyle);
    gutterStyle.put("padding", getAttribute("padding"));
    gutterStyle.put("padding-top", getAttribute("padding-top"));
    gutterStyle.put("padding-right", getAttribute("padding-right"));
    gutterStyle.put("padding-bottom", getAttribute("padding-bottom"));
    gutterStyle.put("padding-left", getAttribute("padding-left"));
    cssStyleLibraries.add("gutter", gutterStyle);
  }

  @Override
  StringBuilder renderChildren(HtmlRenderer renderer) {
    var sb = new StringBuilder();
    for (var childComponent : getChildren()) {
      var childContent = childComponent.renderMjml(renderer);
      if (Utils.isNullOrWhiteSpace(childContent)) {
        continue;
      }
      if (childComponent.isRawElement()) {
        sb.append(childContent);
      } else if (childComponent instanceof BodyComponent component) {
        sb.append("<tr>\n");
        sb.append("<td ")
            .append(
                component.htmlAttributes(
                    mapOf(
                        "align", component.getAttribute("align"),
                        "class", component.getAttribute("css-class"),
                        "style",
                            component.inlineCss(
                                mapOf(
                                    "background",
                                        component.getAttribute("container-background-color"),
                                    "font-size", "0px",
                                    "padding", component.getAttribute("padding"),
                                    "padding-top", component.getAttribute("padding-top"),
                                    "padding-right", component.getAttribute("padding-right"),
                                    "padding-bottom", component.getAttribute("padding-bottom"),
                                    "padding-left", component.getAttribute("padding-left"),
                                    "word-break", "break-word")))))
            .append(">\n");

        sb.append(childContent);

        sb.append("</td>\n");
        sb.append("</tr>\n");
      }
    }
    return sb;
  }

  private StringBuilder renderColumn(HtmlRenderer renderer) {
    var res = new StringBuilder();

    res.append("<table ")
        .append(
            htmlAttributes(
                mapOf(
                    "border", "0",
                    "cellpadding", "0",
                    "cellspacing", "0",
                    "role", "presentation",
                    "style", "table",
                    "width", "100%")))
        .append(">\n");
    res.append("<tbody>\n");
    res.append(renderChildren(renderer));
    res.append("</tbody>");
    res.append("</table>\n");

    return res;
  }

  private StringBuilder renderGutter(HtmlRenderer renderer) {
    var tableAttrs =
        mapOf(
            "border", "0",
            "cellpadding", "0",
            "cellspacing", "0",
            "role", "presentation",
            "width", "100%");
    if (hasBorderRadius()) {
      tableAttrs.put("style", "border-collapse:separate;");
    }

    var res = new StringBuilder();
    res.append("<table ").append(htmlAttributes(tableAttrs)).append(">\n");
    res.append("<tbody>\n");
    res.append("<tr>\n");
    res.append("<td ").append(htmlAttributes(mapOf("style", "gutter"))).append(">\n");
    res.append(renderColumn(renderer));
    res.append("</td>\n");
    res.append("</tr>\n");
    res.append("</tbody>\n");
    res.append("</table>\n");
    return res;
  }

  @Override
  StringBuilder renderMjml(HtmlRenderer renderer) {
    var classesName = new StringBuilder(getColumnClass()).append(" mj-outlook-group-fix");

    if (hasAttribute("css-class")) {
      classesName.append(" ").append(getAttribute("css-class"));
    }

    var bHasGutter = hasGutter();
    var res = new StringBuilder();

    res.append("<div ")
        .append(htmlAttributes(mapOf("class", classesName.toString(), "style", "div")))
        .append(">\n");
    res.append(bHasGutter ? renderGutter(renderer) : renderColumn(renderer));
    res.append("</div>\n");
    return res;
  }
}
