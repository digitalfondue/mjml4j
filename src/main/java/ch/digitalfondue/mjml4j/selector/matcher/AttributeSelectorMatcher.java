package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.AttributeOperator;
import br.com.starcode.parccser.model.AttributeSelector;
import ch.digitalfondue.jfiveparse.Element;

public class AttributeSelectorMatcher implements SimpleSelectorMatcher<AttributeSelector> {

	public boolean matches(Element e, AttributeSelector simpleSelector) {
		String attrName = simpleSelector.getName();
		if (simpleSelector.getOperator() == null) {
			return e.getAttributes().get(attrName) != null;
		} else if (e.getAttributes().get(attrName) != null) {
			String expectedValue = simpleSelector.getValue() == null ? null : simpleSelector.getValue().getActualValue();
			String currentValue = e.getAttribute(attrName);

			if (currentValue == null || currentValue.isEmpty()) {
				return AttributeOperator.EQUALS.equals(simpleSelector.getOperator())
						&& (expectedValue == null || expectedValue.isEmpty());
			}

			if ((expectedValue == null || expectedValue.isEmpty())
					&& !AttributeOperator.EQUALS.equals(simpleSelector.getOperator()) ) {
				return false;
			}

			switch (simpleSelector.getOperator()) {
			case EQUALS:
				return currentValue.equals(expectedValue);
			case INCLUDES:
				String[] words = currentValue.split("[\\s]+");
				for (String word : words) {
					if (word.equals(expectedValue)) {
						return true;
					}
				}
				return false;
			case DASH_MATCH:
				return currentValue.equals(expectedValue) || currentValue.startsWith(expectedValue + "-");
			case PREFIX_MATCH:
				return currentValue.startsWith(expectedValue);
			case SUBSTRING_MATCH:
				return currentValue.contains(expectedValue);
			case SUFFIX_MATCH:
				return currentValue.endsWith(expectedValue);
			}
		}
		return false;
	}

}
