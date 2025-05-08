package ch.digitalfondue.mjml4j.selector;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.JFiveParse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleSelectorTest {

    Element element;

    @BeforeEach
    void setup() {
        element = JFiveParse.parse(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream("test-01.html"))))
                .getDocumentElement();
    }

    @Test
    void typeSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, "title");
        assertEquals(1, elements.size());
        String pageTitle = elements.get(0).getTextContent();
        assertEquals("Page Title", pageTitle);
    }

    @Test
    void classSelector() throws Exception {
        List<Element> elements = CssSelector.select(element, ".my-text");
        assertEquals(2, elements.size());
    }

    @Test
    void typeAndClassSelector() throws Exception {
        String html = "<html>" +
                "<head>" +
                "<title>Page Title</title>" +
                "</head>" +
                "<body>" +
                "<h1 id=\"id1\" class=\"top-title\" it=\"page.title\">Title</h1>" +
                "<p class=\"my-text\">Paragraph</p>" +
                "<p class=\"my-text-2\">Another Paragraph</p>" +
                "<span class=\"my-text\">this is <strong>my</strong> page!</span>" +
                "</body>" +
                "</html>";
        Element doc = JFiveParse.parse(html).getDocumentElement();
        List<Element> elements = CssSelector.select(doc, "p.my-text");
        assertEquals(1, elements.size());
        String paragraphText = elements.get(0).getTextContent();
        assertEquals("Paragraph", paragraphText);
    }

    @Test
    void universal() throws Exception {
        List<Element> elements = CssSelector.select(element, "*");

        assertEquals(10, elements.size());
        assertEquals("html", elements.get(0).getNodeName());
        assertEquals("head", elements.get(1).getNodeName());
        assertEquals("title", elements.get(2).getNodeName());
        assertEquals("body", elements.get(3).getNodeName());
        assertEquals("h1", elements.get(4).getNodeName());
        assertEquals("p", elements.get(5).getNodeName());
        assertEquals("p", elements.get(6).getNodeName());
        assertEquals("span", elements.get(7).getNodeName());
        assertEquals("strong", elements.get(8).getNodeName());
        assertEquals("p", elements.get(9).getNodeName());
    }
}
