package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlertTest {

    @Test
    void testSettersAndGetters() {
        // Création d'un objet UserAlert
        UserAlert userAlert = new UserAlert();

        // Définir les valeurs
        userAlert.setId(1L);
        userAlert.setSymbol("AAPL");
        userAlert.setAlertType("price_variation");
        userAlert.setThreshold(5.0);
        userAlert.setDays(10);
        userAlert.setPattern("Bullish Engulfing");
        userAlert.setPriceLevel(150.0);

        // Vérifications
        assertEquals(1L, userAlert.getId());
        assertEquals("AAPL", userAlert.getSymbol());
        assertEquals("price_variation", userAlert.getAlertType());
        assertEquals(5.0, userAlert.getThreshold());
        assertEquals(10, userAlert.getDays());
        assertEquals("Bullish Engulfing", userAlert.getPattern());
        assertEquals(150.0, userAlert.getPriceLevel());
    }

    @Test
    void testUserAssociation() {
        // Création d'un objet UserAlert
        UserAlert userAlert = new UserAlert();

        // Création d'un objet Account
        Account account = new Account();
        account.setId(1L);
        account.setEmail("test@example.com");
        account.setFirstname("John");
        account.setLastname("Doe");

        // Associer l'utilisateur à l'alerte
        userAlert.setUser(account);

        // Vérifications
        assertNotNull(userAlert.getUser());
        assertEquals(1L, userAlert.getUser().getId());
        assertEquals("test@example.com", userAlert.getUser().getEmail());
    }
}