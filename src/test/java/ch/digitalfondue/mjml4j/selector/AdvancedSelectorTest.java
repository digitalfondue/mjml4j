package ch.digitalfondue.mjml4j.selector;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.JFiveParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdvancedSelectorTest {

    Element element;

    @BeforeEach
    void setup() {
        element = JFiveParse.parse(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("test-01.html"))))
                .getDocumentElement();
    }

    @Test
    void doubleSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "#id1, .my-text");
        assertEquals(3, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());
        assertEquals("p", elements.get(1).getNodeName());
        assertEquals("span", elements.get(2).getNodeName());
    }

    @Test
    void triple() throws Exception {
        List<Element> elements = CssSelector.select(element, "#id1,.my-text,.my-text-2");
        assertEquals(4, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());
        assertEquals("p", elements.get(1).getNodeName());
        assertEquals("span", elements.get(2).getNodeName());
        assertEquals("p", elements.get(3).getNodeName());
    }

    @Test
    void noResults() throws Exception {
        List<Element> elements = CssSelector.select(element, "#id2");
        //no elements
        assertEquals(0, elements.size());
    }

    @Test
    void onlyOneResult() throws Exception {
        List<Element> elements = CssSelector.select(element, "#id1, .my-text-5");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());
    }

    @Test
    void attributeSelector() throws Exception {

        List<Element> elements = CssSelector.select(element, "[style=\"\"]");
        assertEquals(1, elements.size());
        assertEquals("span", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[it='page.title']");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[it^='page']");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[it$='title']");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[it*='ge.ti']");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[class~='otherClass']");
        assertEquals(1, elements.size());
        assertEquals("span", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[class~='my-text']");
        assertEquals(2, elements.size());
        assertEquals("p", elements.get(0).getNodeName());
        assertEquals("span", elements.get(1).getNodeName());

        elements = CssSelector.select(element, "[class|='top']");
        assertEquals(1, elements.size());
        assertEquals("h1", elements.get(0).getNodeName());

        elements = CssSelector.select(element, "[class]");
        assertEquals(4, elements.size());

    }

    @Test
    void descendantCombinator() throws Exception {
        List<Element> elements = CssSelector.select(element, "span strong");
        assertEquals(1, elements.size());
        assertEquals("strong", elements.get(0).getNodeName());
    }

    @Test
    void adjacentSiblingCombinator() throws Exception {
        List<Element> elements = CssSelector.select(element, "body p + span");
        assertEquals(1, elements.size());
        assertEquals("span", elements.get(0).getNodeName());
        assertEquals("my-text otherClass", elements.get(0).getAttribute("class"));
    }

    @Test
    void adjacentSiblingCombinatorEmptyResult() throws Exception {
        List<Element> elements = CssSelector.select(element, "body span+ p");
        assertEquals(0, elements.size());
    }

    @Test
    void childCombinator() throws Exception {
        List<Element> elements = CssSelector.select(element, "body >p");
        assertEquals(2, elements.size());
    }

    @Test
    void generalSiblingCombinator() throws Exception {
        List<Element> elements = CssSelector.select(element, "body h1~p");
        assertEquals(2, elements.size());
    }

}
