package ch.digitalfondue.mjml4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// ensure stability in output for the tests...
class KnownOutputTest {

  // this test has various tricky issues,
  // like the "<" and ">" in raw sections, they should not be interpreted as entities
  // text inside the table in wrong locations: they should not be moved around
  @Test
  void checkAgainstRawSectionColumn() throws IOException {
    var result =
        Mjml4j.render(
            Files.readString(
                Path.of("data/components/raw-section-column.mjml"), StandardCharsets.UTF_8));
    Assertions.assertEquals(
        Files.readString(
            Path.of("data/html-output/raw-section-column.html"), StandardCharsets.UTF_8),
        result);
  }

  // check we at least keep html comments & co
  @Test
  void checkNormalizeHtml() throws IOException {
    var in =
        Files.readString(
            Path.of("data/html-output/raw-section-column.html"), StandardCharsets.UTF_8);
    Assertions.assertEquals(
        Files.readString(
            Path.of("data/html-output/raw-section-column-normalized.html"), StandardCharsets.UTF_8),
        Helpers.normalizeHtml(in));
  }
}
