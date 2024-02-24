package ch.digitalfondue.mjml4j;

import java.util.Locale;
import java.util.regex.Pattern;

class CssUnitParser {


    static class CssParsedUnit {
        final String unit;
        float value;

        CssParsedUnit(String unit, float value) {
            this.unit = unit;
            this.value = value;
        }

        boolean isPercent() {
            return "%".equals(unit);
        }

        boolean isPx() { return "px".equals(unit); }

        @Override
        public String toString() {
            return Utils.floatToString(value) + unit;
        }

    }

    private static final Pattern UNIT_PATTERN = Pattern.compile("([0-9.,]*)([^0-9]*)$");

    static CssParsedUnit parse(String cssValue) {

        if (Utils.isNullOrWhiteSpace(cssValue)) {
            return new CssParsedUnit(null, 0);
        }

        var match = UNIT_PATTERN.matcher(cssValue);

        if (!match.find())
            throw new IllegalStateException("CssWidthParser could not parse " + cssValue + " due to invalid format");

        var widthValue = match.group(1);
        var widthUnit = match.groupCount() != 2 ? "px" : match.group(2);
        if (Utils.isNullOrWhiteSpace(widthUnit)) {
            widthUnit = "px";
        }

        return switch (widthUnit) {
            case "%" -> new CssParsedUnit(widthUnit, Float.parseFloat(widthValue));
            default -> new CssParsedUnit(widthUnit, (int) Float.parseFloat(widthValue));
        };
    }
}
