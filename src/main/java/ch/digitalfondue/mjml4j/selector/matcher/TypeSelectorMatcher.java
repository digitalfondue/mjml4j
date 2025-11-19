package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.TypeSelector;
import ch.digitalfondue.jfiveparse.Element;

public class TypeSelectorMatcher implements SimpleSelectorMatcher<TypeSelector> {

	public boolean matches(Element e, TypeSelector simpleSelector) {
		return simpleSelector.isUniversal() || e.getNodeName().equalsIgnoreCase(simpleSelector.getType());
	}

}
