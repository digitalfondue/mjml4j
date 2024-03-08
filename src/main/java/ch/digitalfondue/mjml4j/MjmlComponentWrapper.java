package ch.digitalfondue.mjml4j;

import org.w3c.dom.Element;

class MjmlComponentWrapper extends MjmlComponentSection {

    MjmlComponentWrapper(Element element, BaseComponent parent, GlobalContext context) {
        super(element, parent, context);
    }

    @Override
    StringBuilder renderChildren(HtmlRenderer renderer) {
        var res = new StringBuilder();

        for (var childComponent : getChildren()) {
            var childContent = childComponent.renderMjml(renderer);
            if (Utils.isNullOrWhiteSpace(childContent)) {
                continue;
            }

            if (childComponent.isRawElement()) {
                res.append(childContent);
            } else if (childComponent instanceof BodyComponent component) {
                renderer.appendCurrentSpacing(res);
                renderer.openIfMsoIE(res, true);
                renderer.openTag("tr", res);
                res.append("<td ").append(component.htmlAttributes(Utils.mapOf(
                        "align", component.getAttribute("align"),
                        "class", Utils.suffixCssClasses(component.getAttribute("css-class"), "outlook"),
                        "width", Utils.doubleToString(getContainerOuterWidth()) + "px"
                ))).append(" >\n");
                renderer.appendCurrentSpacing(res);
                renderer.closeEndif(res, true);
                res.append(childContent);
                renderer.openIfMsoIE(res);
                renderer.closeTag("td", res);
                renderer.closeTag("tr", res);
                renderer.closeEndif(res);
            }
        }
        return res;
    }
}
