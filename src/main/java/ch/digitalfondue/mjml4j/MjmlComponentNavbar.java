package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.*;
import static java.util.Map.entry;

class MjmlComponentNavbar extends BaseComponent.BodyComponent {

    MjmlComponentNavbar(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }


    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            entry("align", of("center")),
            entry("base-url", of(null)),
            entry("hamburger", of(null)),
            entry("ico-align", of("center")),
            entry("ico-open", of("&#9776;")),
            entry("ico-close", of("&#8855;")),
            entry("ico-color", of("#000000", AttributeType.COLOR)),
            entry("ico-font-size", of("30px")),
            entry("ico-font-family", of("Ubuntu, Helvetica, Arial, sans-serif")),
            entry("ico-text-transform", of("uppercase")),
            entry("ico-padding", of("10px")),
            entry("ico-padding-left", of(null)),
            entry("ico-padding-top", of(null)),
            entry("ico-padding-right", of(null)),
            entry("ico-padding-bottom", of(null)),
            entry("padding", of(null)),
            entry("padding-left", of(null)),
            entry("padding-top", of(null)),
            entry("padding-right", of(null)),
            entry("padding-bottom", of(null)),
            entry("ico-text-decoration", of("none")),
            entry("ico-line-height", of("30px"))
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    String headStyle() {
        return "noinput.mj-menu-checkbox { display:block!important; max-height:none!important; visibility:visible!important; }\n" +
                "@media only screen and (max-width:" + Utils.makeLowerBreakpoint(context.breakpoint) + ") {\n" +
                ".mj-menu-checkbox[type=\"checkbox\"] ~ .mj-inline-links { display:none!important; }\n" +
                ".mj-menu-checkbox[type=\"checkbox\"]:checked ~ .mj-inline-links,\n" +
                ".mj-menu-checkbox[type=\"checkbox\"] ~ .mj-menu-trigger { display:block!important; max-width:none!important; max-height:none!important; font-size:inherit!important; }\n" +
                ".mj-menu-checkbox[type=\"checkbox\"] ~ .mj-inline-links > a { display:block!important; }\n" +
                ".mj-menu-checkbox[type=\"checkbox\"]:checked ~ .mj-menu-trigger .mj-menu-icon-close { display:block!important; }\n" +
                ".mj-menu-checkbox[type=\"checkbox\"]:checked ~ .mj-menu-trigger .mj-menu-icon-open { display:none!important; }\n" +
                "}\n";
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add("div", mapOf(
                "align", getAttribute("align"),
                "width", "100%"
        ));

        cssStyleLibraries.add("label", mapOf(
                "display", "block",
                "cursor", "pointer",
                "mso-hide", "all",
                "-moz-user-select", "none",
                "user-select", "none",
                "color", getAttribute("ico-color"),
                "font-size", getAttribute("ico-font-size"),
                "font-family", getAttribute("ico-font-family"),
                "text-transform", getAttribute("ico-text-transform"),
                "text-decoration", getAttribute("ico-text-decoration"),
                "line-height", getAttribute("ico-line-height"),
                "padding", getAttribute("ico-padding"),
                "padding-top", getAttribute("ico-padding-top"),
                "padding-right", getAttribute("ico-padding-right"),
                "padding-bottom", getAttribute("ico-padding-bottom"),
                "padding-left", getAttribute("ico-padding-left")
        ));

        cssStyleLibraries.add("trigger", mapOf(
                "display", "none",
                "max-height", "0px",
                "max-width", "0px",
                "font-size", "0px",
                "overflow", "hidden"
        ));

        cssStyleLibraries.add("icoOpen", mapOf(
                "mso-hide", "all"
        ));

        cssStyleLibraries.add("icoClose", mapOf(
                "display", "none",
                "mso-hide", "all"
        ));
    }

    private StringBuilder renderHamburger() {

        var key = Utils.genRandomHexString();
        var res = new StringBuilder();

        res.append(msoConditionalTag("<input type=\"checkbox\" id=\"" + key + "\" class=\"mj-menu-checkbox\" style=\"display:none !important; max-height:0; visibility:hidden;\" />\n", true));
        res.append("<div ").append(htmlAttributes(mapOf("class", "mj-menu-trigger", "style", "trigger"))).append(">\n");
        res.append("<label ").append(htmlAttributes(mapOf(
                "for", key,
                "class", "mj-menu-label",
                "style", "label",
                "align", getAttribute("ico-align")))).append(">\n");
        res.append("<span ").append(htmlAttributes(mapOf("class", "mj-menu-icon-open", "style", "icoOpen"))).append(">\n");
        res.append(getAttribute("ico-open")).append("\n");
        res.append("</span>\n");
        res.append("<span ").append(htmlAttributes(mapOf("class", "mj-menu-icon-close", "style", "icoClose"))).append(">\n");
        res.append(getAttribute("ico-close")).append("\n");
        res.append("</span>\n");
        res.append("</label>\n");
        res.append("</div>\n");
        return res;
    }

    @Override
    StringBuilder renderChildren(HtmlRenderer renderer) {
        var sb = new StringBuilder();

        for (var childComponent : getChildren()) {
            var childContent = childComponent.renderMjml(renderer);
            if (Utils.isNullOrWhiteSpace(childContent))
                continue;
            sb.append(childContent);
        }
        return sb;
    }

    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();

        res.append(Utils.equalsIgnoreCase(getAttribute("hamburger"), "hamburger") ? renderHamburger() : "");
        res.append("\n");
        res.append("<div ").append(htmlAttributes(mapOf(
                "class", "mj-inline-links",
                "style", ""))).append(">\n"); // TODO: check as most likely mjml is doing something sus:
        res.append(conditionalTag("<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"" + getAttribute("align") + "\">\n" +
                "<tr>\n"
        ));
        res.append(renderChildren(renderer));
        res.append(conditionalTag("""
                </tr>
                </table>
                """));
        res.append("</div>");
        return res;
    }

    @Override
    String getInheritingAttribute(String attributeName) {
        return "navbarBaseUrl".equals(attributeName) ? getAttribute("base-url") : null;
    }
}
