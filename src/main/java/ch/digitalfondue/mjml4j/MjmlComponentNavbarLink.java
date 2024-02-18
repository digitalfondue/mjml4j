package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.conditionalTag;
import static ch.digitalfondue.mjml4j.Utils.mapOf;
import static java.util.Map.entry;

class MjmlComponentNavbarLink extends BaseComponent.BodyComponent {

    MjmlComponentNavbarLink(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("color", of("#000000", AttributeType.COLOR)),
            entry("font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
            entry("font-size", of("13px")),
            entry("font-style", of(null)),
            entry("font-weight", of("normal")),
            entry("href", of(null)),
            entry("name", of(null)),
            entry("target", of("_blank")),
            entry("rel", of(null)),
            entry("letter-spacing", of(null)),
            entry("line-height", of("22px")),
            entry("padding-bottom", of(null)),
            entry("padding-left", of(null)),
            entry("padding-right", of(null)),
            entry("padding-top", of(null)),
            entry("padding", of("15px 10px")),
            entry("text-decoration", of("none")),
            entry("text-transform", of("uppercase"))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("a", mapOf(
                "display", "inline-block",
                "color", getAttribute("color"),
                "font-family", getAttribute("font-family"),
                "font-size", getAttribute("font-size"),
                "font-style", getAttribute("font-style"),
                "font-weight", getAttribute("font-weight"),
                "letter-spacing", getAttribute("letter-spacing"),
                "line-height", getAttribute("line-height"),
                "text-decoration", getAttribute("text-decoration"),
                "text-transform", getAttribute("text-transform"),
                "padding", getAttribute("padding"),
                "padding-top", getAttribute("padding-top"),
                "padding-left", getAttribute("padding-left"),
                "padding-right", getAttribute("padding-right"),
                "padding-bottom", getAttribute("padding-bottom")
        ));

        cssStyleLibraries.add("td", mapOf(
                "padding", getAttribute("padding"),
                "padding-top", getAttribute("padding-top"),
                "padding-left", getAttribute("padding-left"),
                "padding-right", getAttribute("padding-right"),
                "padding-bottom", getAttribute("padding-bottom")
        ));
    }

    private StringBuilder renderContent() {
        var res = new StringBuilder();
        var href = getAttribute("href");
        var navbarBaseUrl = getAttribute("navbarBaseUrl");
        var link = !Utils.isNullOrEmpty(navbarBaseUrl) ? navbarBaseUrl + href : href;
        var cssClass = hasAttribute("css-class") ? getAttribute("css-class") : "";

        res.append("<a ").append(htmlAttributes(mapOf(
                "class", "mj-link" + cssClass,
                "href", link,
                "rel", getAttribute("rel"),
                "target", getAttribute("target"),
                "name", getAttribute("name"),
                "style", "a"
        ))).append(">\n");
        DOMSerializer.serializeInner(getElement(), res);
        res.append("\n</a>\n");
        return res;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();
        res.append(conditionalTag("<td " + htmlAttributes(mapOf(
                "style", "td",
                "class", Utils.suffixCssClasses(getAttribute("css-class"), "outlook"))) +
                " >\n")
        );
        res.append(renderContent());
        res.append(conditionalTag("</td>\n"));
        return res;
    }

    @Override
    boolean isEndingTag() {
        return true;
    }
}
