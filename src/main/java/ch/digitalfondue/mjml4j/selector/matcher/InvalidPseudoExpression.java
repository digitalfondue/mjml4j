package ch.digitalfondue.mjml4j.selector.matcher;

import br.com.starcode.parccser.model.PseudoSelector;

public class InvalidPseudoExpression extends IllegalArgumentException {
    public InvalidPseudoExpression(String s, PseudoSelector simpleSelector) {
        super(s);
    }
}
