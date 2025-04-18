package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.TrademateService;
import me.trading_assistant.api.application.UserAlertService;
import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.UserAlert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserAlertControllerTest {

    private final UserAlertService userAlertService = mock(UserAlertService.class);
    private final TrademateService trademateService = mock(TrademateService.class);
    private final UserAlertController userAlertController = new UserAlertController(userAlertService, trademateService);

    @Test
    void testGetAlertsByUserId() {
        // Mock des données
        Long userId = 1L;
        UserAlert mockAlert = new UserAlert();
        mockAlert.setId(1L);
        mockAlert.setSymbol("AAPL");
        mockAlert.setAlertType("price_variation");
        mockAlert.setThreshold(5.0);

        when(userAlertService.getAlertsByUserId(userId)).thenReturn(List.of(mockAlert));

        // Appeler la méthode
        ResponseEntity<List<UserAlert>> response = userAlertController.getAlertsByUserId(userId);

        // Vérifier les résultats
        assertEquals(1, response.getBody().size());
        assertEquals("AAPL", response.getBody().get(0).getSymbol());
        verify(userAlertService, times(1)).getAlertsByUserId(userId);
    }

    @Test
    void testAddAlert() {
        // Mock des données
        Long userId = 1L;
        Account mockUser = new Account();
        mockUser.setId(userId);

        UserAlert mockAlert = new UserAlert();
        mockAlert.setId(1L);
        mockAlert.setSymbol("AAPL");
        mockAlert.setAlertType("price_variation");

        when(trademateService.getAccountById(userId)).thenReturn(mockUser);
        when(userAlertService.addAlert(mockUser, "AAPL", "price_variation", 5.0, null, null, null)).thenReturn(mockAlert);

        // Appeler la méthode
        ResponseEntity<?> response = userAlertController.addAlert(userId, "AAPL", "price_variation", 5.0, null, null, null);

        // Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAlert, response.getBody());
        verify(userAlertService, times(1)).addAlert(mockUser, "AAPL", "price_variation", 5.0, null, null, null);
    }

    @Test
    void testUpdateAlert() {
        // Mock des données
        Long alertId = 1L;
        UserAlert mockAlert = new UserAlert();
        mockAlert.setId(alertId);
        mockAlert.setSymbol("AAPL");
        mockAlert.setAlertType("price_variation");

        when(userAlertService.updateAlert(alertId, "AAPL", "price_variation", 5.0, null, null, null)).thenReturn(mockAlert);

        // Appeler la méthode
        ResponseEntity<?> response = userAlertController.updateAlert(alertId, "AAPL", "price_variation", 5.0, null, null, null);

        // Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAlert, response.getBody());
        verify(userAlertService, times(1)).updateAlert(alertId, "AAPL", "price_variation", 5.0, null, null, null);
    }

    @Test
    void testRemoveAlert() {
        // Mock des données
        Long alertId = 1L;

        // Appeler la méthode
        ResponseEntity<?> response = userAlertController.removeAlert(alertId);

        // Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alerte supprimée avec succès", response.getBody());
        verify(userAlertService, times(1)).removeAlert(alertId);
    }
}