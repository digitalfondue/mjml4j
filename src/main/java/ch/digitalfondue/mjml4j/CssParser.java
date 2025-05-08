package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssParser {
    private static final Pattern pattern = Pattern.compile("(?s)([^{}]+)\\{([^}]+)}");

    private CssParser() {
        // Prevent instantiation of utility class
    }

    /**
     * Parses CSS string and returns a map of selector -> style.
     *
     * @param css The CSS string to parse.
     * @return A map where the key is the selector and the value is the style.
     */
    public static Map<String, String> parseCss(String css) {
        Map<String, String> styles = new LinkedHashMap<>();

        Matcher matcher = pattern.matcher(css);

        while (matcher.find()) {
            String selector = matcher.group(1).trim();
            String style = matcher.group(2).trim();
            styles.put(selector, style);
        }

        return styles;
    }
}
