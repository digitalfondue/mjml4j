package ch.digitalfondue.mjml4j;

import java.util.LinkedHashMap;

class CssStyleLibraries {

    private final LinkedHashMap<String, LinkedHashMap<String, String>> libraries = new LinkedHashMap<>();

    void add(String libraryName, LinkedHashMap<String, String> libraryStyles) {
        libraries.put(libraryName, libraryStyles);
    }

    LinkedHashMap<String, String> getStyleLibrary(String libraryName) {
        if (libraries.containsKey(libraryName)) {
            return libraries.get(libraryName);
        }

        var toAdd = new LinkedHashMap<String, String>();
        add(libraryName, toAdd);

        return toAdd;
    }
}
