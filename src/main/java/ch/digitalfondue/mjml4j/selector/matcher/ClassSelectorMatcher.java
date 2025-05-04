package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.ClassSelector;
import ch.digitalfondue.jfiveparse.Element;

import java.util.regex.Pattern;

public class ClassSelectorMatcher implements SimpleSelectorMatcher<ClassSelector> {

	public boolean matches(Element e, ClassSelector simpleSelector) {
		String attrClass = e.getAttribute("class");
		return attrClass != null && Pattern.compile(
				".*(\\s|^)" + Pattern.quote(simpleSelector.getClassName()) + "(\\s|$).*", Pattern.DOTALL)
				.matcher(attrClass)
				.matches();
	}

}
