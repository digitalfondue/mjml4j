package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.HashSelector;
import ch.digitalfondue.jfiveparse.Element;

public class HashSelectorMatcher implements SimpleSelectorMatcher<HashSelector> {

	public boolean matches(Element e, HashSelector simpleSelector) {
		String id = e.getAttribute("id");
		return id != null && e.getAttribute("id").equals(simpleSelector.getName());
	}

}
