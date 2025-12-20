package ch.digitalfondue.mjml4j;

import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileSystemResolverTest {

  @Test
  void checkRelativeResolve() {
    var resolver = new Mjml4j.FileSystemResolver(Path.of("/base/"));
    Assertions.assertEquals("/base/test.mjml", resolver.resolvePath("test.mjml", null));
    Assertions.assertEquals(
        "/base/include/test.mjml", resolver.resolvePath("include/test.mjml", "/base/test.mjml"));
    Assertions.assertEquals(
        "/base/back.mjml", resolver.resolvePath("../back.mjml", "/base/include/test.mjml"));
  }

  @Test
  void checkRelativeResolveBaseRelative() {
    var base = Path.of("base/");
    var resolver = new Mjml4j.FileSystemResolver(base);
    var baseAbsolutePath = base.toAbsolutePath();
    Assertions.assertEquals(
        baseAbsolutePath + "/test.mjml", resolver.resolvePath("test.mjml", null));
    Assertions.assertEquals(
        baseAbsolutePath + "/base/include/test.mjml",
        resolver.resolvePath("include/test.mjml", baseAbsolutePath + "/base/test.mjml"));
    Assertions.assertEquals(
        baseAbsolutePath + "/base/back.mjml",
        resolver.resolvePath("../back.mjml", baseAbsolutePath + "/base/include/test.mjml"));
  }

  @Test
  void checkAbsoluteResolve() {
    var resolver = new Mjml4j.FileSystemResolver(Path.of("/base/"));
    Assertions.assertEquals("/base/test.mjml", resolver.resolvePath("/test.mjml", null));
    Assertions.assertEquals(
        "/base/include/include2/test.mjml",
        resolver.resolvePath("/include/include2/test.mjml", "/base/include/hello.mjml"));
  }

  @Test
  void checkAbsoluteResolveBaseRelative() {
    var base = Path.of("base/");
    var resolver = new Mjml4j.FileSystemResolver(base);
    var baseAbsolutePath = base.toAbsolutePath();
    Assertions.assertEquals(
        baseAbsolutePath + "/test.mjml", resolver.resolvePath("/test.mjml", null));
    Assertions.assertEquals(
        baseAbsolutePath + "/include/include2/test.mjml",
        resolver.resolvePath(
            "/include/include2/test.mjml", baseAbsolutePath + "/base/include/hello.mjml"));
  }

  @Test
  void checkOutsideOfBasePath() {
    var resolver = new Mjml4j.FileSystemResolver(Path.of("/base/"));
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          resolver.resolvePath("../test.mjml", null);
        });

    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          resolver.resolvePath("../../../../../test.mjml", "/base/include/test.mjml");
        });
  }
}
