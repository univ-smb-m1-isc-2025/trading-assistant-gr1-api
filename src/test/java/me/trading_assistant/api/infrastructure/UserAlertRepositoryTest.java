/*
package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserAlertRepositoryTest {

    @Autowired
    private UserAlertRepository userAlertRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAndFindByUserId() {
        // Créer un utilisateur
        Account user = new Account();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPassword("123456");
        user.setPhone("123456789");
        Account savedUser = accountRepository.save(user);

        // Créer une alerte
        UserAlert alert = new UserAlert();
        alert.setUser(savedUser);
        alert.setSymbol("AAPL");
        alert.setAlertType("price_variation");
        alert.setThreshold(5.0);
        userAlertRepository.save(alert);

        // Rechercher les alertes par userId
        List<UserAlert> alerts = userAlertRepository.findByUserId(savedUser.getId());

        // Vérifications
        assertEquals(1, alerts.size());
        assertEquals("AAPL", alerts.get(0).getSymbol());
        assertEquals("price_variation", alerts.get(0).getAlertType());
        assertEquals(5.0, alerts.get(0).getThreshold());
    }

    @Test
    void testFindBySymbol() {
        // Créer un utilisateur
        Account user = new Account();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPassword("123456");
        user.setPhone("123456789");
        Account savedUser = accountRepository.save(user);

        // Créer une alerte
        UserAlert alert = new UserAlert();
        alert.setUser(savedUser);
        alert.setSymbol("TSLA");
        alert.setAlertType("moving_average_cross");
        userAlertRepository.save(alert);

        // Rechercher les alertes par symbole
        List<UserAlert> alerts = userAlertRepository.findBySymbol("TSLA");

        // Vérifications
        assertEquals(1, alerts.size());
        assertEquals("TSLA", alerts.get(0).getSymbol());
        assertEquals("moving_average_cross", alerts.get(0).getAlertType());
    }

    @Test
    void testDeleteById() {
        // Créer un utilisateur
        Account user = new Account();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPassword("123456");
        user.setPhone("123456789");
        Account savedUser = accountRepository.save(user);

        // Créer une alerte
        UserAlert alert = new UserAlert();
        alert.setUser(savedUser);
        alert.setSymbol("AAPL");
        alert.setAlertType("price_variation");
        UserAlert savedAlert = userAlertRepository.save(alert);

        // Supprimer l'alerte
        userAlertRepository.deleteById(savedAlert.getId());

        // Vérifier que l'alerte n'existe plus
        Optional<UserAlert> foundAlert = userAlertRepository.findById(savedAlert.getId());
        assertFalse(foundAlert.isPresent());
    }

    @Test
    void testFindByUserIdWithMultipleAlerts() {
        // Créer un utilisateur
        Account user = new Account();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPassword("123456");
        user.setPhone("123456789");
        Account savedUser = accountRepository.save(user);

        // Créer plusieurs alertes
        UserAlert alert1 = new UserAlert();
        alert1.setUser(savedUser);
        alert1.setSymbol("AAPL");
        alert1.setAlertType("price_variation");
        alert1.setThreshold(5.0);

        UserAlert alert2 = new UserAlert();
        alert2.setUser(savedUser);
        alert2.setSymbol("TSLA");
        alert2.setAlertType("moving_average_cross");

        userAlertRepository.save(alert1);
        userAlertRepository.save(alert2);

        // Rechercher les alertes par userId
        List<UserAlert> alerts = userAlertRepository.findByUserId(savedUser.getId());

        // Vérifications
        assertEquals(2, alerts.size());
        assertTrue(alerts.stream().anyMatch(alert -> alert.getSymbol().equals("AAPL")));
        assertTrue(alerts.stream().anyMatch(alert -> alert.getSymbol().equals("TSLA")));
    }
}*/
