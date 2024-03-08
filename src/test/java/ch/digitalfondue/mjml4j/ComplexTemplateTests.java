package ch.digitalfondue.mjml4j;

import org.junit.jupiter.api.Test;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

class ComplexTemplateTests {

    @Test
    void testWordly() {
        testTemplate("wordly");
    }

    @Test
    void testTicketshop() {
        testTemplate("ticketshop");
    }

    @Test
    void testWelcomeEmail() {
        testTemplate("welcome-email");
    }

    @Test
    void testSpheroMini() {
        testTemplate("sphero-mini");
    }
}
