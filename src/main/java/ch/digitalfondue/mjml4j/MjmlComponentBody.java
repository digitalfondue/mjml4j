package ch.digitalfondue.mjml4j;

import ch.digitalfondue.mjml4j.AttributeValueType.AttributeType;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;

import static ch.digitalfondue.mjml4j.AttributeValueType.of;
import static ch.digitalfondue.mjml4j.Utils.mapOf;

class MjmlComponentBody extends BaseComponent.BodyComponent {
    MjmlComponentBody(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    @Override
    void setupPostConstruction() {
        super.setupPostConstruction();
        if (hasAttribute("width"))
            context.containerWidth = getAttribute("width");

        if (hasAttribute("background-color"))
            context.backgroundColor = getAttribute("background-color");
    }

    private static final LinkedHashMap<String, AttributeValueType> ALLOWED_DEFAULT_ATTRIBUTES = mapOf(
            "width", of("600px", AttributeType.PIXELS),
            "background-color", of(null, AttributeType.COLOR)
    );

    @Override
    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        return ALLOWED_DEFAULT_ATTRIBUTES;
    }

    @Override
    void setupStyles(CssStyleLibraries cssStyleLibraries) {
        cssStyleLibraries.add(
                "div",
                mapOf("background-color", getAttribute("background-color"))
        );
    }


    @Override
    StringBuilder renderMjml(HtmlRenderer renderer) {
        var res = new StringBuilder();
        renderer.openTag("div", htmlAttributes(mapOf(
                "class", getAttribute("class"),
                "style", "div",
                "lang", context.language,
                "dir", context.dir
        )), res);
        res.append(renderChildren(renderer));
        renderer.closeTag("div", res);
        return res;
    }
}
