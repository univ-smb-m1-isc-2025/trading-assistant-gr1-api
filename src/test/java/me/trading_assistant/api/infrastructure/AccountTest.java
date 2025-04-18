package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testAccountSettersAndGetters() {
        // Création d'un objet Account
        Account account = new Account();

        // Définir les valeurs
        account.setId(1L);
        account.setEmail("test@example.com");
        account.setFirstname("John");
        account.setLastname("Doe");
        account.setPassword("123456");
        account.setPhone("123456789");

        // Vérifications
        assertEquals(1L, account.getId());
        assertEquals("test@example.com", account.getEmail());
        assertEquals("John", account.getFirstname());
        assertEquals("Doe", account.getLastname());
        assertEquals("123456", account.getPassword());
        assertEquals("123456789", account.getPhone());
    }

    @Test
    void testAccountAlerts() {
        // Création d'un objet Account
        Account account = new Account();

        // Création d'alertes fictives
        UserAlert alert1 = new UserAlert();
        alert1.setId(1L);
        alert1.setSymbol("AAPL");
        alert1.setAlertType("price_variation");

        UserAlert alert2 = new UserAlert();
        alert2.setId(2L);
        alert2.setSymbol("TSLA");
        alert2.setAlertType("moving_average_cross");

        // Ajouter les alertes à l'utilisateur
        account.setAlerts(List.of(alert1, alert2));

        // Vérifications
        assertNotNull(account.getAlerts());
        assertEquals(2, account.getAlerts().size());
        assertEquals("AAPL", account.getAlerts().get(0).getSymbol());
        assertEquals("TSLA", account.getAlerts().get(1).getSymbol());
    }
}