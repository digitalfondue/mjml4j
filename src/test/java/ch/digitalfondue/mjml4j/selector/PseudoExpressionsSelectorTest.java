package ch.digitalfondue.mjml4j.selector;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.JFiveParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PseudoExpressionsSelectorTest {

    Element element;

    @BeforeEach
    void setup() {
        element = JFiveParse.parse(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("test-04.html"))))
                .getDocumentElement();
    }

    @Test
    void nthChildSelectorOdd() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul3 li:nth-child(2n + 1)");
        assertEquals(3, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-3", elements.get(1).getAttribute("id"));
        assertEquals("li3-5", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(odd)");
        assertEquals(3, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-3", elements.get(1).getAttribute("id"));
        assertEquals("li3-5", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child('odd')");
        assertEquals(3, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-3", elements.get(1).getAttribute("id"));
        assertEquals("li3-5", elements.get(2).getAttribute("id"));
    }

    @Test
    void nthChildSelectorEven() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul3 li:nth-child(2n+0)");
        assertEquals(3, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));
        assertEquals("li3-6", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(2n)");
        assertEquals(3, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));
        assertEquals("li3-6", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(even)");
        assertEquals(3, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));
        assertEquals("li3-6", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child('even')");
        assertEquals(3, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));
        assertEquals("li3-6", elements.get(2).getAttribute("id"));
    }

    @Test
    void nthChildSelectorNumber() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul1 li:nth-child(1)");
        assertEquals(1, elements.size());
        assertEquals("li1-1", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "#ul1 li:nth-child(2)");
        assertEquals(1, elements.size());
        assertEquals("li1-2", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "#ul1 li:nth-child(3)");
        assertEquals(1, elements.size());
        assertEquals("li1-3", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "#ul1 li:nth-child(4)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "#ul1 li:nth-child(0)");
        assertEquals(0, elements.size());
    }

    @Test
    void nthChildSelectorVariations() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul3 li:nth-child(3n+0)");
        assertEquals(2, elements.size());
        assertEquals("li3-3", elements.get(0).getAttribute("id"));
        assertEquals("li3-6", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(3n+1)");
        assertEquals(2, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(3n+2)");
        assertEquals(2, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-5", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(3n+3)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "#ul3 li:nth-child(0n+0)");
        assertEquals(0, elements.size());
    }

    @Test
    void nthChildSelectorVariationsBNegative() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul3 li:nth-child(3n-0)");
        assertEquals(2, elements.size());
        assertEquals("li3-3", elements.get(0).getAttribute("id"));
        assertEquals("li3-6", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(3n-2)");
        assertEquals(2, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(3n-1)");
        assertEquals(2, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-5", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(3n-3)");
        assertEquals(2, elements.size());
        assertEquals("li3-3", elements.get(0).getAttribute("id"));
        assertEquals("li3-6", elements.get(1).getAttribute("id"));
    }

    @Test
    void nthChildSelectorVariationsANegative() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul3 li:nth-child(-3n-0)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "#ul3 li:nth-child(-3n+0)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "#ul3 li:nth-child(-3n-1)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "#ul3 li:nth-child(-3n+1)");
        assertEquals(1, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(-3n+3)");
        assertEquals(3, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-2", elements.get(1).getAttribute("id"));
        assertEquals("li3-3", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(-3n+5)");
        assertEquals(5, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-5", elements.get(4).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-child(-3n+7)");
        assertEquals(6, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-6", elements.get(5).getAttribute("id"));

    }

    @Test
    void otherSelectorVariations() throws Exception {
        List<Element> elements = CssSelector.select(element, "#ul3 li:nth-last-child(-3n+2)");
        assertEquals(2, elements.size());
        assertEquals("li3-5", elements.get(0).getAttribute("id"));
        assertEquals("li3-6", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-last-child(2n+1)");
        assertEquals(3, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));
        assertEquals("li3-6", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-last-child(odd)");
        assertEquals(3, elements.size());
        assertEquals("li3-2", elements.get(0).getAttribute("id"));
        assertEquals("li3-4", elements.get(1).getAttribute("id"));
        assertEquals("li3-6", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "#ul3 li:nth-last-child(even)");
        assertEquals(3, elements.size());
        assertEquals("li3-1", elements.get(0).getAttribute("id"));
        assertEquals("li3-3", elements.get(1).getAttribute("id"));
        assertEquals("li3-5", elements.get(2).getAttribute("id"));

        elements = CssSelector.select(element, "body > ul:nth-of-type(even)");
        assertEquals(1, elements.size());
        assertEquals("ul2", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "body > ul:nth-of-type(odd)");
        assertEquals(2, elements.size());
        assertEquals("ul1", elements.get(0).getAttribute("id"));
        assertEquals("ul3", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "body > ul:nth-of-type(2)");
        assertEquals(1, elements.size());
        assertEquals("ul2", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "body > ul:nth-last-of-type(2)");
        assertEquals(1, elements.size());
        assertEquals("ul2", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "body > ul:nth-last-of-type(1)");
        assertEquals(1, elements.size());
        assertEquals("ul3", elements.get(0).getAttribute("id"));
    }

}
