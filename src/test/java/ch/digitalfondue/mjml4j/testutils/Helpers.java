package ch.digitalfondue.mjml4j.testutils;

import ch.digitalfondue.mjml4j.Mjml4j;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;

public class Helpers {
  private static String beautifyHtml(String html) throws IOException {
    var parsed = Jsoup.parse(html);
    var writer = new CSSWriter();
    parsed.select("style").forEach(e -> {
      e.text(writer.getCSSAsString(CSSReader.readFromString(e.text())));
    });

    return parsed.html();
  }

  private static String simplifyBrTags(String input) {
    return input.replaceAll("<br\s*/>", "<br>");
  }

  private static String alignDoctype(String input) {
    return input.replace("<!DOCTYPE html>", "<!doctype html>");
  }

  // align all values defined in ids -> mjml use random strings, for coherence reason we find all
  // id="...." and replace them with
  // a sequence. this allows to align id=".." and for=".."
  private static String alignIdFor(String input) {
    var findIds =
        Pattern.compile(
            "id=\"[^\"]*([0-9a-f]{16})[^\"]*\"", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    var matcher = findIds.matcher(input);
    var matches = new ArrayList<String>();
    while (matcher.find()) {
      matches.add(matcher.group(1));
    }
    var res = input;
    int i = 0;
    for (var id : matches) {
      i++;
      res = res.replace(id, "replacement_id_" + i);
    }
    return res;
  }

  public static void testTemplate(String directory, String name) {
    var resolver = new Mjml4j.FileSystemResolver(Path.of("data", directory));
    try {
      var template =
          Files.readString(Path.of("data", directory, name + ".mjml"), StandardCharsets.UTF_8);
      var conf = new Mjml4j.Configuration("und", Mjml4j.TextDirection.AUTO, resolver);
      var res = Mjml4j.render(template, conf);
      var comparison =
          Files.readString(Path.of("data", "compiled", name + ".html"), StandardCharsets.UTF_8);
      Assertions.assertEquals(
          simplifyBrTags(alignIdFor(beautifyHtml(comparison))),
          alignIdFor(beautifyHtml(alignDoctype(res))));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
