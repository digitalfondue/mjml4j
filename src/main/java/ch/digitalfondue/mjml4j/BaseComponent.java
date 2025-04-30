package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.digitalfondue.mjml4j.Utils.equalsIgnoreCase;

abstract class BaseComponent {

    private final Element element;
    private final List<BaseComponent> children = new ArrayList<>();
    private BaseComponent parent;

    final GlobalContext context;

    private LinkedHashMap<String, String> attributes;

    BaseComponent(Element element, BaseComponent parent, GlobalContext context) {
        this.element = element;
        this.parent = parent;
        this.context = context;
        setupComponentAttributes();
    }

    final void doSetupPostConstruction() {
        setupPostConstruction();
        for (var child : children) {
            child.doSetupPostConstruction();
        }
    }

    void setParent(BaseComponent parent) {
        this.parent = parent;
    }

    void setupPostConstruction() {
    }

    LinkedHashMap<String, AttributeValueType> allowedAttributes() {
        throw new IllegalStateException("Not implemented in " + getClass().getCanonicalName());
    }

    private LinkedHashMap<String, String> defaultAttributeValues() {
        var res = new LinkedHashMap<String, String>();
        for (var kvt : allowedAttributes().entrySet()) {
            res.put(kvt.getKey(), kvt.getValue().value());
        }
        return res;
    }

    LinkedHashMap<String, String> getAttributes() {
        return attributes;
    }

    final void setupComponentAttributes() {
        attributes = new LinkedHashMap<>(defaultAttributeValues());
        if (!attributes.containsKey("mj-class")) {
            attributes.put("mj-class", null);
        }
        if (!attributes.containsKey("css-class")) {
            attributes.put("css-class", null);
        }
        if (element.hasAttributes()) {
            setAttributes();
        }
    }


    void setAttributes() {
        var attributesLength = element.getAttributes().getLength();
        var elemAttr = element.getAttributes();
        for (int i = 0; i < attributesLength; i++) {
            var attr = elemAttr.item(i);
            var userAttributeName = attr.getNodeName().toLowerCase(Locale.ROOT);
            var userAttributeValue = attr.getNodeValue();
            if (!attributes.containsKey(userAttributeName)) {
                continue;
            }
            attributes.put(userAttributeName, userAttributeValue);
        }
    }


    final String getTagName() {
        return element.getNodeName().toLowerCase(Locale.ROOT);
    }

    boolean isRawElement() {
        return false;
    }

    final Element getElement() {
        return element;
    }

    final List<BaseComponent> getChildren() {
        return children;
    }

    final BaseComponent getParent() {
        return parent;
    }

    final String getAttribute(String attributeName) {
        var defaultAndType = allowedAttributes().get(attributeName);
        var value = getAttributeInternal(attributeName);
        // apply post-processing on fetched attribute (at the moment, only color has a custom processing logic)
        if (defaultAndType != null) {
            return defaultAndType.process(value);
        } else {
            return value;
        }
    }

    // priority as derived from code and tests:
    //  0. own attribute
    //  1. mj-class attribute
    //  2. mj-attribute by own tag name
    //  3. by inheriting from parent
    //  4. by mj-all
    //  5. by default
    final String getAttributeInternal(String attributeName) {
        // the dom element has the attribute present
        if (!Utils.isNullOrWhiteSpace(element.getAttribute(attributeName))) {
            return element.getAttribute(attributeName);
        }
        //
        if (!context.attributesByClass.isEmpty()) {
            var currentClasses = Utils.EMPTY_ARRAY_STR;
            if (attributes.containsKey("mj-class") && attributes.get("mj-class") != null) {
                currentClasses = Utils.splitBySpace(attributes.get("mj-class").trim());
            }
            String classAttribute = null;
            for (var className : currentClasses) {
                if (context.attributesByClass.containsKey(className) && context.attributesByClass.get(className).containsKey(attributeName)) {
                    classAttribute = context.attributesByClass.get(className).get(attributeName);
                }
            }

            if (classAttribute != null) {
                return classAttribute;
            }
        }

        //
        if (context.attributesByName.containsKey(attributeName)) {
            var byType = context.attributesByName.get(attributeName);
            var tagName = getTagName();
            if (byType.containsKey(tagName)) {
                return byType.get(tagName);
            }
        }
        //

        if (parent != null) {
            var attribute = parent.getInheritingAttribute(attributeName);
            if (attribute != null) {
                return attribute;
            }
        }

        //

        if (context.attributesByName.containsKey(attributeName)) {
            var byType = context.attributesByName.get(attributeName);
            if (byType.containsKey("mj-all")) {
                return byType.get("mj-all");
            }
        }


        // https://github.com/SebastianStehle/mjml-net/blob/d087dfade21495076840bc2b44c6eb858e6ea97e/Mjml.Net/Internal/Binder.cs#L41
        // default value (if present)
        return attributes.get(attributeName);
    }

    String getInheritingAttribute(String attributeName) {
        return null;
    }

    final boolean hasAttribute(String attributeName) {
        if (attributes.containsKey(attributeName)) {
            var attrVal = getAttributeInternal(attributeName);
            return attrVal != null && !attrVal.isEmpty();
        }
        return false;
    }

    final String getContent() {
        var nodeValue = element.getNodeValue();
        return nodeValue == null || nodeValue.isBlank() ? element.getTextContent() : nodeValue;
    }

    StringBuilder renderChildren(HtmlRenderer renderer) {
        StringBuilder sb = new StringBuilder();
        for (var childComponent : children) {
            sb.append(childComponent.renderMjml(renderer));
        }
        return sb;
    }

    final boolean hasParentComponent() {
        return getParent() != null;
    }

    final BaseComponent getParentComponent() {
        if (!hasParentComponent()) {
            throw new IllegalStateException("No parent component");
        }
        return getParent();
    }

    StringBuilder renderMjml(HtmlRenderer renderer) {
        return renderChildren(renderer);
    }

    static abstract class BodyComponent extends BaseComponent {

        CssBoxModel cssBoxModel;
        private final CssStyleLibraries styleLibraries = new CssStyleLibraries();

        BodyComponent(Element element, BaseComponent parent, GlobalContext context) {
            super(element, parent, context);
        }

        void setupPostConstruction() {
            cssBoxModel = getBoxModel();
            var tagName = getTagName();
            context.addHeadStyle(tagName, headStyle());
            context.addComponentHeadStyle(componentsHeadStyle());

            setupStyles(styleLibraries);
        }

        String headStyle() {
            return "";
        }

        String componentsHeadStyle() {
            return "";
        }

        CssBoxModel getBoxModel() {
            var containerWidth = CssUnitParser.parse(context.containerWidth);

            var paddings = getShorthandAttributeValue("padding", "right") + getShorthandAttributeValue("padding", "left");

            var borders = getShorthandBorderValue("border", "right") + getShorthandBorderValue("border", "left");


            if (hasParentComponent() && getParent() instanceof BodyComponent parent) {

                containerWidth = containerWidth.withValue(parent.getContainerInnerWidth() - paddings - borders);

                return new CssBoxModel(
                        parent.getContainerInnerWidth(),
                        borders,
                        paddings,
                        containerWidth.value()
                );
            }
            return new CssBoxModel(
                    containerWidth.value(),
                    borders,
                    paddings,
                    containerWidth.value());
        }


        private static final Pattern PATTERN_SHORTHAND_BORDER_VALUE = Pattern.compile("(?:(?:^| )([0-9]+))");

        double getShorthandBorderValue(String attributeName, String direction) {
            var mjAttributeDirection = getAttribute(attributeName + "-" + direction);
            var mjAttribute = getAttribute(attributeName);

            if (!Utils.isNullOrWhiteSpace(mjAttributeDirection)) {
                return CssUnitParser.parse(mjAttributeDirection).value();
            }

            if (Utils.isNullOrWhiteSpace(mjAttribute)) {
                return 0;
            }

            // MERGED borderParser: https://github.com/mjmlio/mjml/blob/d4c6ea0744e05c928044108c3117c16a9c4110fe/packages/mjml-core/src/helpers/shorthandParser.js#L3
            //return CssUnitParser.Parse(mjAttribute).Value;

            var match = PATTERN_SHORTHAND_BORDER_VALUE.matcher(mjAttribute);

            if (!match.find())
                return 0;

            return Double.parseDouble(match.group().trim());
        }

        double getContainerInnerWidth() {
            return cssBoxModel.boxWidth();
        }

        double getContainerOuterWidth() {
            return cssBoxModel.totalWidth();
        }


        double getShorthandAttributeValue(String attribute, String direction) {
            var mjAttributeDirection = getAttribute(attribute + "-" + direction);
            var mjAttribute = getAttribute(attribute);

            if (!Utils.isNullOrWhiteSpace(mjAttributeDirection))
                return CssUnitParser.parse(mjAttributeDirection).value();

            if (Utils.isNullOrWhiteSpace(mjAttribute))
                return 0;

            // MERGED shorthandParser: https://github.com/mjmlio/mjml/blob/d4c6ea0744e05c928044108c3117c16a9c4110fe/packages/mjml-core/src/helpers/shorthandParser.js#L3
            var splittedCssValue = Utils.splitBySpace(mjAttribute);
            Map<String, Integer> directions;

            // FIXME: don't use map here
            switch (splittedCssValue.length) {
                case 2:
                    directions = Map.of(
                            "top", 0,
                            "bottom", 0,
                            "left", 1,
                            "right", 1);
                    break;
                case 3:
                    directions = Map.of(
                            "top", 0,
                            "bottom", 2,
                            "left", 1,
                            "right", 1
                    );
                    break;

                case 4:
                    directions = Map.of(
                            "top", 0,
                            "bottom", 2,
                            "left", 3,
                            "right", 1
                    );
                    break;
                case 1:
                default:
                    return CssUnitParser.parse(mjAttribute).value();
            }

            return CssUnitParser.parse(splittedCssValue[directions.get(direction)]).value();
        }

        StringBuilder htmlAttributes(LinkedHashMap<String, String> htmlAttributes) {
            return htmlAttributes(htmlAttributes, null, false);
        }

        StringBuilder htmlAttributes(LinkedHashMap<String, String> htmlAttributes, LinkedHashMap<String, String> styleOverride) {
            return htmlAttributes(htmlAttributes, styleOverride, false);
        }

        StringBuilder htmlAttributes(LinkedHashMap<String, String> htmlAttributes, LinkedHashMap<String, String> styleOverride, boolean mergeDefaultCSSProperties) {
            StringBuilder sb = new StringBuilder();

            for (var htmlAttributePair : htmlAttributes.entrySet()) {
                var value = htmlAttributePair.getValue();
                var key = htmlAttributePair.getKey();

                if (equalsIgnoreCase(key, "style")) {
                    if (!value.contains(":") && !value.contains(";")) {
                        value = styles(htmlAttributePair.getValue(), styleOverride, mergeDefaultCSSProperties);
                    }
                }

                if (value != null) {
                    sb.append(" ").append(key).append("=\"").append(value).append('"');
                }
            }
            return sb;
        }

        String inlineCss(LinkedHashMap<String, String> cssProperties) {
            var sb = new StringBuilder();
            for (var cssPropertyPair : cssProperties.entrySet()) {
                var value = cssPropertyPair.getValue();

                if (!Utils.isNullOrWhiteSpace(value)) {
                    sb.append(cssPropertyPair.getKey()).append(":").append(cssPropertyPair.getValue()).append(";");
                }
            }
            return sb.toString().trim();
        }


        String styles(String styleLibraryName) {
            return styles(styleLibraryName, null, false);
        }

        String styles(String styleLibraryName, LinkedHashMap<String, String> styleOverride, boolean outputDefaults) {

            var styleLibrary = styleLibraries.getStyleLibrary(styleLibraryName);

            if (!outputDefaults)
                return inlineCss(Utils.mergeLeft(styleLibrary, styleOverride));

            var defaultStyles = new LinkedHashMap<String, String>();

            for (var attribute : getAttributes().entrySet()) {
                if (Utils.isNullOrWhiteSpace(attribute.getValue()))
                    continue;

                if (!Utils.isCssProperty(attribute.getKey())) {
                    continue;
                }

                defaultStyles.put(attribute.getKey(), attribute.getValue());
            }

            return inlineCss(Utils.mergeLeft(defaultStyles, styleLibrary, styleOverride));
        }


        void setupStyles(CssStyleLibraries cssStyleLibraries) {
        }

    }

    static abstract class HeadComponent extends BaseComponent {

        HeadComponent(Element element, BaseComponent parent, GlobalContext context) {
            super(element, parent, context);
        }

        void handler() {
            throw new IllegalStateException("Not implemented in " + getClass().getCanonicalName());
        }

        @Override
        StringBuilder renderMjml(HtmlRenderer renderer) {
            handler();
            return renderChildren(renderer);
        }
    }

    boolean isEndingTag() {
        return false;
    }
}
