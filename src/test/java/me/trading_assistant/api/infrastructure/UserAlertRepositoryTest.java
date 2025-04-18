package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAlertRepositoryTest {

    @Mock
    private UserAlertRepository userAlertRepository;

    @InjectMocks
    private UserAlertRepositoryTest userAlertRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserId() {
        // Mock des données
        Long userId = 1L;
        UserAlert mockAlert = new UserAlert();
        mockAlert.setId(1L);
        mockAlert.setSymbol("AAPL");
        mockAlert.setAlertType("price_variation");

        when(userAlertRepository.findByUserId(userId)).thenReturn(List.of(mockAlert));

        // Appeler la méthode
        List<UserAlert> alerts = userAlertRepository.findByUserId(userId);

        // Vérifications
        assertEquals(1, alerts.size());
        assertEquals("AAPL", alerts.get(0).getSymbol());
        verify(userAlertRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindBySymbol() {
        // Mock des données
        String symbol = "TSLA";
        UserAlert mockAlert = new UserAlert();
        mockAlert.setId(1L);
        mockAlert.setSymbol(symbol);
        mockAlert.setAlertType("moving_average_cross");

        when(userAlertRepository.findBySymbol(symbol)).thenReturn(List.of(mockAlert));

        // Appeler la méthode
        List<UserAlert> alerts = userAlertRepository.findBySymbol(symbol);

        // Vérifications
        assertEquals(1, alerts.size());
        assertEquals(symbol, alerts.get(0).getSymbol());
        verify(userAlertRepository, times(1)).findBySymbol(symbol);
    }

    @Test
    void testDeleteById() {
        // Mock de la méthode
        Long alertId = 1L;
        doNothing().when(userAlertRepository).deleteById(alertId);

        // Appeler la méthode
        userAlertRepository.deleteById(alertId);

        // Vérifications
        verify(userAlertRepository, times(1)).deleteById(alertId);
    }

    @Test
    void testSave() {
        // Mock des données
        UserAlert mockAlert = new UserAlert();
        mockAlert.setId(1L);
        mockAlert.setSymbol("AAPL");
        mockAlert.setAlertType("price_variation");

        when(userAlertRepository.save(mockAlert)).thenReturn(mockAlert);

        // Appeler la méthode
        UserAlert savedAlert = userAlertRepository.save(mockAlert);

        // Vérifications
        assertNotNull(savedAlert);
        assertEquals("AAPL", savedAlert.getSymbol());
        verify(userAlertRepository, times(1)).save(mockAlert);
    }
}