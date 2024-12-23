package ch.digitalfondue.mjml4j;

import org.junit.jupiter.api.Disabled;
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

    @Test
    void testAmario() {
        testTemplate("amario");
    }

    @Test
    void testArturia() {
        testTemplate("arturia");
    }

    @Test
    void testBasic() {
        testTemplate("basic");
    }

    @Test
    void testBlackFriday() {
        testTemplate("black-friday");
    }

    @Test
    void testCard() {
        testTemplate("card");
    }

    @Test
    void testChristmas() {
        testTemplate("christmas");
    }

    @Test
    void testNewsletter() {
        testTemplate("newsletter");
    }

    @Test
    void testOnepage() {
        testTemplate("onepage");
    }

    @Test
    void testProof() {
        testTemplate("proof");
    }

    @Test
    void testRacoon() {
        testTemplate("racoon");
    }

    @Test
    void testReactivationEmail() {
        testTemplate("reactivation-email");
    }

    @Test
    void testRealEstate() {
        testTemplate("real-estate");
    }

    @Test
    void testReceiptEmail() {
        testTemplate("receipt-email");
    }

    @Test
    void testReferralEmail() {
        testTemplate("referral-email");
    }

    @Test
    void testSpheroDroids() {
        testTemplate("sphero-droids");
    }

    @Test
    void testIncludeHtml() {
        testTemplate("include-type-html");
    }

    @Test
    void testIncludeCss() {
        testTemplate("include-type-css");
    }

    // imported from issue https://github.com/SebastianStehle/mjml-net/issues/177
    @Test
    @Disabled
    void testIncludeIndex() {
        testTemplate("include-index");
    }

    @Test
    @Disabled
    void testIncludeAbout() {
        testTemplate("include-about");
    }

}
