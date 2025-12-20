package ch.digitalfondue.mjml4j;

import java.util.regex.Pattern;

record AttributeValueType(String value, AttributeType type) {

  static AttributeValueType of(String value) {
    return new AttributeValueType(value, null);
  }

  static AttributeValueType of(String value, AttributeType type) {
    return new AttributeValueType(value, type);
  }

  String process(String value) {
    return type != null ? type.process(value) : value;
  }

  private static final Pattern COLOR_SHORT = Pattern.compile("^#(\\w)(\\w)(\\w)$");

  enum AttributeType {
    ALIGN,
    ALIGN_JUSTIFY,
    BOOLEAN,
    COLOR {
      @Override
      String process(String value) {
        if (value != null) {
          var matcher = COLOR_SHORT.matcher(value);
          if (matcher.find()) {
            return "#"
                + matcher.group(1).repeat(2)
                + matcher.group(2).repeat(2)
                + matcher.group(3).repeat(2);
          }
        }
        return value;
      }
    },
    DIRECTION,
    LEFT_RIGHT,
    INCLUDE_TYPE,
    PIXELS,
    PIXELS_OR_AUTO,
    PIXELS_OR_EM,
    PIXELS_OR_PERCENT,
    PIXELS_OR_PERCENT_OR_NONE,
    FOUR_PIXELS_OR_PERCENT,
    STRING,
    REQUIRED_STRING,
    VERTICAL_ALIGN,
    SOCIAL_TABLE_LAYOUT,
    SOCIAL_MODE,
    TEXT_ALIGN;

    String process(String value) {
      return value;
    }
  }
}
