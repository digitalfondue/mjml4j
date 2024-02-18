package ch.digitalfondue.mjml4j;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

/**
 * Unit test for simple App.
 */
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
    void testSocial() {
        testTemplate("social");
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
    void testRawSpecial() {
        testTemplate("raw-special");
    }
}
