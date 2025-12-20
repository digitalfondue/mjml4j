package ch.digitalfondue.mjml4j;

record LocalContext(
    String accordionFontFamily, String elementFontFamily, String thumbnails, String gap) {
  LocalContext() {
    this(null, null, null, null);
  }

  LocalContext withAccordionFontFamily(String accordionFontFamily) {
    return new LocalContext(accordionFontFamily, elementFontFamily, thumbnails, gap);
  }

  LocalContext withElementFontFamily(String elementFontFamily) {
    return new LocalContext(accordionFontFamily, elementFontFamily, thumbnails, gap);
  }

  LocalContext withThumbnails(String thumbnails) {
    return new LocalContext(accordionFontFamily, elementFontFamily, thumbnails, gap);
  }

  LocalContext withGap(String gap) {
    return new LocalContext(accordionFontFamily, elementFontFamily, thumbnails, gap);
  }
}
