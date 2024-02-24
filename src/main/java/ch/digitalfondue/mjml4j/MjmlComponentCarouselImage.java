package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.*;
import static java.util.Map.entry;

class MjmlComponentCarouselImage extends BaseComponent.BodyComponent {
    MjmlComponentCarouselImage(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    int index;
    String carouselId;

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("alt", of("")),
            entry("href", of(null)),
            entry("rel", of(null)),
            entry("target", of("_blank")),
            entry("title", of(null)),
            entry("src", of(null)),
            entry("thumbnails-src", of(null)),
            entry("border-radius", of(null)),
            entry("tb-border", of(null)),
            entry("tb-border-radius", of(null))
    );

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("images_img", mapOf(
                "border-radius", getAttribute("border-radius"),
                "display", "block",
                "width", floatToString(getContainerOuterWidth()) + "px",
                "max-width", "100%",
                "height", "auto"
        ));
        cssStyleLibraries.add("images_firstImageDiv", mapOf());
        cssStyleLibraries.add("images_otherImageDiv", mapOf(
                "display", "none",
                "mso-hide", "all"
        ));

        //
        cssStyleLibraries.add("radio_input", mapOf(
                "display", "none",
                "mso-hide", "all"
        ));

        cssStyleLibraries.add("thumbnails_a", mapOf(
                "border", getAttribute("tb-border"),
                "border-radius", getAttribute("tb-border-radius"),
                "display", "inline-block",
                "overflow", "hidden",
                "width", CssUnitParser.parse(getAttribute("tb-width")).toString()
        ));

        cssStyleLibraries.add("thumbnails_img", mapOf(
                "display", "block",
                "width", "100%",
                "height", "auto"
        ));
    }

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    boolean isEndingTag() {
        return true;
    }

    StringBuilder renderThumbnail(HtmlRenderer renderer) {
        var res = new StringBuilder();
        var imgIndex = index + 1;
        var cssClass = suffixCssClasses(getAttribute("css-class"), "thumbnail");

        renderer.openTag("a", htmlAttributes(mapOf(
                "style", "thumbnails_a",
                "href", "#" + imgIndex,
                "target", getAttribute("target"),
                "class", "mj-carousel-thumbnail mj-carousel-" + carouselId + "-thumbnail mj-carousel-" + carouselId + "-thumbnail-" + imgIndex + " " + cssClass
        )), res);
        renderer.openTag("label", htmlAttributes(mapOf(
                "for", "mj-carousel-" + carouselId + "-radio-" + imgIndex
        )), res);

        var src = getAttribute("thumbnails-src");
        if (Utils.isNullOrWhiteSpace(src)) {
            src = getAttribute("src");
        }
        renderer.openCloseTag("img", htmlAttributes(mapOf(
                "style", "thumbnails_img",
                "src", src,
                "alt", getAttribute("alt"),
                "width", floatToString(CssUnitParser.parse(getAttribute("tb-width")).value)
        )), res);

        renderer.closeTag("label", res);
        renderer.closeTag("a", res);

        return res;
    }


    StringBuilder renderRadio(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.openCloseTag("input", htmlAttributes(mapOf(
                "class", "mj-carousel-radio mj-carousel-" + carouselId + "-radio mj-carousel-" + carouselId + "-radio-" + (index + 1),
                "checked", index == 0 ? "checked" : null,
                "type", "radio",
                "name", "mj-carousel-radio-" + carouselId,
                "id", "mj-carousel-" + carouselId + "-radio-" + (index + 1),
                "style", "radio_input"
        )), res);
        return res;
    }


    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();

        var cssClass = getAttribute("css-class");
        var href = getAttribute("href");

        Runnable renderImage = () -> renderer.openCloseTag("img", htmlAttributes(mapOf(
                "title", getAttribute("title"),
                "src", getAttribute("src"),
                "alt", getAttribute("alt"),
                "style", "images_img",
                "width", floatToString(getContainerOuterWidth()), // this.context.containerWidth
                "border", "0"
        )), res);

        renderer.openTag("div", htmlAttributes(mapOf(
                "class", "mj-carousel-image mj-carousel-image-" + (index + 1) + " " + (cssClass != null ? cssClass : ""),
                "style", index == 0 ? "images_firstImageDiv" : "images_otherImageDiv"
        )), res);

        if (href != null) {
            var rel = this.getAttribute("rel");
            renderer.openTag("a", htmlAttributes(mapOf("href", href, "rel", rel, "target", "_blank")), res, false);
            renderImage.run();
            renderer.closeTag("a", res);
        } else {
            renderImage.run();
        }

        renderer.closeTag("div", res);
        return res;
    }
}
