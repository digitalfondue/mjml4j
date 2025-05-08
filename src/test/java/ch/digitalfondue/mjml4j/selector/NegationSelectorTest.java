package ch.digitalfondue.mjml4j.selector;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.JFiveParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NegationSelectorTest {

    Element element;

    @BeforeEach
    void setup() {
        element = JFiveParse.parse(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("test-01.html"))))
                .getDocumentElement();
    }

    @Test
    void notTypeSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, ":not(title)");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "title").get(0)));
    }

    @Test
    void notClassSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "p:not(.my-text)");
        assertEquals(2, elements.size());
        assertEquals("my-text-2", elements.get(0).getAttribute("class"));
        assertNotEquals("my-text", elements.get(1).getAttribute("class"));
    }

    @Test
    void notAttributeSelector() throws Exception {

        List<Element> elements = CssSelector.select(element, ":not([style=\"\"])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[style=\"\"]").get(0)));

        elements = CssSelector.select(element, ":not([it='page.title'])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[it='page.title']").get(0)));

        elements = CssSelector.select(element, ":not([it^='page'])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[it^='page']").get(0)));

        elements = CssSelector.select(element, ":not([it$='title'])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[it$='title']").get(0)));

        elements = CssSelector.select(element, ":not([it*='ge.ti'])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[it*='ge.ti']").get(0)));

        elements = CssSelector.select(element, ":not([class~='otherClass'])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[class~='otherClass']").get(0)));

        elements = CssSelector.select(element, ":not([class~='my-text'])");
        assertEquals(8, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[class~='my-text']").get(0)));
        assertFalse(elements.contains(CssSelector.select(element, "[class~='my-text']").get(1)));

        elements = CssSelector.select(element, ":not([class|='top'])");
        assertEquals(9, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[class|='top']").get(0)));

        elements = CssSelector.select(element, ":not([class])");
        assertEquals(6, elements.size());
        assertFalse(elements.contains(CssSelector.select(element, "[class]").get(0)));
        assertFalse(elements.contains(CssSelector.select(element, "[class]").get(1)));
        assertFalse(elements.contains(CssSelector.select(element, "[class]").get(2)));
        assertFalse(elements.contains(CssSelector.select(element, "[class]").get(3)));
    }

}
