package ch.digitalfondue.mjml4j;

class HtmlRenderer {
    private int depth;

    void increaseDepth() {
        depth++;
    }

    void openTag(String name, StringBuilder buffer) {
        buffer.append("  ".repeat(Math.max(0, depth)));
        buffer.append("<").append(name).append(">\n");
        depth++;
    }

    void openTag(String name, StringBuilder attributes, StringBuilder buffer, boolean spaceEnd) {
        buffer.append("  ".repeat(Math.max(0, depth)));
        buffer.append("<").append(name).append(" ").append(attributes);
        if (spaceEnd) {
            buffer.append(" ");
        }
        buffer.append(">\n");
        depth++;
    }

    void openTag(String name, StringBuilder attributes, StringBuilder buffer) {
        openTag(name, attributes, buffer, true);
    }

    void openCloseTag(String name, StringBuilder attributes, StringBuilder buffer) {

        buffer.append("  ".repeat(Math.max(0, depth)));
        buffer.append("<").append(name).append(" ").append(attributes).append(" />\n");
    }

    void closeTag(String name, StringBuilder buffer) {
        depth--;
        buffer.append("  ".repeat(Math.max(0, depth)));
        buffer.append("</").append(name).append(">\n");

    }

    void openIfMsoIE(StringBuilder buffer) {
        openIfMsoIE(buffer, false);
    }

    void openIfMsoIE(StringBuilder buffer, boolean newLine) {
        buffer.append("<!--[if mso | IE]>");
        if (newLine) {
            buffer.append('\n');
        }
    }

    void closeEndif(StringBuilder buffer) {
        closeEndif(buffer, false);
    }

    void closeEndif(StringBuilder buffer, boolean newLine) {
        buffer.append("<![endif]-->");
        if (newLine) {
            buffer.append('\n');
        }
    }

    void appendCurrentSpacing(StringBuilder buffer) {
        buffer.append("  ".repeat(Math.max(0, depth)));
    }
}
