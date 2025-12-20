package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

import org.junit.jupiter.api.Test;

class BugTests {

  // see https://github.com/digitalfondue/mjml4j/issues/7
  @Test
  void checkMjSocialNpe() {
    testTemplate("bug-mj-social");
  }

  @Test
  void checkMjColumnInnerBorderNfe() {
    testTemplate("bug-mj-column-inner-border");
  }
}
