package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.Parser;
import br.com.starcode.parccser.ParserException;
import br.com.starcode.parccser.model.Combinator;
import br.com.starcode.parccser.model.Selector;
import br.com.starcode.parccser.model.SimpleSelector;
import br.com.starcode.parccser.model.SimpleSelectorSequence;
import ch.digitalfondue.jfiveparse.Element;

import java.util.ArrayList;
import java.util.List;

import static ch.digitalfondue.mjml4j.selector.matcher.ElementUtils.getAllElements;
import static ch.digitalfondue.mjml4j.selector.matcher.ElementUtils.getChildElements;
import static ch.digitalfondue.mjml4j.selector.matcher.ElementUtils.getParentElement;

public class SelectorMatcher {

    private MatcherRegistry matcherRegistry;

    public SelectorMatcher(MatcherRegistry matcherRegistry) {
        if (matcherRegistry == null) {
            throw new IllegalArgumentException("Matcher cannot be null!");
        }
        this.matcherRegistry = matcherRegistry;
    }

    public List<Element> applySelector(Element e, String selectorStr) throws ParserException {
        List<Selector> selectors = Parser.parse(selectorStr);
        List<Element> elements = new ArrayList<>();
        for (Selector selector : selectors) {
            elements.addAll(matchSelector(e, selector));
        }
        return elements;
    }

    protected List<Element> matchSelector(Element e, Selector selector) {
        int i = 0;
        List<Element> elements = new ArrayList<>();
        for (SimpleSelectorSequence simpleSelectorSequence : selector.getSelectors()) {
            if (i == 0) {
                elements = matchSimpleSelectorSequence(e, simpleSelectorSequence);
            } else {
                Combinator combinator = selector.getCombinators().get(i - 1);
                elements = matchSimpleSelectorSequence(e, simpleSelectorSequence, combinator, elements);
            }
            i++;
        }
        return elements;
    }

    protected List<Element> matchSimpleSelectorSequence(
            Element e,
            SimpleSelectorSequence simpleSelectorSequence) {
        List<SimpleSelector> simpleSelectors = simpleSelectorSequence.getSimpleSelectors();
        return walkAndMatchSimpleSelectorList(e, simpleSelectors, getAllElements(e));
    }

    protected List<Element> matchSimpleSelectorSequence(
            Element e,
            SimpleSelectorSequence simpleSelectorSequence,
            Combinator combinator,
            List<Element> elements) {
        List<SimpleSelector> simpleSelectors = simpleSelectorSequence.getSimpleSelectors();
        List<Element> returnedElements = new ArrayList<>();
        for (Element element : elements) {
            walkAndCombineSimpleSelectorList(element, simpleSelectors, combinator, returnedElements);
        }
        return returnedElements;
    }

    protected List<Element> walkAndMatchSimpleSelectorList(
            Element e,
            List<SimpleSelector> simpleSelectors,
            List<Element> elements) {
        List<Element> resultElements = new ArrayList<>();
        for (Element element : elements) {
            if (matchSimpleSelectorList(element, simpleSelectors)) {
                resultElements.add(element);
            }
        }
        return resultElements;
    }

    protected void walkAndCombineSimpleSelectorList(
            Element e,
            List<SimpleSelector> simpleSelectors,
            Combinator combinator,
            List<Element> elements) {

        List<Element> combinedElements = new ArrayList<>();
        switch (combinator) {
            case DESCENDANT:
                combinedElements.addAll(getAllElements(e));
                combinedElements.remove(e);
                break;
            case CHILD:
                combinedElements.addAll(getChildElements(e));
                break;
            case ADJASCENT_SIBLING:
                if (getParentElement(e) != null) {
                    List<Element> siblings = getChildElements(getParentElement(e));
                    int pos = siblings.indexOf(e);
                    if (pos < siblings.size() - 1) {
                        combinedElements.add(siblings.get(pos + 1));
                    }
                }
                break;
            case GENERAL_SIBLING:
                if (getParentElement(e) != null) {
                    combinedElements.addAll(getChildElements(getParentElement(e)));
                    combinedElements.remove(e);
                }
                break;
            default:
                return;
        }

        elements.addAll(walkAndMatchSimpleSelectorList(e, simpleSelectors, combinedElements));
    }

    protected boolean matchSimpleSelectorList(
            Element e,
            List<SimpleSelector> simpleSelectors) {
        for (SimpleSelector simpleSelector : simpleSelectors) {
            if (!matcherRegistry.matches(e, simpleSelector)) {
                return false;
            }
        }
        return true;
    }
}
