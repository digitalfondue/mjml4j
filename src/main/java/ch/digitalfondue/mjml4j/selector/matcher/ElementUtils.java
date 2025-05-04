package ch.digitalfondue.mjml4j.selector.matcher;

import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.jfiveparse.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with `Element` objects.
 */
class ElementUtils {

    private ElementUtils() {
        // Prevent instantiation of utility class
    }

    /**
     * Retrieves the parent element of the given element.
     *
     * @param e the element whose parent is to be retrieved
     * @return the parent element, or `null` if the parent is not an element or does not exist
     */
    static Element getParentElement(Element e) {
        Node parentNode = e.getParentNode();
        if (parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        return (Element) parentNode;
    }

    /**
     * Retrieves all child elements of the given element.
     *
     * @param e the element whose child elements are to be retrieved
     * @return a list of child elements
     */
    static List<Element> getChildElements(Element e) {
        List<Element> childElements = new ArrayList<>();
        for (Node childNode : e.getChildNodes()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                childElements.add((Element) childNode);
            }
        }
        return childElements;
    }

    /**
     * Retrieves the root element of the given element.
     * The root element is the topmost ancestor in the element hierarchy.
     *
     * @param e the element whose root element is to be retrieved
     * @return the root element
     */
    static Element getRootElement(Element e) {
        Node parentNode = e.getParentNode();
        if (parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE) {
            return e;
        }
        return getRootElement((Element) parentNode);
    }

    /**
     * Retrieves all elements in the subtree rooted at the given element.
     *
     * @param e the root element of the subtree
     * @return a list of all elements in the subtree, including the root element
     */
    static List<Element> getAllElements(Element e) {
        List<Element> allElements = new ArrayList<>();
        allElements.add(e);
        for (Node childNode : e.getChildNodes()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                allElements.addAll(getAllElements((Element) childNode));
            }
        }
        return allElements;
    }
}
