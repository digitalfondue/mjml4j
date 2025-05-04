package ch.digitalfondue.mjml4j.selector;

import br.com.starcode.parccser.ParserException;
import ch.digitalfondue.jfiveparse.Document;
import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.mjml4j.selector.matcher.MatcherRegistry;
import ch.digitalfondue.mjml4j.selector.matcher.SelectorMatcher;

import java.util.List;

public class CssSelector {
    private CssSelector() {
        // Prevent instantiation of utility class
    }

    /**
     * Selects elements from the given document based on the provided CSS selector.
     *
     * @param document The document to start the selection from.
     * @param selector The CSS selector to use for selecting elements.
     * @return A list of elements that match the selector.
     * @throws ParserException If an error occurs during parsing or selection.
     */
    public static List<Element> select(Document document, String selector) throws ParserException {
        return select(document.getDocumentElement(), selector);
    }

    /**
     * Selects elements from the given element based on the provided CSS selector.
     *
     * @param element  The root element to start the selection from.
     * @param selector The CSS selector to use for selecting elements.
     * @return A list of elements that match the selector.
     * @throws ParserException If an error occurs during parsing or selection.
     */
    public static List<Element> select(Element element, String selector) throws ParserException {
        MatcherRegistry matcherRegistry = new MatcherRegistry();
        SelectorMatcher selectorMatcher = new SelectorMatcher(matcherRegistry);

        return selectorMatcher.applySelector(element, selector);
    }
}
