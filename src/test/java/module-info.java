module ch.digitalfondue.mjml4j {
  requires ch.digitalfondue.jfiveparse;
  requires java.xml;

  exports ch.digitalfondue.mjml4j;

  requires ph.css;
  requires org.jsoup;

  // Test extensions
  requires org.junit.jupiter.api;
  requires org.junit.jupiter.params;

  opens ch.digitalfondue.mjml4j to
      org.junit.platform.commons;

  exports ch.digitalfondue.mjml4j.testutils to
      org.junit.platform.commons;
}
