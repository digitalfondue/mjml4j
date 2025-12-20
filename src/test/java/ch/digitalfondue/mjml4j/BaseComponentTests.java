package ch.digitalfondue.mjml4j;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

import org.junit.jupiter.api.Test;

/** Unit test for base cases. */
class BaseComponentTests {

  @Test
  void testBase() {
    testTemplate("base");
  }

  @Test
  void testImage() {
    testTemplate("image");
  }

  @Test
  void testDivider() {
    testTemplate("divider");
  }

  @Test
  void testButton() {
    testTemplate("button");
  }

  @Test
  void testHeroDivider() {
    testTemplate("hero-divider");
  }

  @Test
  void testSpacer() {
    testTemplate("spacer");
  }

  @Test
  void testPreview() {
    testTemplate("preview");
  }

  @Test
  void testHeroFluidHeightButton() {
    testTemplate("hero-fluid-height-button");
  }

  @Test
  void testSection() {
    testTemplate("section");
  }

  @Test
  void testSectionColumnColumnDivider() {
    testTemplate("section-column-column-divider");
  }

  @Test
  void testSectionColumnColumnSectionColumnColumnDivider() {
    testTemplate("section-column-column-section-column-column-divider");
  }

  @Test
  void testSectionGroupColumnText() {
    testTemplate("section-group-column-text");
  }

  @Test
  void testNavbar() {
    testTemplate("navbar");
  }

  @Test
  void testNavbarIcoPadding() {
    testTemplate("navbar-ico-padding");
  }

  @Test
  void testSocial() {
    testTemplate("social");
  }

  @Test
  void testSocialAlign() {
    testTemplate("social-align");
  }

  @Test
  void testSocialIconHeight() {
    testTemplate("social-icon-height");
  }

  @Test
  void testText() {
    testTemplate("text");
  }

  @Test
  void testFont() {
    testTemplate("font");
  }

  @Test
  void testBreakpoint() {
    testTemplate("breakpoint");
  }

  @Test
  void testTitle() {
    testTemplate("title");
  }

  @Test
  void testStyle() {
    testTemplate("style");
  }

  @Test
  void testGroup() {
    testTemplate("group");
  }

  @Test
  void testWrapper() {
    testTemplate("wrapper");
  }

  @Test
  void testWrapperBorderRadius() {
    testTemplate("wrapper-border-radius");
  }

  @Test
  void testWrapperGap() {
    testTemplate("wrapper-gap");
  }

  @Test
  void testAttributes() {
    testTemplate("attributes");
  }

  @Test
  void testSectionWithSize() {
    testTemplate("test-section-with-size");
  }

  @Test
  void testTable() {
    testTemplate("table");
  }

  @Test
  void testTableCellSpacing() {
    testTemplate("table-cellspacing");
  }

  @Test
  void testTableWidth() {
    testTemplate("tableWidth");
  }

  @Test
  void testAccordion() {
    testTemplate("accordion");
  }

  @Test
  void testAccordionAttrs() {
    testTemplate("accordion-attrs");
  }

  @Test
  void testAccordionAttrsAll() {
    testTemplate("accordion-attrs-all");
  }

  @Test
  void testAccordionFontFamily() {
    testTemplate("accordion-font-family");
  }

  @Test
  void testAccordionPadding() {
    testTemplate("accordion-padding");
  }

  @Test
  void testAccordionTitleFontWeight() {
    testTemplate("accordionTitle-fontWeight");
  }

  @Test
  void colorText() {
    testTemplate("color-text");
  }

  @Test
  void testAccordionPermutation() {
    testTemplate("accordion-permutation");
  }

  @Test
  void testCarousel() {
    testTemplate("carousel");
  }

  @Test
  void testCarouselHoverSupported() {
    testTemplate("carousel-hoverSupported");
  }

  @Test
  void testRawSpecial() {
    testTemplate("raw-special");
  }

  @Test
  void testColumnGutter() {
    testTemplate("column-gutter");
  }

  @Test
  void testColumnBorderRadius() {
    testTemplate("column-border-radius");
  }

  @Test
  void testSocialVertical() {
    testTemplate("social-vertical");
  }

  @Test
  void testSectionBackgroundImage() {
    testTemplate("section-background-image");
  }

  @Test
  void testEntitiesEscaping() {
    testTemplate("entities-escaping");
  }

  @Test
  void testSectionMisc() {
    testTemplate("mj-section-with-background");
    testTemplate("mj-section-with-background-url");
    testTemplate("mj-section-with-css-class");
    testTemplate("mj-section-with-full-width");
    testTemplate("mj-section-with-mj-class");
  }

  @Test
  void testSectionColumnPrecision() {
    testTemplate("section-column-precision");
  }

  @Test
  void testSectionColumnPrecision2() {
    testTemplate("mj-section-column-precision-2");
  }

  @Test
  void testEmptyColumnEmptyNodeSocial() {
    testTemplate("empty-column-empty-node-social");
  }

  @Test
  void testBodyWidthOverride() {
    testTemplate("body-width-override");
  }

  @Test
  void testEmptySection() {
    testTemplate("empty-section");
  }

  @Test
  void testImportHead() {
    testTemplate("import-head");
  }

  @Test
  void test2Import() {
    testTemplate("import-2-import");
  }
}
