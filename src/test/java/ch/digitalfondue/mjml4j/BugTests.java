package ch.digitalfondue.mjml4j;

import org.junit.jupiter.api.Test;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

class BugTests {
    
    // see https://github.com/digitalfondue/mjml4j/issues/7
    @Test
    void checkMjSocialNpe() {
        testTemplate("bug-mj-social");
    }

    // imported from https://github.com/FelixSchwarz/mjml-python/issues/51
    @Test
    void checkMjAttributeBody() {
        testTemplate("mj-attribute-body");
    }

    // imported from https://github.com/SebastianStehle/mjml-net/issues/210
    @Test
    void checkMjClass() {
        testTemplate("mj-class-check");
    }
}
