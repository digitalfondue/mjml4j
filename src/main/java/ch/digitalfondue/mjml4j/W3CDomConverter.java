package ch.digitalfondue.mjml4j;

import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomConverter {

    public static String toHtmlString(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            // Add the doctype to the output
            StringWriter writer = new StringWriter();
            writer.write("<!doctype html>");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (TransformerException e) {
            System.err.println("Error converting Document to String: " + e.getMessage());
            return null;
        }
    }
}
