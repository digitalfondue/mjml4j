package ch.digitalfondue.mjml4j;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.junit.jupiter.api.Assertions;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class Helpers {

  private static String normalizeHtml(String html) {
    try {
      var builder = new HtmlDocumentBuilder();
      builder.setIgnoringComments(false);
      var doc = builder.parse(new InputSource(new StringReader(html)));
      CSSOMParser cssParser = new CSSOMParser();

      // normalize css
      var allStyle = doc.getElementsByTagName("style");
      for (int i = 0; i < allStyle.getLength(); i++) {
        var style = allStyle.item(i);
        var content = style.getTextContent();
        var source = new org.htmlunit.cssparser.parser.InputSource(new StringReader(content));

        CSSStyleSheetImpl sheet = cssParser.parseStyleSheet(source, null);
        style.setTextContent(sheet.toString());
      }

      // normalize html document (remove useless spaces)
      // doc.normalizeDocument();
      var xPath = XPathFactory.newInstance().newXPath();
      var textNodes = (NodeList) xPath.evaluate("//text()", doc, XPathConstants.NODESET);
      for (int i = 0; i < textNodes.getLength(); i++) {
        var node = textNodes.item(i);
        var cleanText = (String) xPath.evaluate("normalize-space(.)", node, XPathConstants.STRING);
        node.setNodeValue(cleanText);
      }
      //
      var tf = TransformerFactory.newInstance();
      var transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      var source = new DOMSource(doc);
      var sw = new StringWriter();
      var result = new StreamResult(sw);
      transformer.transform(source, result);
      return sw.toString();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
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
          alignIdFor(normalizeHtml(comparison)), alignIdFor(normalizeHtml(res)));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
