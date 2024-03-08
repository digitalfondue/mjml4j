package ch.digitalfondue.mjml4j;

import java.util.regex.Pattern;

class CssUnitParser {


    static class CssParsedUnit {
        final String unit;
        final double value;

        final double valueFullPrecision;

        CssParsedUnit(String unit, double value, double valueFullPrecision) {
            this.unit = unit;
            this.value = value;
            this.valueFullPrecision = valueFullPrecision;
        }

        boolean isPercent() {
            return "%".equals(unit);
        }

        boolean isPx() { return "px".equals(unit); }

        @Override
        public String toString() {
            return Utils.doubleToString(value) + unit;
        }

        public String toFullPrecisionString() {
            return Utils.doubleToString(valueFullPrecision) + unit;
        }

        CssParsedUnit withValue(double value) {
            return new CssParsedUnit(unit, value, value);
        }

    }

    private static final Pattern UNIT_PATTERN = Pattern.compile("([0-9.,]*)([^0-9]*)$");

    static CssParsedUnit parse(String cssValue) {

        if (Utils.isNullOrWhiteSpace(cssValue)) {
            return new CssParsedUnit(null, 0, 0);
        }

        var match = UNIT_PATTERN.matcher(cssValue);

        if (!match.find())
            throw new IllegalStateException("CssWidthParser could not parse " + cssValue + " due to invalid format");

        var widthValue = match.group(1);
        var widthUnit = match.groupCount() != 2 ? "px" : match.group(2);
        if (Utils.isNullOrWhiteSpace(widthUnit)) {
            widthUnit = "px";
        }

        var valueFullPrecision = Double.parseDouble(widthValue);
        if ("%".equals(widthUnit)) {
            return new CssParsedUnit(widthUnit, valueFullPrecision, valueFullPrecision);
        } else {
            return new CssParsedUnit(widthUnit, (int) valueFullPrecision, valueFullPrecision);
        }
    }
}
