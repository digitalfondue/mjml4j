package ch.digitalfondue.mjml4j.selector.matcher.util;

import br.com.starcode.parccser.model.PseudoSelector;
import br.com.starcode.parccser.model.expression.PseudoExpression;
import ch.digitalfondue.jfiveparse.Element;
import ch.digitalfondue.mjml4j.selector.matcher.InvalidPseudoExpression;

import java.util.List;

public class PseudoExpressionResolver {

    public boolean match(Element e, List<Element> siblings, PseudoSelector pseudoSelector, boolean reverse) {
        if (e == null) {
            throw new InvalidPseudoExpression("Null element!", pseudoSelector);
        }
        if (siblings == null) {
            throw new InvalidPseudoExpression("Null list!", pseudoSelector);
        }
        if (pseudoSelector == null) {
            throw new InvalidPseudoExpression("Null selector!", pseudoSelector);
        }

        PseudoExpression expression = pseudoSelector.getExpression();
        if (expression == null) {
            throw new InvalidPseudoExpression("Null expression!", pseudoSelector);
        }

        int position = siblings.indexOf(e);
        if (position < 0) {
            throw new InvalidPseudoExpression("Provided element must be in the siblings list!", pseudoSelector);
        }

        if (reverse) {
            position = siblings.size() - position - 1;
        }

        if ("odd".equals(expression.getText())) {

            return matchExpression(2, 1, position);

        } else if ("even".equals(expression.getText())) {

            return matchExpression(2, 0, position);

        } else if (expression.isValidGroupExpression()) {

            return matchExpression(
                    expression.getEvaluatedExpression().getA(),
                    expression.getEvaluatedExpression().getB(),
                    position);

        } else {
            throw new InvalidPseudoExpression("Invalid expression!", pseudoSelector);
        }

    }

    protected boolean matchExpression(Integer a, Integer b, int position) {
        if (a == null) a = 0;
        if (b == null) b = 0;
        position++;

        if (a < 0) {
            return position <= b;
        } else {

            //:nth-child(10n-1)  /* represents the 9th, 19th, 29th, etc, element */
            //:nth-child(10n+9)  /* Same */
            if (b < 0) b += a;

            return a == 0 ?
                    b == position :
                    b == position % a;

        }
    }

}
