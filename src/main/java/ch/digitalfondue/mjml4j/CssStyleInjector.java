package ch.digitalfondue.mjml4j;


import ch.digitalfondue.jfiveparse.Document;
import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.mjml4j.selector.CssSelector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CssStyleInjector {

    private CssStyleInjector() {
        // Prevent instantiation of utility class
    }

    /**
     * Applies inline styles to multiple CSS selectors within a JFiveParse Document.
     * Ensures styles are merged without duplication of CSS properties.
     *
     * @param document       The JFiveParse Document to modify. Must not be null.
     * @param selectorStyles A map of CSS selectors to their corresponding inline styles.
     *                       Each key is a CSS selector, and the value is the style string to apply.
     *                       If the map is null or empty, no styles are applied.
     */
    public static void applyInlineStyles(Document document, Map<String, String> selectorStyles) {
        if (document == null || selectorStyles == null || selectorStyles.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : selectorStyles.entrySet()) {
            String selector = entry.getKey();
            String styleToApply = entry.getValue();

            if (styleToApply == null || styleToApply.trim().isEmpty()) {
                continue; // Skip empty styles
            }

            try {
                List<Element> elements = CssSelector.select(document, selector);
                for (Element element : elements) {
                    String mergedStyle = mergeStyles(element.getAttribute("style"), styleToApply);
                    element.setAttribute("style", mergedStyle);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error applying selector: " + selector, e);
            }
        }
    }

    /**
     * Merges two CSS style strings without duplicating properties.
     * If a property exists in both style strings, the value from the new style string
     * will overwrite the value from the existing style string.
     *
     * @param existingStyle The current inline style string. Can be null or empty.
     * @param newStyle      The new style string to add. Can be null or empty.
     * @return The merged style string, with properties from both input strings.
     */
    private static String mergeStyles(String existingStyle, String newStyle) {
        Map<String, String> styleMap = new LinkedHashMap<>();

        if (existingStyle != null) {
            parseStyleString(existingStyle, styleMap);
        }
        if (newStyle != null) {
            parseStyleString(newStyle, styleMap);
        }

        StringBuilder merged = new StringBuilder();
        for (Map.Entry<String, String> entry : styleMap.entrySet()) {
            if (!merged.isEmpty()) {
                merged.append(" ");
            }
            merged.append(entry.getKey()).append(": ").append(entry.getValue()).append(";");
        }

        return merged.toString();
    }

    /**
     * Parses a CSS style string and adds its properties to a map.
     * If a property already exists in the map, the new value will overwrite it
     * based on the priority of `!important`.
     *
     * @param style The CSS style string to parse. Can be null or empty.
     * @param map   The map to store parsed properties. Must not be null.
     */
    private static void parseStyleString(String style, Map<String, String> map) {
        if (style == null || style.trim().isEmpty()) {
            return;
        }

        String[] rules = style.split(";");
        for (String rule : rules) {
            if (rule.trim().isEmpty()) continue;

            String[] parts = rule.trim().split(":", 2);
            if (parts.length != 2) continue;

            String property = parts[0].trim().toLowerCase();
            String value = parts[1].trim();

            boolean newImportant = value.endsWith("!important");
            String newValue = newImportant ? value.replaceFirst("!important\\s*$", "").trim() : value;

            String currentValue = map.get(property);
            boolean currentImportant = currentValue != null && currentValue.trim().endsWith("!important");

            if (currentValue == null || (!currentImportant && newImportant) || (currentImportant && newImportant)) {
                // We always strip !important from stored value since it's for inline use
                map.put(property, newValue);
            }
        }
    }
}
