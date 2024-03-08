package ch.digitalfondue.mjml4j;

import org.w3c.dom.*;

// directly imported and modified from JFiveParse  html serializer
class DOMSerializer {

    private static final String NAMESPACE_HTML = "http://www.w3.org/1999/xhtml";
    private static final String NAMESPACE_XMLNS = "http://www.w3.org/2000/xmlns/";
    private static final String NAMESPACE_XML = "http://www.w3.org/XML/1998/namespace";
    private static final String NAMESPACE_XLINK = "http://www.w3.org/1999/xlink";


    private static String serializeAttributeName(Attr attribute) {
        String lowercaseName = attribute.getName();
        String name = attribute.getName();
        String namespace = attribute.getNamespaceURI();

        if (NAMESPACE_XML.equals(namespace)) {
            return "xml:" + name;
        } else if (NAMESPACE_XMLNS.equals(namespace)) {
            return "xmlns".equals(lowercaseName) ? "xmlns" : ("xmlns:" + name);
        } else if (NAMESPACE_XLINK.equals(namespace)) {
            return "xlink:" + name;
        } else if (namespace != null) {
            return namespace + ":" + name;// TODO check
        } else {
            return name;
        }
    }

    static void serializeInner(Node node, StringBuilder sb) {
        if (node == null || !node.hasChildNodes()) {
            return;
        }
        var childNodes = node.getChildNodes();
        var count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            visit(childNodes.item(i), sb, count == 1); // we trim only if we have a single text node
        }
    }

    private static void visit(Node node, StringBuilder sb, boolean trim) {
        // open tag
        start(node, sb, trim);
        var childNodes = node.getChildNodes();
        var count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            visit(childNodes.item(i), sb, false);
        }
        end(node, sb);
    }

    private static void end(Node node, StringBuilder appendable) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            if (!skipEndTag(e)) {
                appendable.append("</").append(e.getNodeName()).append(">");
            }
        }
    }

    private static boolean skipEndTag(Element e) {
        return NAMESPACE_HTML.equals(e.getNamespaceURI()) && isNoEndTag(e.getNodeName());
    }

    private static boolean isNoEndTag(String nodeName) {
        switch (nodeName) {
            case "area":
            case "base":
            case "basefont":
            case "bgsound":
            case "br":
            case "col":
            case "embed":
            case "frame":
            case "hr":
            case "img":
            case "input":
            case "keygen":
            case "link":
            case "meta":
            case "param":
            case "source":
            case "track":
            case "wbr":
                return true;
            default:
                return false;
        }
    }

    private static String escapeAttributeValue(Attr attribute) {
        String s = attribute.getValue();
        if (s != null) {
            s = s.replace(Character.valueOf(NO_BREAK_SPACE).toString(), "&nbsp;");
        }
        return s;
    }

    private static String escapeTextData(String s) {
        if (s != null) {
            s = s.replace(Character.valueOf(NO_BREAK_SPACE).toString(), "&nbsp;");
        }
        return s;
    }

    private static void start(Node node, StringBuilder appendable, boolean trim) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            // TODO: for tag outside of html,mathml,svg namespace : use qualified name!
            appendable.append('<').append(e.getNodeName());
            var attributes = e.getAttributes();
            var attributesCount = attributes.getLength();
            for (int i = 0; i < attributesCount; i++) {
                var attr = (Attr) attributes.item(i);
                appendable.append(' ').append(serializeAttributeName(attr));//
                appendable.append('=').append("\"").append(escapeAttributeValue(attr)).append("\"");
            }

            appendable.append('>');

            if ((isHtmlNS(e, "pre") || isHtmlNS(e, "textarea") || isHtmlNS(e, "listing")) && //
                    e.hasChildNodes() && //
                    e.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                String text = ((Text) e.getFirstChild()).getData();
                if (!text.isEmpty() && text.charAt(0) == LF) {
                    appendable.append(LF);
                }
            }

        } else if (node.getNodeType() == Node.TEXT_NODE) {
            Node parent = node.getParentNode();
            boolean literalAppend = false;
            if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
                Element p = (Element) parent;
                literalAppend = NAMESPACE_HTML.equals(p.getNamespaceURI()) && (isTextNodeParent(p.getNodeName()) || ("noscript".equals(p.getNodeName())));
            }
            Text t = (Text) node;
            var text = literalAppend ? t.getData() : escapeTextData(t.getData());
            appendable.append(trim ? text.trim() : text);
        } else if (node.getNodeType() == Node.COMMENT_NODE) {
            appendable.append("<!--").append(((Comment) node).getData()).append("-->");
        } else if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) {
            // TODO: should append the rest of the attributes if present
            appendable.append("<!DOCTYPE ").append(((DocumentType) node).getName()).append('>');
        }
    }


    private static final char LF = 0x000A;
    private static final char NO_BREAK_SPACE = 0x00A0;

    private static boolean isHtmlNS(Element element, String name) {
        return element.getNodeName().equals(name) && element.getNamespaceURI().equals(NAMESPACE_HTML);
    }

    private static boolean isTextNodeParent(String nodeName) {
        switch (nodeName) {
            case "style":
            case "script":
            case "xmp":
            case "iframe":
            case "noembed":
            case "noframes":
            case "plaintext":
                return true;
            default:
                return false;
        }
    }
}
