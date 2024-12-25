package ch.digitalfondue.mjml4j;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.io.IOAccess;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

class Helpers {

    static String beautifyHtml(String html) throws IOException {
        System.getProperties().setProperty("polyglot.engine.WarnInterpreterOnly", "false");

        var options = new HashMap<String, String>();
        // Enable CommonJS experimental support.
        options.put("js.commonjs-require", "true");
        options.put("js.commonjs-require-cwd", "node_modules/");

        try (Context context = Context.newBuilder("js")
                .allowExperimentalOptions(true)
                .allowIO(IOAccess.ALL)
                .options(options)
                .build()) {
            context.eval(Source.newBuilder("js", new File("node_modules/js-beautify/js/index.js")).build());
            context.getBindings("js").putMember("input", html);
            var res = context.eval(Source.newBuilder("js", """
                        require('js-beautify').html(input, {
                            indent_size: 2,
                            wrap_attributes_indent_size: 2,
                            max_preserve_newline: 0,
                            preserve_newlines: false,
                            end_with_newline: true,
                        });
                    """, "test.mjs").build());
            return res.asString();
        }
    }



    static String simplifyBrTags(String input) {
        return input.replaceAll("<br\s*/>", "<br>");
    }

    // align all values defined in ids -> mjml use random strings, for coherence reason we find all id="...." and replace them with
    // a sequence. this allows to align id=".." and for=".."
    static String alignIdFor(String input) {
        var findIds = Pattern.compile("id=\"[^\"]*([0-9a-f]{16})[^\"]*\"", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
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

    static void testTemplate(String name) {
        testTemplate(name, new Mjml4j.FileSystemResolver(Path.of("data")));
    }

    static void testTemplate(String name, Mjml4j.IncludeResolver resolver) {
        try {
            var template = Files.readString(new File("data/" + name + ".mjml").toPath(), StandardCharsets.UTF_8);
            var conf = new Mjml4j.Configuration("und", Mjml4j.TextDirection.AUTO, resolver);
            var res = Mjml4j.render(template, conf);
            var comparison = Files.readString(new File("data/" + name + ".html").toPath(), StandardCharsets.UTF_8);
            Assertions.assertEquals(simplifyBrTags(alignIdFor(beautifyHtml(comparison))), alignIdFor(beautifyHtml(res)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
