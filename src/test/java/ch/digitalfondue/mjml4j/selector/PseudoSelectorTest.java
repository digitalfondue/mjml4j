package ch.digitalfondue.mjml4j.selector;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.JFiveParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PseudoSelectorTest {

    Element element;

    @BeforeEach
    void setup() {
        element = JFiveParse.parse(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("test-02.html"))))
                .getDocumentElement();
    }

    @Test
    void rootSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, ":root");
        assertEquals(1, elements.size());
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void nthChildSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "body h1:nth-child(1)");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body > *");
        assertEquals(7, elements.size());

        elements = CssSelector.select(element, "body > *:nth-child(7)");
        assertEquals(1, elements.size());
        assertEquals("button", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "html:nth-child(1)");
        assertEquals(1, elements.size());
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void nthChildSelectorNoResults() throws Exception {
        List<Element> elements = CssSelector.select(element, "body:nth-child(0)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-child()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-child(-1)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-child(6)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-child()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-child(2)");
        assertEquals(0, elements.size());
    }

    @Test
    void nthLastChildSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "body > *:nth-last-child(1)");
        assertEquals(1, elements.size());
        assertEquals("button", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body > *:nth-last-child(7)");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "html:nth-last-child(1)");
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void nthLastChildSelectorNoResults() throws Exception {
        List<Element> elements = CssSelector.select(element, "body:nth-last-child(0)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-last-child()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-last-child(-1)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-last-child(6)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-last-child()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-last-child(2)");
        assertEquals(0, elements.size());
    }

    @Test
    void nthTypeSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "body > p:nth-of-type(1)");
        assertEquals(1, elements.size());
        assertEquals("my-text", elements.get(0).getAttribute("class"));

        elements = CssSelector.select(element, "body p:nth-of-type(2)");
        assertEquals("my-text-2", elements.get(0).getAttribute("class"));

        elements = CssSelector.select(element, "html:nth-of-type(1)");
        assertEquals("html", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body:nth-of-type()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-of-type(2)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-of-type()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-of-type(2)");
        assertEquals(0, elements.size());
    }

    @Test
    void nthLastTypeSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "body > p:nth-last-of-type(1)");
        assertEquals(1, elements.size());
        assertEquals("my-text-2", elements.get(0).getAttribute("class"));

        elements = CssSelector.select(element, "body p:nth-last-of-type(2)");
        assertEquals("my-text", elements.get(0).getAttribute("class"));

        elements = CssSelector.select(element, "html:nth-last-of-type(1)");
        assertEquals("html", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body:nth-last-of-type()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "body:nth-last-of-type(2)");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-last-of-type()");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:nth-last-of-type(2)");
        assertEquals(0, elements.size());
    }

    @Test
    void firstChild() throws Exception {
        List<Element> elements = CssSelector.select(element, "body h1:first-child");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "button p:first-child");
        assertEquals(1, elements.size());
        assertEquals("p", elements.get(0).getNodeName());
        assertNull(elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "html:first-child");
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void lastChild() throws Exception {
        List<Element> elements = CssSelector.select(element, "body button:last-child");
        assertEquals(1, elements.size());
        assertEquals("button", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "button :last-child");
        assertEquals(1, elements.size());
        assertEquals("p", elements.get(0).getNodeName());
        assertEquals("p2", elements.get(0).getAttribute("id"));

        elements = CssSelector.select(element, "html:last-child");
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void firstType() throws Exception {
        List<Element> elements = CssSelector.select(element, "body h1:first-of-type");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body > p:first-of-type");
        assertEquals(1, elements.size());
        assertEquals("p", elements.get(0).getNodeName());
        assertEquals("my-text", elements.get(0).getAttribute("class"));

        elements = CssSelector.select(element, "html:first-of-type");
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void lastType() throws Exception {
        List<Element> elements = CssSelector.select(element, "body button:last-of-type");
        assertEquals(1, elements.size());
        assertEquals("button", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body > p:last-of-type");
        assertEquals(1, elements.size());
        assertEquals("p", elements.get(0).getNodeName());
        assertEquals("my-text-2", elements.get(0).getAttribute("class"));

        elements = CssSelector.select(element, "body p:last-of-type");
        assertEquals(2, elements.size());
        assertEquals("p", elements.get(0).getNodeName());
        assertEquals("p2", elements.get(1).getAttribute("id"));

        elements = CssSelector.select(element, "html:last-of-type");
        assertEquals("html", elements.get(0).getNodeName());
    }

    @Test
    void onlyChild() throws Exception {
        List<Element> elements = CssSelector.select(element, "title:only-child");
        assertEquals(1, elements.size());
        assertEquals("title", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "button p:only-child");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:only-child");
        assertEquals(1, elements.size());
    }

    @Test
    void onlyType() throws Exception {
        List<Element> elements = CssSelector.select(element, "title:only-of-type");
        assertEquals(1, elements.size());
        assertEquals("title", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "body > button:only-of-type");
        assertEquals(1, elements.size());
        assertEquals("button", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "button p:only-of-type");
        assertEquals(0, elements.size());

        elements = CssSelector.select(element, "html:only-of-type");
        assertEquals(1, elements.size());
    }

    @Test
    void empty() throws Exception {
        List<Element> elements = CssSelector.select(element, ":empty");
        assertEquals(3, elements.size());
        assertEquals("input", elements.get(0).getNodeName());
        assertEquals("div", elements.get(1).getNodeName());
        assertEquals("img", elements.get(2).getNodeName());
    }

}
