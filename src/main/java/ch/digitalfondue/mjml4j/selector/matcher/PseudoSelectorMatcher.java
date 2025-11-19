package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.PseudoSelector;
import br.com.starcode.parccser.model.PseudoType;
import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.mjml4j.selector.matcher.util.PseudoExpressionResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ch.digitalfondue.mjml4j.selector.matcher.ElementUtils.getChildElements;
import static ch.digitalfondue.mjml4j.selector.matcher.ElementUtils.getParentElement;
import static ch.digitalfondue.mjml4j.selector.matcher.ElementUtils.getRootElement;

public class PseudoSelectorMatcher implements SimpleSelectorMatcher<PseudoSelector> {

    PseudoExpressionResolver resolver = new PseudoExpressionResolver();

    /**
     * http://www.w3.org/TR/css3-selectors/#selectors
     * TODO test indexes to not fail
     */
    public boolean matches(Element e, PseudoSelector simpleSelector) {

        String name = simpleSelector.getName();
        if (PseudoType.PSEUDO_CLASS.equals(simpleSelector.getType())) {

            try {
                if ("root".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return e.equals(getRootElement(e));
                } else if ("nth-child".equals(name)) {
                    return resolver.match(e, getSiblings(e), simpleSelector, false);
                } else if ("nth-last-child".equals(name)) {
                    return resolver.match(e, getSiblings(e), simpleSelector, true);
                } else if ("nth-of-type".equals(name)) {
                    return resolver.match(e, getSiblingsOfType(e), simpleSelector, false);
                } else if ("nth-last-of-type".equals(name)) {
                    return resolver.match(e, getSiblingsOfType(e), simpleSelector, true);
                } else if ("first-child".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return getParentElement(e) == null || e.equals(getSibling(e, 1, false));
                } else if ("last-child".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return getParentElement(e) == null || e.equals(getSibling(e, 1, true));
                } else if ("first-of-type".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return getParentElement(e) == null
                            || e.equals(getSiblingOfType(e, 1, false));
                } else if ("last-of-type".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return getParentElement(e) == null
                            || e.equals(getSiblingOfType(e, 1, true));
                } else if ("only-child".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return getParentElement(e) == null ||
                            getChildElements(getParentElement(e)).size() == 1;
                } else if ("only-of-type".equals(name)) {
                    assertNullExpression(simpleSelector);
                    if (getParentElement(e) == null) {
                        return true;
                    }
                    return getSiblingsOfType(e).size() == 1;
                } else if ("empty".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return getChildElements(e).isEmpty() && e.getInnerHTML().isEmpty();
                } else if ("enabled".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return isUserInterfaceElementCanBeDisabled(e) && e.getAttributes().get("disabled") == null;
                } else if ("disabled".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return isUserInterfaceElementCanBeDisabled(e) && e.getAttributes().get("disabled") != null;
                } else if ("checked".equals(name)) {
                    assertNullExpression(simpleSelector);
                    return isUserInterfaceElementChecked(e);
                }

            } catch (InvalidPseudoExpression ex) {
                //TODO handle exception
            }

        } else if (PseudoType.PSEUDO_ELEMENT.equals(simpleSelector.getType())) {
            //nothing to do
        }
        return false;

    }

    protected List<Element> getSiblings(Element e) {
        if (getParentElement(e) == null) {
            return List.of(e);
        }
        return getChildElements(getParentElement(e));
    }

    protected List<Element> getSiblingsOfType(Element e) {
        if (getParentElement(e) == null) {
            return List.of(e);
        }
        List<Element> children = new ArrayList<>(getChildElements(getParentElement(e)));
        for (Iterator<Element> it = children.iterator(); it.hasNext(); ) {
            if (!e.getNodeName().equals(it.next().getNodeName())) it.remove();
        }
        return children;
    }

    protected Element getSibling(Element e, int position, boolean reverse) {
        List<Element> children = getChildElements(getParentElement(e));
        if (position > 0 && position <= children.size()) {
            return children.get(reverse ? (children.size() - position) : (position - 1));
        }
        return null;
    }

    protected Element getSiblingOfType(Element e, int position, boolean reverse) {
        List<Element> children = getSiblingsOfType(e);
        if (position > 0 && position <= children.size()) {
            return children.get(reverse ? (children.size() - position) : (position - 1));
        }
        return null;
    }

    protected void assertNullExpression(PseudoSelector simpleSelector) {
        if (simpleSelector.getExpression() != null) {
            throw new InvalidPseudoExpression("Expression not expected", simpleSelector);
        }
    }

    /**
     * http://www.w3.org/TR/html5/disabled-elements.html
     */
    protected boolean isUserInterfaceElementCanBeDisabled(Element e) {
        return "a".equals(e.getNodeName())
                || "area".equals(e.getNodeName())
                || "link".equals(e.getNodeName())
                || "input".equals(e.getNodeName())
                || "button".equals(e.getNodeName())
                || "select".equals(e.getNodeName())
                || "textarea".equals(e.getNodeName())
                || "optgroup".equals(e.getNodeName())
                || "option".equals(e.getNodeName())
                || "fieldset".equals(e.getNodeName());
    }

    /**
     * http://www.w3.org/TR/html5/disabled-elements.html
     */
    protected boolean isUserInterfaceElementChecked(Element e) {
        String type = e.getAttribute("type");
        if ("input".equals(e.getNodeName())) {
            return e.getAttributes().get("checked") != null
                    && ("checkbox".equals(type) || "radio".equals(type));
        }
        return "option".equals(e.getNodeName()) && e.getAttributes().get("selected") != null;
    }
}
