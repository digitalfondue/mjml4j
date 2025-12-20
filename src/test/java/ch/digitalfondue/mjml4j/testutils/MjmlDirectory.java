package ch.digitalfondue.mjml4j.testutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.provider.ArgumentsSource;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(MjmlListArgumentsProvider.class)
public @interface MjmlDirectory {
  /** The directory to scan for MJML test files. */
  String value();
}
