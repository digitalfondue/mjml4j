package ch.digitalfondue.mjml4j;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CssParserTest {

    @Test
    void parseCssWithSingleSelectorAndStyle() {
        String css = "body { color: red; }";
        Map<String, String> result = CssParser.parseCss(css);
        assertEquals(1, result.size());
        assertEquals("color: red;", result.get("body"));
    }

    @Test
    void parseCssWithMultipleSelectorsAndStyles() {
        String css = "h1 { font-size: 20px; } p { margin: 10px; }";
        Map<String, String> result = CssParser.parseCss(css);
        assertEquals(2, result.size());
        assertEquals("font-size: 20px;", result.get("h1"));
        assertEquals("margin: 10px;", result.get("p"));
    }

    @Test
    void parseCssWithEmptyInput() {
        String css = "";
        Map<String, String> result = CssParser.parseCss(css);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseCssWithWhitespaceOnly() {
        String css = "   ";
        Map<String, String> result = CssParser.parseCss(css);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseCssWithInvalidFormat() {
        String css = "invalid-css";
        Map<String, String> result = CssParser.parseCss(css);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseCssWithNestedBraces() {
        String css = "div { color: red; { font-size: 12px; } }";
        Map<String, String> result = CssParser.parseCss(css);
        assertEquals(1, result.size());
        assertEquals("color: red; { font-size: 12px;", result.get("div"));
    }

    @Test
    void parseCssWithMultipleLines() {
        String css = "h1 {\n  font-size: 20px;\n}\np {\n  margin: 10px;\n}";
        Map<String, String> result = CssParser.parseCss(css);
        assertEquals(2, result.size());
        assertEquals("font-size: 20px;", result.get("h1"));
        assertEquals("margin: 10px;", result.get("p"));
    }

    @Test
    void parseCssWithDuplicateSelectors() {
        String css = "h1 { color: red; } h1 { font-size: 20px; }";
        Map<String, String> result = CssParser.parseCss(css);
        assertEquals(1, result.size());
        assertEquals("font-size: 20px;", result.get("h1"));
    }
}
