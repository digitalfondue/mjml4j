package ch.digitalfondue.mjml4j;

import org.w3c.dom.Document;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class GlobalContext {

    final Document document;
    String title = "";
    String previewText = "";
    String breakpoint = "480px";
    String containerWidth = "600px";
    String backgroundColor = "";
    final String language;
    final String dir;
    //
    final Mjml4j.IncludeResolver includeResolver;
    final ArrayDeque<String> currentResourcePaths = new ArrayDeque<>();
    final ArrayDeque<MjmlComponent.MjmlRootComponent> rootComponents = new ArrayDeque<>();
    //

    final LinkedHashMap<String, String> fonts = Utils.mapOf(
            "Open Sans", "https://fonts.googleapis.com/css?family=Open+Sans:300,400,500,700",
            "Droid Sans", "https://fonts.googleapis.com/css?family=Droid+Sans:300,400,500,700",
            "Lato", "https://fonts.googleapis.com/css?family=Lato:300,400,500,700",
            "Roboto", "https://fonts.googleapis.com/css?family=Roboto:300,400,500,700",
            "Ubuntu", "https://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700"
    );
    final LinkedHashMap<String, String> headStyle = new LinkedHashMap<>();
    final List<String> componentsHeadStyle = new ArrayList<>();

    final LinkedHashMap<String, String> mediaQueries = new LinkedHashMap<>();

    final LinkedHashMap<String, LinkedHashMap<String, String>> attributesByName = new LinkedHashMap<>();
    final LinkedHashMap<String, LinkedHashMap<String, String>> attributesByClass = new LinkedHashMap<>();

    final List<String> styles = new ArrayList<>();
    final List<String> inlineStyles = new ArrayList<>();

    GlobalContext(Document document, Mjml4j.Configuration configuration) {
        this.document = document;
        this.dir = configuration.dir().value();
        this.language = configuration.language();

        //
        this.includeResolver = configuration.includeResolver();
        //
    }

    void addFont(String name, String href) {
        if (fonts.containsKey(name)) {
            var hrefCurrent = mediaQueries.get(name);
            if (hrefCurrent != null && hrefCurrent.equalsIgnoreCase(href))
                return;
        }
        fonts.put(name, href);
    }

    void addStyle(String css, boolean inline) {
        if (css == null || css.isBlank())
            return;

        if (inline) {
            inlineStyles.add(css);
        } else {
            styles.add(css);
        }
    }


    void addHeadStyle(String componentName, String css) {
        if (Utils.isNullOrWhiteSpace(componentName) || Utils.isNullOrWhiteSpace(css))
            return;
        if (headStyle.containsKey(componentName))
            return;
        headStyle.put(componentName, css);
    }

    void addComponentHeadStyle(String css) {
        if (Utils.isNullOrWhiteSpace(css))
            return;
        componentsHeadStyle.add(css);
    }

    void addMediaQuery(String className, CssUnitParser.CssParsedUnit cssParsedUnit) {
        var mediaQuery = "{\n        width: " + cssParsedUnit + " !important;\n        max-width: " + cssParsedUnit + ";\n      }";

        if (mediaQueries.containsKey(className)) {
            var mediaQueryCurrent = mediaQueries.get(className);

            if (Utils.equalsIgnoreCase(mediaQueryCurrent, mediaQuery))
                return;
        }
        mediaQueries.put(className, mediaQuery);
    }

    void setClassAttribute(String name, String className, String value) {
        if (!attributesByClass.containsKey(className)) {
            attributesByClass.put(className, new LinkedHashMap<>());
        }
        attributesByClass.get(className).put(name, value);
    }

    void setTypeAttribute(String name, String type, String value) {
        if (!attributesByName.containsKey(name)) {
            attributesByName.put(name, new LinkedHashMap<>());
        }
        attributesByName.get(name).put(type, value);
    }
}
