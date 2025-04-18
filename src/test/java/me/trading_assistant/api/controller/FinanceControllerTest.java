package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.FinanceService;
import me.trading_assistant.api.application.CandlePatternService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FinanceControllerTest {

    @Test
    void testGetStockChart() {
        // Mock des services
        FinanceService financeService = mock(FinanceService.class);
        CandlePatternService patternService = mock(CandlePatternService.class);

        // Données simulées
        Map<String, Object> mockChartData = Map.of("key", "value");
        when(financeService.getStockChart("AAPL", "1d")).thenReturn(mockChartData);

        // Instancier le contrôleur avec les mocks
        FinanceController controller = new FinanceController(financeService, patternService);

        // Appeler la méthode à tester
        ResponseEntity<?> response = controller.getStockChart("AAPL", "1d");

        // Vérifier les résultats
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockChartData, response.getBody());

        // Vérifier que le service a été appelé
        verify(financeService, times(1)).getStockChart("AAPL", "1d");
    }
}