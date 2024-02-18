package ch.digitalfondue.mjml4j;



import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static ch.digitalfondue.mjml4j.Helpers.testTemplate;

class AlfioMjmlTests {

    @Test
    @Disabled
    void testConfirmationEmailForOrganizerHtml() {
        testTemplate("alfio/confirmation-email-for-organizer-html");
    }


    @Test
    @Disabled
    void testConfirmationEmailHtml() {
        testTemplate("alfio/confirmation-email-html");
    }

    @Test
    @Disabled
    void testConfirmationEmailSubscriptionHtml() {
        testTemplate("alfio/confirmation-email-subscription-html");
    }

    @Test
    @Disabled
    void testCustomMessageHtml() {
        testTemplate("alfio/custom-message-html");
    }

    @Test
    @Disabled
    void testofflineReservationExpiringEmailForOrganizerHtml() {
        testTemplate("alfio/offline-reservation-expiring-email-for-organizer-html");
    }

    @Test
    @Disabled
    void testTicketEmailHtml() {
        testTemplate("alfio/ticket-email-html");
    }

    @Test
    @Disabled
    void testTicketEmailOnlineHtml() {
        testTemplate("alfio/ticket-email-online-html");
    }
}
