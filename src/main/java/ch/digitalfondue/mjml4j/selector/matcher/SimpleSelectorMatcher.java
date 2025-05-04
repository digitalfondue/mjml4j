package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.SimpleSelector;
import ch.digitalfondue.jfiveparse.Element;

public interface SimpleSelectorMatcher<T extends SimpleSelector> {

	boolean matches(Element e, T simpleSelector);

}
