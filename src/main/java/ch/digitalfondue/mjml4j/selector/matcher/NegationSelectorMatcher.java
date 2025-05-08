package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.NegationSelector;
import ch.digitalfondue.jfiveparse.Element;

public class NegationSelectorMatcher implements SimpleSelectorMatcher<NegationSelector> {

	MatcherRegistry matcherRegistry;

	public NegationSelectorMatcher(MatcherRegistry matcherRegistry) {
		this.matcherRegistry = matcherRegistry;
	}

	public boolean matches(Element e, NegationSelector simpleSelector) {
		return !matcherRegistry.get(
				simpleSelector.getSimpleSelector()
			).matches(e, simpleSelector.getSimpleSelector());
	}
}
