package ch.digitalfondue.mjml4j.selector;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.JFiveParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PseudoFormSelectorTest {

    Element element;

    @BeforeEach
    void setup() {
        element = JFiveParse.parse(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("test-03.html"))))
                .getDocumentElement();
    }

    @Test
    void enabled() throws Exception {
        List<Element> elements = CssSelector.select(element, ":enabled");
        assertEquals(8, elements.size());
        assertEquals("input-1", elements.get(0).getAttribute("id"));
        assertEquals("input-2", elements.get(1).getAttribute("id"));
        assertEquals("input-3", elements.get(2).getAttribute("id"));
        assertEquals("input-5", elements.get(3).getAttribute("id"));
        assertEquals("input-7", elements.get(4).getAttribute("id"));
        assertEquals("input-8", elements.get(5).getAttribute("id"));
        assertEquals("input-11", elements.get(6).getAttribute("id"));
        assertEquals("input-13", elements.get(7).getAttribute("id"));
    }

    @Test
    void disabled() throws Exception {
        List<Element> elements = CssSelector.select(element, ":disabled");
        assertEquals(5, elements.size());
        assertEquals("input-4", elements.get(0).getAttribute("id"));
        assertEquals("input-6", elements.get(1).getAttribute("id"));
        assertEquals("input-9", elements.get(2).getAttribute("id"));
        assertEquals("input-10", elements.get(3).getAttribute("id"));
        assertEquals("input-12", elements.get(4).getAttribute("id"));
    }

    @Test
    void checked() throws Exception {
        List<Element> elements = CssSelector.select(element, ":checked");
        assertEquals(2, elements.size());
        assertEquals("input-1", elements.get(0).getAttribute("id"));
        assertEquals("input-8", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "input[type='checkbox']:not(:checked),"
                + "input[type='radio']:not(:checked),"
                + "option:not(:checked)");
        assertEquals(2, elements.size());
        assertEquals("input-2", elements.get(0).getAttribute("id"));
        assertEquals("input-9", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "option:checked");
        assertEquals(1, elements.size());

    }

    @Test
    void none() throws Exception {
        List<Element> elements = CssSelector.select(element, ":checked2");
        assertEquals(0, elements.size());
    }
}
