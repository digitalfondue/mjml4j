package ch.digitalfondue.mjml4j;

import ch.digitalfondue.jfiveparse.Document;
import ch.digitalfondue.jfiveparse.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CssStyleInjectorTest {

    Document document;
    Element element;

    @BeforeEach
    void setUp() {
        document = new Document();
        Element root = new Element("html");
        document.appendChild(root);
        Element body = new Element("body");
        root.appendChild(body);
        element = new Element("div");
        body.appendChild(element);
    }

    @Test
    void applyInlineStylesWithValidSelectorAndStyle() {
        Map<String, String> selectorStyles = Map.of("div", "color: red;");
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertEquals("color: red;", element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesWithEmptySelectorStyles() {
        Map<String, String> selectorStyles = Map.of();
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertNull(element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesWithNullSelectorStyles() {
        CssStyleInjector.applyInlineStyles(document, null);

        assertNull(element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesWithInvalidSelector() {
        Map<String, String> selectorStyles = Map.of("invalid", "color: red;");
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertNull(element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesMergesExistingAndNewStyles() {
        Map<String, String> selectorStyles = Map.of("div", "color: red;");
        element.setAttribute("style", "background: blue;");
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertEquals("background: blue; color: red;", element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesOverwritesWithImportantStyles() {
        Map<String, String> selectorStyles = Map.of("div", "color: red !important;");
        element.setAttribute("style", "color: blue;");
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertEquals("color: red;", element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesKeepExistingWhenNotImportantStyles() {
        Map<String, String> selectorStyles = Map.of("div", "color: red;");
        element.setAttribute("style", "color: blue;");
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertEquals("color: blue;", element.getAttribute("style"));
    }

    @Test
    void applyInlineStylesIgnoresEmptyStyleValues() {
        Map<String, String> selectorStyles = Map.of("div", "");
        CssStyleInjector.applyInlineStyles(document, selectorStyles);

        assertNull(element.getAttribute("style"));
    }
}
