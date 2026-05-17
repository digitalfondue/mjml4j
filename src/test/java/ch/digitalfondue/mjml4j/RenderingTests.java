package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

/** Unit test for base cases. */
public class RenderingTests {
  @ParameterizedTest
  @MjmlDirectory("base")
  public void testBaseTemplates(String name) {
    testTemplate("base", name);
  }

  @ParameterizedTest
  @MjmlDirectory("components")
  public void testComponentTemplates(String name) {
    testTemplate("components", name);
  }

  @ParameterizedTest
  @MjmlDirectory("complex")
  public void testComplexTemplates(String name) {
    testTemplate("complex", name);
  }

  @ParameterizedTest
  @MjmlDirectory("upstream")
  public void testUpstreamTemplates(String name) {
    testTemplate("upstream", name);
  }

  @ParameterizedTest
  @MjmlDirectory("bugs")
  public void testBugsTemplates(String name) {
    testTemplate("bugs", name);
  }

  @Disabled
  @ParameterizedTest
  @MjmlDirectory("work")
  public void testWork(String name) {
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
