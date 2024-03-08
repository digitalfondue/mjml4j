package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

class Utils {


    static boolean isNullOrWhiteSpace(String v) {
        return v == null || v.isEmpty() || v.isBlank();
    }

    static boolean isNullOrWhiteSpace(StringBuilder v) {
        return v == null || v.isEmpty() || v.toString().isBlank(); // TODO OPTIMIZE
    }

    static boolean isNullOrEmpty(String v) {
        return v == null || v.isEmpty();
    }

    static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    static String parseIntNumberPart(String s) {
        var res = new StringBuilder();
        for (var c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                res.append(c);
            } else {
                break;
            }
        }
        return res.toString();
    }


    static <K, V> LinkedHashMap<K, V> mergeLeft(LinkedHashMap<K, V> f, LinkedHashMap<K, V>... others) {
        var res = new LinkedHashMap<K, V>();

        List<Map.Entry<K, V>> entries = new ArrayList<>(f.entrySet());
        for (var other : others) {
            if (other != null) {
                entries.addAll(other.entrySet());
            }
        }
        for (var entry : entries) {
            if (entry != null) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        return res;
    }


    private static final Set<String> CssProperties = Set.of(
            "background",
            "background-attachment",
            "background-clip",
            "background-color",
            "background-image",
            "background-origin",
            "background-position",
            "background-position-x",
            "background-position-y",
            "background-repeat",
            "background-size",
            "border",
            "border-collapse",
            "border-color",
            "border-spacing",
            "border-style",
            "border-top",
            "border-right",
            "border-bottom",
            "border-left",
            "border-top-color",
            "border-right-color",
            "border-bottom-color",
            "border-left-color",
            "border-top-style",
            "border-right-style",
            "border-bottom-style",
            "border-left-style",
            "border-top-width",
            "border-right-width",
            "border-bottom-width",
            "border-left-width",
            "border-width",
            "bottom",
            "caption-side",
            "clear",
            "clip",
            "color",
            "content",
            "counter-increment",
            "counter-reset",
            "cursor",
            "direction",
            "display",
            "empty-cells",
            "float",
            "font",
            "font-family",
            "font-size",
            "font-size-adjust",
            "font-style",
            "font-variant",
            "font-weight",
            "height",
            "left",
            "letter-spacing",
            "line-height",
            "list-style",
            "list-style-image",
            "list-style-position",
            "list-style-type",
            "margin",
            "margin-top",
            "margin-right",
            "margin-bottom",
            "margin-left",
            "max-height",
            "max-width",
            "min-height",
            "min-width",
            "outline",
            "outline-color",
            "outline-style",
            "outline-width",
            "overflow",
            "padding",
            "padding-top",
            "padding-right",
            "padding-bottom",
            "padding-left",
            "page-break-after",
            "page-break-before",
            "page-break-inside",
            "position",
            "quotes",
            "right",
            "table-layout",
            "text-align",
            "text-decoration",
            "text-indent",
            "text-shadow",
            "text-transform",
            "top",
            "vertical-align",
            "visibility",
            "white-space",
            "width",
            "word-spacing",
            "z-index"
    );

    static boolean isCssProperty(String propertyName) {
        if (isNullOrWhiteSpace(propertyName))
            return false;

        return CssProperties.contains(propertyName.toLowerCase(Locale.ROOT));
    }

    static String suffixCssClasses(String cssClasses, String suffix) {
        if (isNullOrWhiteSpace(cssClasses) || isNullOrWhiteSpace(suffix))
            return "";
        var classes = splitBySpace(cssClasses);
        if (classes.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (var klass : classes) {
            sb.append(' ').append(klass).append('-').append(suffix);
        }
        return sb.toString().trim();
    }

    static final LinkedHashMap<String, AttributeValueType> EMPTY_MAP = new LinkedHashMap<>();

    static <T> LinkedHashMap<String, T> mapOf() {
        return new LinkedHashMap<>(0);
    }

    static <T> LinkedHashMap<String, T> mapOf(String k, T v) {
        var res = new LinkedHashMap<String, T>();
        res.put(k, v);
        return res;
    }

    static <T> LinkedHashMap<String, T> mapOf(String k, T v, String k2, T v2) {
        var res = mapOf(k, v);
        res.put(k2, v2);
        return res;
    }

    static <T> LinkedHashMap<String, T> mapOf(String k, T v, String k2, T v2, String k3, T v3) {
        var res = mapOf(k, v, k2, v2);
        res.put(k3, v3);
        return res;
    }

    static <T> LinkedHashMap<String, T> mapOf(String k, T v, String k2, T v2, String k3, T v3, String k4, T v4) {
        var res = mapOf(k, v, k2, v2, k3, v3);
        res.put(k4, v4);
        return res;
    }

    static LinkedHashMap<String, String> mapOf(String k, String v, String k2, String v2, String... rest) {
        var res = mapOf(k, v, k2, v2);

        for (int i = 0; i < rest.length; i += 2) {
            res.put(rest[i], rest[i + 1]);
        }
        return res;
    }

    static <K, V> LinkedHashMap<K, V> mapOf(Map.Entry<K, V> e, Map.Entry<K, V>... rest) {
        var res = new LinkedHashMap<K, V>();
        res.put(e.getKey(), e.getValue());
        for (var r : rest) {
            res.put(r.getKey(), r.getValue());
        }
        return res;
    }

    static StringBuilder conditionalTag(CharSequence content) {
        return conditionalTag(content, false);
    }


    static StringBuilder msoConditionalTag(CharSequence content) {
        return msoConditionalTag(content, false);
    }

    static StringBuilder msoConditionalTag(CharSequence content, boolean negation) {
        return new StringBuilder(negation ? startMsoNegationConditionalTag : startMsoConditionalTag)
                .append("\n")
                .append(content)
                .append((negation ? endNegationConditionalTag : endConditionalTag)).append("\n");
    }


    static String doubleToString(double d) {
        if (d == (long) d)
            return Long.toString((long) d);
        else
            return Double.toString(d);
    }

    private static final String startConditionalTag = "<!--[if mso | IE]>";
    private static final String startMsoConditionalTag = "<!--[if mso]>";
    private static final String endConditionalTag = "<![endif]-->";
    private static final String startNegationConditionalTag = "<!--[if !mso | IE]><!-->";
    private static final String startMsoNegationConditionalTag = "<!--[if !mso]><!-->";
    private static final String endNegationConditionalTag = "<!--<![endif]-->";

    static StringBuilder conditionalTag(CharSequence content, boolean negation) {
        return new StringBuilder(negation ? startNegationConditionalTag : startConditionalTag).append("\n")
                .append(content)
                .append(negation ? endNegationConditionalTag : endConditionalTag).append("\n");
    }

    static String genRandomHexString() {
        var res = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            res.append(Integer.toString(ThreadLocalRandom.current().nextInt(0, 16), 16));
        }
        return res.toString();
    }

    private static final Pattern VALUE_PATTERN_IN_PX = Pattern.compile("^(\\d+)px$");

    static String makeLowerBreakpoint(String breakPoint) {
        var matcher = VALUE_PATTERN_IN_PX.matcher(breakPoint);
        try {
            matcher.find();
            var res = Integer.parseInt(matcher.group(1));
            return (res - 1) + "px";
        } catch (IllegalStateException | IndexOutOfBoundsException | NumberFormatException e) {
            return breakPoint;
        }
    }


    static boolean equalsIgnoreCase(String a, String b) {
        return (a == null && b == null) || (a != null && a.equalsIgnoreCase(b));
    }

    private static final Pattern SPACE = Pattern.compile("\\s+");

    static final String[] EMPTY_ARRAY_STR = new String[]{};

    static String[] splitBySpace(String s) {
        return s == null ? null : SPACE.split(s);
    }

    private static final Pattern FIND_OUTLOOK_CONDITIONALS = Pattern.compile("(<!\\[endif]-->\\s*?<!--\\[if mso \\| IE]>)", Pattern.MULTILINE);

    static String mergeOutlookConditionals(CharSequence seq) {
        return FIND_OUTLOOK_CONDITIONALS.matcher(seq).replaceAll("");
    }

    private static final Pattern FIND_OUTLOOK_CONDITIONALS_MINIFY = Pattern.compile("(<!--\\[if\\s[^\\]]+]>)([\\s\\S]*?)(<!\\[endif]-->)", Pattern.MULTILINE);
    private static final Pattern FIND_SPACES_BETWEEN_TAGS = Pattern.compile("(^|>)(\\s+)(<|$)", Pattern.MULTILINE);
    private static final Pattern TWO_OR_MORE_SPACES = Pattern.compile("\\s{2,}", Pattern.MULTILINE);

    static String minifyOutlookConditionals(CharSequence seq) {
        return FIND_OUTLOOK_CONDITIONALS_MINIFY.matcher(seq).replaceAll((match) -> {
            var prefix = match.group(1);
            var content = match.group(2);
            var suffix = match.group(3);
            var processedContent = TWO_OR_MORE_SPACES.matcher(FIND_SPACES_BETWEEN_TAGS.matcher(content).replaceAll((spacesMatch) -> {
                var spacesMatchPrefix = spacesMatch.group(1);
                var spacesMatchSuffix = spacesMatch.group(3);
                return spacesMatchPrefix + spacesMatchSuffix;
            })).replaceAll(" ");
            return prefix + processedContent + suffix;
        });
    }

    /**
     * return true if the element has: 0 childs or only empty text nodes
     */
    static boolean hasNonEmptyChildNodes(Element element) {
        if (!element.hasChildNodes()) {
            return false;
        }
        var childNodes = element.getChildNodes();
        var childNodesSize = childNodes.getLength();
        for (var i = 0; i < childNodesSize; i++) {
            var childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.TEXT_NODE && !childNode.getTextContent().isBlank()) {
                return true;
            } else if (childNode.getNodeType() != Node.TEXT_NODE) {
                return true;
            }
        }
        return false;
    }
}
