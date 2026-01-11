package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.testutils.Helpers.testTemplate;

import ch.digitalfondue.mjml4j.testutils.MjmlDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

/** Unit test for base cases. */
class RenderingTests {
  @ParameterizedTest
  @MjmlDirectory("base")
  void testBaseTemplates(String name) {
    testTemplate("base", name);
  }

  @ParameterizedTest
  @MjmlDirectory("components")
  void testComponentTemplates(String name) {
    testTemplate("components", name);
  }

  @ParameterizedTest
  @MjmlDirectory("complex")
  void testComplexTemplates(String name) {
    testTemplate("complex", name);
  }

  @ParameterizedTest
  @MjmlDirectory("upstream")
  void testUpstreamTemplates(String name) {
    testTemplate("upstream", name);
  }

  @ParameterizedTest
  @MjmlDirectory("bugs")
  void testBugsTemplates(String name) {
    testTemplate("bugs", name);
  }

  @Disabled
  @ParameterizedTest
  @MjmlDirectory("work")
  void testWork(String name) {
    testTemplate("work", name);
  }

  @Test
  void testCircularInclude() {
    var t =
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> testTemplate("invalid/circular", "include-circular"));
    Assertions.assertTrue(t.getMessage().startsWith("Circular inclusion detected on file"));
  }
}
