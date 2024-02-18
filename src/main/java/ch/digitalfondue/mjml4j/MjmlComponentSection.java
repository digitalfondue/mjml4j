package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.*;
import static java.util.Map.entry;

class MjmlComponentSection extends BaseComponent.BodyComponent {

    MjmlComponentSection(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private boolean hasBackground() {
        return hasAttribute("background-url");
    }

    private boolean isFullWidth() {
        return hasAttribute("full-width") && getAttribute("full-width").equalsIgnoreCase("full-width");
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("background-color", of(null, AttributeType.COLOR)),
            entry("background-url", of(null)),
            entry("background-repeat", of("repeat")),
            entry("background-size", of("auto")),
            entry("background-position", of("top center")),
            entry("background-position-x", of(null)),
            entry("background-position-y", of(null)),
            entry("border", of(null)),
            entry("border-bottom", of(null)),
            entry("border-left", of(null)),
            entry("border-radius", of(null)),
            entry("border-right", of(null)),
            entry("border-top", of(null)),
            entry("direction", of("ltr")),
            entry("full-width", of(null)),
            entry("padding", of("20px 0")),
            entry("padding-top", of(null)),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("text-align", of("center")),
            entry("text-padding", of("4px 4px 4px 0"))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    private String getBackgroundString() {
        var backgroundPosition = getBackgroundPosition();
        return backgroundPosition.x + backgroundPosition.y;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        return isFullWidth() ? renderFullWidth(renderer) : renderSimple(renderer);
    }

    private StringBuilder renderSimple(HtmlRenderer renderer) {
        var bHasBackground = hasBackground();
        return new StringBuilder(renderBefore(renderer))
                .append(bHasBackground ? renderWithBackground(renderSection(renderer)) : renderSection(renderer))
                .append(renderAfter());
    }

    private StringBuilder renderBefore(HtmlRenderer renderer) {
        var bgcolorAttr = this.getAttribute("background-color");
        var tableAttr = mapOf(
                "align", "center",
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "class", suffixCssClasses(getAttribute("css-class"), "outlook"),
                "role", "presentation",
                "style", inlineCss(mapOf("width", floatToString(getContainerOuterWidth()) + "px")),
                "width", floatToString(getContainerOuterWidth())
        );
        if (!isNullOrWhiteSpace(bgcolorAttr)) {
            tableAttr.put("bgcolor", bgcolorAttr);
        }

        var res = new StringBuilder();
        renderer.appendCurrentSpacing(res);
        renderer.openIfMsoIE(res);
        res.append("<table ").append(htmlAttributes(tableAttr));
        res.append(" >");
        res.append("<tr>");
        res.append("<td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">");
        renderer.closeEndif(res, true);
        return res;
    }

    private StringBuilder renderFullWidth(HtmlRenderer renderer) {
        var bHasBackground = hasBackground();

        var beforeSectionAfter = new StringBuilder()
                .append(renderBefore(renderer))
                .append(renderSection(renderer))
                .append(renderAfter());

        var content = bHasBackground ? renderWithBackground(beforeSectionAfter) : beforeSectionAfter;
        var res = new StringBuilder();
        res.append("<table ").append(htmlAttributes(mapOf(
                "align", "center",
                "class", getAttribute("css-class"),
                "background", getAttribute("background-url"),
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "tableFullwidth"
        ))).append(">\n");
        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);
        renderer.openTag("td", res);
        res.append(content);
        renderer.closeTag("td", res);
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        res.append("</table>\n");
        return res;
    }

    private StringBuilder renderWithBackground(StringBuilder content) {

        var containerWidth = CssUnitParser.parse(floatToString(getContainerOuterWidth()));
        var isFullWidth = isFullWidth();
        var isPercentage = "%".equalsIgnoreCase(containerWidth.unit);
        var backgroundPosition = getBackgroundPosition();

        switch (backgroundPosition.x) {
            case "left":
                backgroundPosition.x = "0%";
                break;

            case "center":
                backgroundPosition.x = "50%";
                break;

            case "right":
                backgroundPosition.x = "100%";
                break;

            default:
                if (!backgroundPosition.x.contains("%"))
                    backgroundPosition.x = "50%";
                break;
        }

        switch (backgroundPosition.y) {
            case "top":
                backgroundPosition.y = "0%";
                break;

            case "center":
                backgroundPosition.y = "50%";
                break;

            case "bottom":
                backgroundPosition.y = "100%";
                break;

            default:
                if (!backgroundPosition.y.contains("%"))
                    backgroundPosition.y = "0%";
                break;
        }

        var originX = calculateBackgroundAxisOrigin("x", backgroundPosition);
        var originY = calculateBackgroundAxisOrigin("y", backgroundPosition);

        var vSizeAttributes = new LinkedHashMap<String, String>();
        if (getAttribute("background-size").equalsIgnoreCase("cover") ||
                getAttribute("background-size").equalsIgnoreCase("contain")) {
            vSizeAttributes = mapOf(
                    "size", "1,1",
                    "aspect", getAttribute("background-size").equalsIgnoreCase("cover") ? "atleast" : "atmost"
            );
        } else if (!getAttribute("background-size").equalsIgnoreCase("auto")) {
            var backgroundSize = getAttribute("background-size");
            var bgSplit = Utils.splitBySpace(backgroundSize);
            if (bgSplit.length == 1) {
                vSizeAttributes = mapOf(
                        "size", getAttribute("background-size"),
                        "aspect", "atmost"
                );
            } else {
                vSizeAttributes = mapOf(
                        "size", backgroundSize.replace(' ', ',')
                );
            }
        }

        var vmlType = getAttribute("background-repeat").equalsIgnoreCase("no-repeat") ? "frame" : "tile";
        if (getAttribute("background-size").equalsIgnoreCase("auto")) {
            vmlType = "tile"; // If no size provided, keep old behavior because outlook can't use original image size with "frame"

            // Also ensure that images are still cropped the same way
            originX = new CssCoordinate("0.5", "0.5");
            originY = new CssCoordinate("0", "0");
        }

        var res = new StringBuilder();
        res.append("<!--[if mso | IE]>\n");
        res.append("<v:rect ").append(
                htmlAttributes(
                        mapOf("style", inlineCss(isFullWidth ?
                                        mapOf("mso-width-percent", "1000") :
                                        mapOf("width", floatToString(getContainerOuterWidth()) + "px")),
                                "xmlns:v", "urn:schemas-microsoft-com:vml",
                                "fill", "true",
                                "stroke", "false"
                        )
                )
        ).append(">\n");
        res.append("<v:fill ").append(
                htmlAttributes(
                        mergeLeft(
                                mapOf(
                                        "origin", originX.x + ", " + originY.x,
                                        "position", originX.y + ", " + originY.y,
                                        "src", getAttribute("background-url"),
                                        "color", getAttribute("background-color"),
                                        "type", vmlType
                                ), vSizeAttributes)
                )
        ).append(" />\n");
        res.append("<v:textbox style=\"mso-fit-shape-to-text:true\" inset=\"0,0,0,0\">\n");
        res.append("<![endif]-->\n");
        res.append(content);
        res.append("<!--[if mso | IE]>\n");
        res.append("</v:textbox>\n");
        res.append("</v:rect>\n");
        res.append("<![endif]-->\n");
        return res;
    }

    private String renderAfter() {
        return "   <!--[if mso | IE]>" +
                "      </td>" +
                "    </tr>" +
                "  </table>" +
                "  <![endif]-->";
    }


    private StringBuilder renderSection(HtmlRenderer renderer) {
        var bHasBackground = hasBackground();
        var bIsFullWidth = isFullWidth();

        var res = new StringBuilder();
        renderer.openTag("div", htmlAttributes(mapOf(
                "class", bIsFullWidth ? null : getAttribute("css-class"),
                "style", "div")), res);
        if (bHasBackground) {
            renderer.openTag("div", htmlAttributes(mapOf("style", "innerDiv")), res);
        }

        renderer.openTag("table", htmlAttributes(mapOf(
                "align", "center",
                "background", bIsFullWidth ? null : getAttribute("background-url"),
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "table"
        )), res);
        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);
        renderer.openTag("td", htmlAttributes(mapOf("style", "td")), res);
        res.append("\n");
        renderer.appendCurrentSpacing(res);
        res.append("<!--[if mso | IE]>");
        res.append("<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        res.append("<![endif]-->\n");
        res.append(this.renderChildren(renderer));
        res.append("\n<!--[if mso | IE]></table><![endif]-->");
        renderer.closeTag("td", res);
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        if (bHasBackground) {
            renderer.closeTag("div", res);
        }
        renderer.closeTag("div", res);
        return res;
    }

    @Override
    StringBuilder renderChildren(HtmlRenderer renderer) {
        var sb = new StringBuilder();

        if (this.getChildren().isEmpty())
            return sb;

        sb.append("\n<!--[if mso | IE]>\n");
        sb.append("<tr>\n");
        sb.append("<![endif]-->\n");

        for (var childComponent : this.getChildren()) {
            var childContent = childComponent.renderMjml(renderer);
            if (Utils.isNullOrWhiteSpace(childContent))
                continue;

            if (childComponent.isRawElement()) {
                sb.append(childContent);
            } else {
                var component = (BodyComponent) childComponent;
                sb.append("\n<!--[if mso | IE]>\n");
                sb.append("<td ").append(component.htmlAttributes(mapOf(
                        "align", component.getAttribute("align"),
                        "class", suffixCssClasses(component.getAttribute("css-class"), "outlook"),
                        "style", component.styles("tdOutlook")
                ))).append(" >\n");
                sb.append("<![endif]-->\n");
                //
                sb.append(childContent);
                //
                sb.append("<!--[if mso | IE]>\n");
                sb.append("</td>\n");
                sb.append("<![endif]-->\n");
            }
        }


        sb.append("\n<!--[if mso | IE]>\n");
        sb.append("</tr>\n");
        sb.append("<![endif]-->\n");
        return sb;
    }

    private String getBackground() {
        if (!hasBackground()) {
            return getAttribute("background-color");
        }

        return getAttribute("background-color") +
                " url(" +
                getAttribute("background-url") +
                ") " +
                getBackgroundString() +
                " / " +
                getAttribute("background-size") +
                " " +
                getAttribute("background-repeat");
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        var isFullWidth = isFullWidth();

        var background = hasBackground() ? mapOf(
                "background", getBackground(),
                "background-position", getBackgroundString(),
                "background-repeat", getAttribute("background-repeat"),
                "background-size", getAttribute("background-size")
        ) :
                mapOf(
                        "background", getAttribute("background-color"),
                        "background-color", getAttribute("background-color")
                );

        cssStyleLibraries.add("tableFullwidth",
                isFullWidth ? mergeLeft(background, mapOf(
                        "width", "100%",
                        "border-radius", getAttribute("border-radius")
                )) :
                        mapOf("width", "100%",
                                "border-radius", getAttribute("border-radius")
                        )
        );

        cssStyleLibraries.add("table",
                !isFullWidth ? mergeLeft(background,
                        mapOf(
                                "width", "100%",
                                "border-radius", getAttribute("border-radius")
                        )) :
                        mapOf(
                                "width", "100%",
                                "border-radius", getAttribute("border-radius")
                        )
        );

        cssStyleLibraries.add("td", mapOf(
                "border", getAttribute("border"),
                "border-bottom", getAttribute("border-bottom"),
                "border-left", getAttribute("border-left"),
                "border-right", getAttribute("border-right"),
                "border-top", getAttribute("border-top"),
                "direction", getAttribute("direction"),
                "font-size", "0px",
                "padding", getAttribute("padding"),
                "padding-bottom", getAttribute("padding-bottom"),
                "padding-left", getAttribute("padding-left"),
                "padding-right", getAttribute("padding-right"),
                "padding-top", getAttribute("padding-top"),
                "text-align", getAttribute("text-align")
        ));

        cssStyleLibraries.add("div",
                isFullWidth ? mapOf(
                        "margin", "0px auto",
                        "border-radius", getAttribute("border-radius"),
                        "max-width", floatToString(getContainerOuterWidth()) + "px"

                ) : mergeLeft(background, mapOf(
                        "margin", "0px auto",
                        "border-radius", getAttribute("border-radius"),
                        "max-width", floatToString(getContainerOuterWidth()) + "px")
                ));

        cssStyleLibraries.add("innerDiv", mapOf(
                "line-height", "0",
                "font-size", "0"
        ));
    }

    private CssCoordinate getBackgroundPosition() {
        var position = parseBackgroundPosition();
        return new CssCoordinate(
                hasAttribute("background-position-x") ? getAttribute("background-position-x") : position.x,
                hasAttribute("background-position-y") ? getAttribute("background-position-y") : position.y
        );
    }

    private CssCoordinate parseBackgroundPosition() {
        var posSplit = Utils.splitBySpace(getAttribute("background-position"));


        if (posSplit.length == 1) {
            var value = posSplit[0];

            if (value.equalsIgnoreCase("top") || value.equalsIgnoreCase("bottom")) {
                return new CssCoordinate("center", value);
            }
            return new CssCoordinate(value, "center");
        }

        if (posSplit.length == 2) {
            var value1 = posSplit[0];
            var value2 = posSplit[1];

            if (value1.equalsIgnoreCase("top") ||
                    value1.equalsIgnoreCase("bottom") ||
                    (value1.equalsIgnoreCase("center") &&
                            value2.equalsIgnoreCase("left")) ||
                    value2.equalsIgnoreCase("right")) {
                return new CssCoordinate(value2, value1);
            }

            return new CssCoordinate(value1, value2);
        }
        return new CssCoordinate("center", "top");
    }

    private CssCoordinate calculateBackgroundAxisOrigin(String axis, CssCoordinate coordinate) {
        var isX = axis.equalsIgnoreCase("x");
        var isBackgroundRepeat = hasAttribute("background-repeat") && getAttribute("background-repeat").equalsIgnoreCase("repeat");

        var position = isX ? coordinate.x : coordinate.y;

        float positionFloat;
        float originFloat;

        if (position.contains("%")) {
            var percentage = CssUnitParser.parse(position);

            if (isBackgroundRepeat) {
                positionFloat = percentage.value;
                originFloat = percentage.value;
            } else {
                float computed = (-50 + (percentage.value * 100)) / 100;
                positionFloat = computed;
                originFloat = computed;
            }
        } else if (isBackgroundRepeat) {
            positionFloat = isX ? 0.5f : 0f;
            originFloat = isX ? 0.5f : 0f;
        } else {
            positionFloat = isX ? 0f : -0.5f;
            originFloat = isX ? 0f : -0.5f;
        }
        return new CssCoordinate(floatToString(originFloat), floatToString(positionFloat));
    }
}
