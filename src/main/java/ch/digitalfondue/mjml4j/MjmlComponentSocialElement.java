package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

class MjmlComponentSocialElement extends BaseComponent.BodyComponent {

    private static final String IMAGE_BASE_URL = "https://www.mailjet.com/images/theme/v1/icons/ico-social/";

    private record SocialNetworkSettings(String shareUrl, String backgroundColor, String src) {
    }

    private static final LinkedHashMap<String, SocialNetworkSettings> DEFAULT_SOCIAL_NETWORKS = mapOf(
            entry("facebook", new SocialNetworkSettings("https://www.facebook.com/sharer/sharer.php?u=[[URL]]", "#3b5998", IMAGE_BASE_URL + "facebook.png")),
            entry("twitter", new SocialNetworkSettings("https://twitter.com/intent/tweet?url=[[URL]]", "#55acee", IMAGE_BASE_URL + "twitter.png")),
            entry("x", new SocialNetworkSettings("https://twitter.com/intent/tweet?url=[[URL]]", "#000000", IMAGE_BASE_URL + "twitter-x.png")),
            entry("google", new SocialNetworkSettings("https://plus.google.com/share?url=[[URL]]", "#dc4e41", IMAGE_BASE_URL + "google-plus.png")),
            entry("pinterest", new SocialNetworkSettings("https://pinterest.com/pin/create/button/?url=[[URL]]&media=&description=", "#bd081c", IMAGE_BASE_URL + "pinterest.png")),
            entry("linkedin", new SocialNetworkSettings("https://www.linkedin.com/shareArticle?mini=true&url=[[URL]]&title=&summary=&source=", "#0077b5", IMAGE_BASE_URL + "linkedin.png")),
            entry("instagram", new SocialNetworkSettings("", "#3f729b", IMAGE_BASE_URL + "instagram.png")),
            entry("web", new SocialNetworkSettings("", "#4BADE9", IMAGE_BASE_URL + "web.png")),
            entry("snapchat", new SocialNetworkSettings("", "#FFFA54", IMAGE_BASE_URL + "snapchat.png")),
            entry("youtube", new SocialNetworkSettings("", "#EB3323", IMAGE_BASE_URL + "youtube.png")),
            entry("tumblr", new SocialNetworkSettings("https://www.tumblr.com/widgets/share/tool?canonicalUrl=[[URL]]", "#344356", IMAGE_BASE_URL + "tumblr.png")),
            entry("github", new SocialNetworkSettings("", "#000000", IMAGE_BASE_URL + "github.png")),
            entry("xing", new SocialNetworkSettings("https://www.xing.com/app/user?op=share&url=[[URL]]", "#296366", IMAGE_BASE_URL + "xing.png")),
            entry("vimeo", new SocialNetworkSettings("", "#53B4E7", IMAGE_BASE_URL + "vimeo.png")),
            entry("medium", new SocialNetworkSettings("", "#000000", IMAGE_BASE_URL + "medium.png")),
            entry("soundcloud", new SocialNetworkSettings("", "#EF7F31", IMAGE_BASE_URL + "soundcloud.png")),
            entry("dribbble", new SocialNetworkSettings("", "#D95988", IMAGE_BASE_URL + "dribbble.png")),

            // no share
            entry("facebook-noshare", new SocialNetworkSettings("[[URL]]", "#3b5998", IMAGE_BASE_URL + "facebook.png")),
            entry("twitter-noshare", new SocialNetworkSettings("[[URL]]", "#55acee", IMAGE_BASE_URL + "twitter.png")),
            entry("x-noshare", new SocialNetworkSettings("[[URL]]", "#000000", IMAGE_BASE_URL + "twitter-x.png")),
            entry("google-noshare", new SocialNetworkSettings("[[URL]]", "#dc4e41", IMAGE_BASE_URL + "google-plus.png")),
            entry("pinterest-noshare", new SocialNetworkSettings("[[URL]]", "#bd081c", IMAGE_BASE_URL + "pinterest.png")),
            entry("linkedin-noshare", new SocialNetworkSettings("[[URL]]", "#0077b5", IMAGE_BASE_URL + "linkedin.png")),
            entry("instagram-noshare", new SocialNetworkSettings("[[URL]]", "#3f729b", IMAGE_BASE_URL + "instagram.png")),
            entry("web-noshare", new SocialNetworkSettings("[[URL]]", "#4BADE9", IMAGE_BASE_URL + "web.png")),
            entry("snapchat-noshare", new SocialNetworkSettings("[[URL]]", "#FFFA54", IMAGE_BASE_URL + "snapchat.png")),
            entry("youtube-noshare", new SocialNetworkSettings("[[URL]]", "#EB3323", IMAGE_BASE_URL + "youtube.png")),
            entry("tumblr-noshare", new SocialNetworkSettings("[[URL]]", "#344356", IMAGE_BASE_URL + "tumblr.png")),
            entry("github-noshare", new SocialNetworkSettings("[[URL]]", "#000000", IMAGE_BASE_URL + "github.png")),
            entry("xing-noshare", new SocialNetworkSettings("[[URL]]", "#296366", IMAGE_BASE_URL + "xing.png")),
            entry("vimeo-noshare", new SocialNetworkSettings("[[URL]]", "#53B4E7", IMAGE_BASE_URL + "vimeo.png")),
            entry("medium-noshare", new SocialNetworkSettings("[[URL]]", "#000000", IMAGE_BASE_URL + "medium.png")),
            entry("soundcloud-noshare", new SocialNetworkSettings("[[URL]]", "#EF7F31", IMAGE_BASE_URL + "soundcloud.png")),
            entry("dribbble-noshare", new SocialNetworkSettings("[[URL]]", "#D95988", IMAGE_BASE_URL + "dribbble.png"))
    );

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("align", of("left")),
            entry("background-color", of(null, AttributeType.COLOR)),
            entry("color", of("#000", AttributeType.COLOR)),
            entry("border-radius", of("3px")),
            entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
            entry("font-size", of("13px")),
            entry("font-style", of(null)),
            entry("font-weight", of(null)),
            entry("href", of(null)),
            entry("icon-size", of(null)),
            entry("icon-height", of(null)),
            entry("icon-padding", of(null)),
            entry("line-height", of("1")),
            entry("name", of(null)),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("padding", of("4px")),
            entry("text-padding", of("4px 4px 4px 0")),
            entry("rel", of(null)),
            entry("src", of(null)),
            entry("srcset", of(null)),
            entry("sizes", of(null)),
            entry("alt", of("")),
            entry("title", of(null)),
            entry("target", of("_blank")),
            entry("text-decoration", of("none")),
            entry("vertical-align", of("middle"))
    );

    private SocialAttributes socialAttributes;

    MjmlComponentSocialElement(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        socialAttributes = getSocialAttributes();

        cssStyleLibraries.add("td", mapOf(
                "padding", getAttribute("padding"),
                "padding-top", getAttribute("padding-top"),
                "padding-right", getAttribute("padding-right"),
                "padding-bottom", getAttribute("padding-bottom"),
                "padding-left", getAttribute("padding-left"),
                "vertical-align", getAttribute("vertical-align")
        ));

        cssStyleLibraries.add("table", mapOf(
                "background", socialAttributes.background,
                "border-radius", getAttribute("border-radius"),
                "width", socialAttributes.iconSize
        ));

        cssStyleLibraries.add("icon", mapOf(
                "padding", getAttribute("icon-padding"),
                "font-size", "0",
                "height", !Utils.isNullOrWhiteSpace(socialAttributes.iconHeight) ? socialAttributes.iconHeight : socialAttributes.iconSize,
                "vertical-align", "middle",
                "width", socialAttributes.iconSize
        ));

        cssStyleLibraries.add("img", mapOf(
                "border-radius", getAttribute("border-radius"),
                "display", "block"
        ));

        cssStyleLibraries.add("tdText", mapOf(
                "vertical-align", "middle",
                "padding", getAttribute("text-padding")
        ));

        cssStyleLibraries.add("text", mapOf(
                "color", getAttribute("color"),
                "font-size", getAttribute("font-size"),
                "font-weight", getAttribute("font-weight"),
                "font-style", getAttribute("font-style"),
                "font-family", getAttribute("font-family"),
                "line-height", getAttribute("line-height"),
                "text-decoration", getAttribute("text-decoration")
        ));
    }

    private record SocialAttributes(String href, String iconSize, String iconHeight,
                                    String srcset, String sizes, String src, String background) {
    }

    private SocialAttributes getSocialAttributes() {
        var socialNetworkName = getAttribute("name");


        if (!DEFAULT_SOCIAL_NETWORKS.containsKey(socialNetworkName))
            return new SocialAttributes(null, null, null, null, null, null, null);

        var socialNetwork = DEFAULT_SOCIAL_NETWORKS.get(socialNetworkName);


        var href = getAttribute("href");

        if (!Utils.isNullOrEmpty(href) && !Utils.isNullOrEmpty(socialNetwork.shareUrl)) {
            href = socialNetwork.shareUrl.replace("[[URL]]", href);
        }

        return new SocialAttributes(
                href,
                getAttribute("icon-size"),
                getAttribute("icon-size"),
                getAttribute("srcset"),
                getAttribute("sizes"),
                hasAttribute("src") ? getAttribute("src") : socialNetwork.src,
                hasAttribute("background-color") ? getAttribute("background-color") : socialNetwork.backgroundColor
        );
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var hasLink = hasAttribute("href");
        var res = new StringBuilder();
        renderer.openTag("tr", htmlAttributes(mapOf("class", getAttribute("css-class"))), res);
        renderer.openTag("td", htmlAttributes(mapOf("style", "td")), res);
        renderer.openTag("table", htmlAttributes(mapOf(
                "border", "0",
                "cellpadding", "0",
                "cellspacing", "0",
                "role", "presentation",
                "style", "table"
        )), res);
        renderer.openTag("tbody", res);
        renderer.openTag("tr", res);
        renderer.openTag("td", htmlAttributes(mapOf("style", "icon")), res);
        if (hasLink) {
            renderer.openTag("a", htmlAttributes(mapOf(
                    "href", socialAttributes.href,
                    "rel", getAttribute("rel"),
                    "target", getAttribute("target")
            )), res);
        }
        renderer.openCloseTag("img", htmlAttributes(mapOf(
                "alt", getAttribute("alt"),
                "title", getAttribute("title"),
                "height", Utils.parseIntNumberPart(!Utils.isNullOrWhiteSpace(socialAttributes.iconHeight) ? socialAttributes.iconHeight : socialAttributes.iconSize),
                "src", socialAttributes.src,
                "style", "img",
                "width", Utils.parseIntNumberPart(getAttribute("icon-size")),
                "sizes", getAttribute("sizes"),
                "srcset", getAttribute("srcset")
        )), res);

        if (hasLink) {
            renderer.closeTag("a", res);
        }
        renderer.closeTag("td", res);
        renderer.closeTag("tr", res);
        renderer.closeTag("tbody", res);
        renderer.closeTag("table", res);
        renderer.closeTag("td", res);

        if (Utils.hasNonEmptyChildNodes(getElement())) {
            res.append(renderContent(renderer, hasLink));
        }
        renderer.closeTag("tr", res);

        return res;
    }

    private StringBuilder renderContent(HtmlRenderer renderer, boolean hasLink) {
        var res = new StringBuilder();
        renderer.openTag("td", htmlAttributes(mapOf("style", "tdText")), res);

        if (hasLink) {
            renderer.openTag("a", htmlAttributes(mapOf(
                    "href", socialAttributes.href,
                    "style", "text",
                    "rel", getAttribute("rel"),
                    "target", getAttribute("target")
            )), res);
        } else {
            renderer.openTag("span", htmlAttributes(mapOf("style", "text")), res);
        }
        DOMSerializer.serializeInner(getElement(), res);
        renderer.closeTag(hasLink ? "a" : "span", res);
        renderer.closeTag("td", res);
        return res;
    }

    @Override
    boolean isEndingTag() {
        return true;
    }
}
