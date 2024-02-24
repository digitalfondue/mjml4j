package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.List;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.*;
import static java.util.Map.entry;

class MjmlComponentCarousel extends BaseComponent.BodyComponent {

    private final String carouselId;
    private List<MjmlComponentCarouselImage> carouselImages;
    private int carouselImagesCount;

    MjmlComponentCarousel(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
        carouselId = genRandomHexString();
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("align", of("center")),
            entry("border-radius", of("6px")),
            entry("container-background-color", of(null, AttributeType.COLOR)),
            entry("icon-width", of("44px")),
            entry("left-icon", of("https://i.imgur.com/xTh3hln.png")),
            entry("padding", of(null)),
            entry("padding-top", of(null)),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("right-icon", of("https://i.imgur.com/os7o9kz.png")),
            entry("thumbnails", of("visible")),
            entry("tb-border", of("2px solid transparent")),
            entry("tb-border-radius", of("6px")),
            entry("tb-hover-border-color", of("#fead0d", AttributeType.COLOR)),
            entry("tb-selected-border-color", of("#ccc", AttributeType.COLOR)),
            entry("tb-width", of(null))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupPostConstruction() {
        //
        //
        carouselImages = getChildren().stream()
                .filter(MjmlComponentCarouselImage.class::isInstance)
                .map(MjmlComponentCarouselImage.class::cast)
                .toList();
        carouselImagesCount = carouselImages.size();
        for (var i = 0; i < carouselImagesCount; i++) {
            var cImg = carouselImages.get(i);
            cImg.index = i;
            cImg.carouselId = carouselId;
        }
        super.setupPostConstruction();
    }

    @Override
    String componentsHeadStyle() {
        if (carouselImagesCount == 0) {
            return "";
        }

        var res = new StringBuilder();
        res.append("""
                .mj-carousel {
                  -webkit-user-select: none;
                  -moz-user-select: none;
                  user-select: none;
                }
                """);

        res.append(".mj-carousel-").append(carouselId).append("-icons-cell {");
        res.append("  display: table-cell !important;");
        res.append("  width: ").append(getAttribute("icon-width")).append(" !important;");
        res.append("}");

        res.append("""
                .mj-carousel-radio,
                .mj-carousel-next,
                .mj-carousel-previous {
                  display: none !important;
                }
                                
                .mj-carousel-thumbnail,
                .mj-carousel-next,
                .mj-carousel-previous {
                  touch-action: manipulation;
                }
                """);

        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-radio:checked ").append("+ * ".repeat(i)).append("+ .mj-carousel-content .mj-carousel-image");
            if (i + 1 < carouselImagesCount) {
                res.append(',');
            }
        }
        res.append("""
                {
                  display: none !important;
                }
                """);

        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-radio-").append(i + 1).append(":checked ")
                    .append("+ * ".repeat(carouselImagesCount - i - 1))
                    .append("+ .mj-carousel-content .mj-carousel-image-").append(i + 1);
            if (i + 1 < carouselImagesCount) {
                res.append(',');
            }
        }
        res.append("""
                {
                  display: block !important;
                }
                                
                .mj-carousel-previous-icons,
                .mj-carousel-next-icons,
                """);

        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-radio-").append(i + 1).append(":checked ")
                    .append("+ * ".repeat(carouselImagesCount - i - 1))
                    .append("+ .mj-carousel-content .mj-carousel-next-")
                    .append(((i + (1 % carouselImagesCount) + carouselImagesCount) % carouselImagesCount) + 1)
                    .append(",\n");
        }
        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-radio-").append(i + 1).append(":checked ")
                    .append("+ * ".repeat(carouselImagesCount - i - 1))
                    .append("+ .mj-carousel-content .mj-carousel-previous-")
                    .append(((i - (1 % carouselImagesCount) + carouselImagesCount) % carouselImagesCount) + 1);

            if (i + 1 < carouselImagesCount) {
                res.append(",\n");
            }
        }
        res.append("""
                 {
                  display: block !important;
                }
                """);

        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-radio-").append(i + 1).append(":checked ")
                    .append("+ * ".repeat(carouselImagesCount - i - 1))
                    .append("+ .mj-carousel-content .mj-carousel-").append(carouselId).append("-thumbnail-")
                    .append(i + 1);
            if (i + 1 < carouselImagesCount) {
                res.append(",\n");
            }
        }
        res.append(" {");
        res.append("  border-color: ").append(getAttribute("tb-selected-border-color")).append(" !important;\n");
        res.append("}");

        res.append("""
                .mj-carousel-image img + div,
                .mj-carousel-thumbnail img + div {
                  display: none !important;
                }
                """);

        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-thumbnail:hover ")
                    .append("+ * ".repeat(carouselImagesCount - i - 1))
                    .append("+ .mj-carousel-main .mj-carousel-image");
            if (i + 1 < carouselImagesCount) {
                res.append(",\n");
            }
        }
        res.append("""
                 {
                 display: none !important;
                }
                """);

        res.append(".mj-carousel-thumbnail:hover {\n");
        res.append("  border-color: ").append(getAttribute("tb-hover-border-color")).append("  !important;\n");
        res.append("}\n");

        for (int i = 0; i < carouselImagesCount; i++) {
            res.append(".mj-carousel-").append(carouselId).append("-thumbnail-").append(i + 1).append(":hover ")
                    .append("+ * ".repeat(carouselImagesCount - i - 1))
                    .append("+ .mj-carousel-main .mj-carousel-image-").append(i + 1);
            if (i + 1 < carouselImagesCount) {
                res.append(",\n");
            }
        }
        res.append("""
                 {
                  display: block !important;
                }
                """);

        res.append("""
                .mj-carousel noinput { display:block !important; }
                .mj-carousel noinput .mj-carousel-image-1 { display: block !important;  }
                .mj-carousel noinput .mj-carousel-arrows,
                .mj-carousel noinput .mj-carousel-thumbnails { display: none !important; }
                                
                [owa] .mj-carousel-thumbnail { display: none !important; }
                
                @media screen yahoo {
                """
        );
        res.append("    .mj-carousel-").append(carouselId).append("-icons-cell,\n");
        res.append("""
                    .mj-carousel-previous-icons,
                    .mj-carousel-next-icons {
                      display: none !important;
                    }
                """);
        res.append("    .mj-carousel-").append(carouselId).append("-radio-1:checked ").append("+ *".repeat(carouselImagesCount - 1))
                .append("+ .mj-carousel-content .mj-carousel-").append(carouselId).append("-thumbnail-1 {\n");
        res.append("      border-color: transparent;");
        res.append("    }\n");
        res.append("}\n");

        return res.toString();
    }

    @Override
    String getInheritingAttribute(String attributeName) {
        return switch (attributeName) {
            case "border-radius", "tb-border", "tb-border-radius" -> getAttribute(attributeName);
            case "tb-width" -> getThumbnailsWidth();
            default -> null;
        };
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("carousel_div", mapOf(
                "display", "table",
                "width", "100%",
                "table-layout", "fixed",
                "text-align", "center",
                "font-size", "0px"
        ));

        cssStyleLibraries.add("carousel_table", mapOf(
                "caption-side", "top",
                "display", "table-caption",
                "table-layout", "fixed",
                "width", "100%"
        ));

        cssStyleLibraries.add("images_td", mapOf("padding", "0px"));

        cssStyleLibraries.add("controls_div", mapOf(
                "display", "none",
                "mso-hide", "all"
        ));
        cssStyleLibraries.add("controls_img", mapOf(
                "display", "block",
                "width", getAttribute("icon-width"),
                "height", "auto"
        ));
        cssStyleLibraries.add("controls_td", mapOf(
                "font-size", "0px",
                "display", "none",
                "mso-hide", "all",
                "padding", "0px"
        ));
    }

    private StringBuilder generateRadios(HtmlRenderer renderer) {
        var res = new StringBuilder();
        carouselImages.stream().forEachOrdered(c -> res.append(c.renderRadio(renderer)));
        return res;
    }

    private StringBuilder generateThumbnails(HtmlRenderer renderer) {
        var res = new StringBuilder();
        if (!"visible".equals(getAttribute("thumbnails"))) {
            return res;
        }
        carouselImages.stream().forEachOrdered(c -> res.append(c.renderThumbnail(renderer)));
        return res;
    }

    private String getThumbnailsWidth() {
        if (carouselImages.isEmpty()) {
            return "0";
        }

        var tbWidth = getAttribute("tb-width");
        if (!Utils.isNullOrEmpty(tbWidth)) {
            return tbWidth;
        }
        var containerWidth = CssUnitParser.parse(floatToString(getContainerOuterWidth()));
        return floatToString(Math.min(containerWidth.value, 110));
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.openTag("div", htmlAttributes(mapOf("class", "mj-carousel")), res);
        res.append(generateRadios(renderer));
        renderer.openTag("div", htmlAttributes(mapOf(
                "class", "mj-carousel-content mj-carousel-" + carouselId + "-content",
                "style", "carousel_div"
        )), res);
        res.append(generateThumbnails(renderer));
        res.append(generateCarousel(renderer));

        renderer.closeTag("div", res);
        renderer.closeTag("div", res);

        return new StringBuilder(msoConditionalTag(res, true)).append("\n").append(renderFallback(renderer));
    }

    private StringBuilder generateCarousel(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.openTag("table", htmlAttributes(mapOf(
                "style", "carousel_table",
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "width", "100%",
                "role", "presentation",
                "class", "mj-carousel-main"
        )), res);
        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);
        res.append(generateControls("previous", getAttribute("left-icon"), renderer));
        res.append(generateImages(renderer));
        res.append(generateControls("next", getAttribute("right-icon"), renderer));
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        return res;
    }

    private StringBuilder generateImages(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.openTag("td", htmlAttributes(mapOf("style", "images_td")), res);
        renderer.openTag("div", htmlAttributes(mapOf("class", "mj-carousel-images")), res);
        res.append(renderChildren(renderer));
        renderer.closeTag("div", res);
        renderer.closeTag("td", res);
        return res;
    }

    private StringBuilder generateControls(String direction, String icon, HtmlRenderer renderer) {
        var res = new StringBuilder();
        var iconWidth = Utils.parseIntNumberPart(getAttribute("icon-width"));

        renderer.openTag("td", htmlAttributes(mapOf(
                "class", "mj-carousel-" + carouselId + "-icons-cell",
                "style", "controls_td"
        )), res);
        renderer.openTag("div", htmlAttributes(mapOf(
                "class", "mj-carousel-" + direction + "-icons",
                "style", "controls_div"
        )), res);

        for (var i = 1; i < carouselImagesCount + 1; i++) {
            renderer.openTag("label", htmlAttributes(mapOf(
                    "for", "mj-carousel-" + carouselId + "-radio-" + i,
                    "class", "mj-carousel-" + direction + " mj-carousel-" + direction + "-" + i
            )), res);
            renderer.openCloseTag("img", htmlAttributes(mapOf(
                    "src", icon,
                    "alt", direction,
                    "style", "controls_img",
                    "width", iconWidth
            )), res);
            renderer.closeTag("label", res);
        }

        renderer.closeTag("div", res);
        renderer.closeTag("td", res);
        return res;
    }

    private StringBuilder renderFallback(HtmlRenderer renderer) {
        var res = new StringBuilder();
        if (carouselImagesCount == 0) {
            return res;
        }
        return new StringBuilder(msoConditionalTag(carouselImages.get(0).renderMjml(renderer).toString()));
    }
}
