package ch.digitalfondue.mjml4j.testutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.support.ParameterDeclarations;

public class MjmlListArgumentsProvider extends AnnotationBasedArgumentsProvider<MjmlDirectory> {
  @Override
  protected Stream<? extends Arguments> provideArguments(
      ParameterDeclarations parameters, ExtensionContext context, MjmlDirectory annotation) {
    try {
      return Files.list(Path.of("data", annotation.value()))
          .filter(p -> p.toString().endsWith(".mjml"))
          .map(p -> p.getFileName().toString().replace(".mjml", ""))
          .map(Arguments::of);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
