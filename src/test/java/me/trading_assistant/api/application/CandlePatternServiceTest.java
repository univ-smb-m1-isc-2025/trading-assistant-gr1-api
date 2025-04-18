package me.trading_assistant.api.application;

import me.trading_assistant.api.domain.CandleDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CandlePatternServiceTest {

    private final CandlePatternService candlePatternService = new CandlePatternService();

    @Test
    void testDetectPatternsWithValidData() {
        // Données de test
        Map<String, Object> chartData = Map.of(
            "chart", Map.of(
                "result", List.of(
                    Map.of(
                        "timestamp", List.of(1672531200L, 1672617600L, 1672704000L),
                        "indicators", Map.of(
                            "quote", List.of(
                                Map.of(
                                    "open", List.of(100.0, 105.0, 102.0),
                                    "high", List.of(110.0, 108.0, 107.0),
                                    "low", List.of(90.0, 100.0, 101.0),
                                    "close", List.of(105.0, 102.0, 103.0)
                                )
                            )
                        )
                    )
                )
            )
        );

        // Appeler la méthode
        Map<String, Object> result = candlePatternService.detectPatterns(chartData);

        // Vérifications
        assertNotNull(result);
        assertTrue(result.containsKey("candles"));
        assertTrue(result.containsKey("patterns"));

        List<CandleDTO> candles = (List<CandleDTO>) result.get("candles");
        assertEquals(3, candles.size());

        Map<Long, List<String>> patterns = (Map<Long, List<String>>) result.get("patterns");
        assertNotNull(patterns);
    }

    @Test
    void testDetectPatternsWithEmptyData() {
        // Données vides
        Map<String, Object> chartData = Map.of();

        // Appeler la méthode
        Map<String, Object> result = candlePatternService.detectPatterns(chartData);

        // Vérifications
        assertNotNull(result);
        assertTrue(result.containsKey("candles"));
        assertTrue(result.containsKey("patterns"));

        List<CandleDTO> candles = (List<CandleDTO>) result.get("candles");
        assertTrue(candles.isEmpty());

        Map<Long, List<String>> patterns = (Map<Long, List<String>>) result.get("patterns");
        assertTrue(patterns.isEmpty());
    }

    @Test
    void testDetectPatternsWithNullData() {
        // Données nulles
        Map<String, Object> chartData = null;

        // Appeler la méthode
        Map<String, Object> result = candlePatternService.detectPatterns(chartData);

        // Vérifications
        assertNotNull(result);
        assertTrue(result.containsKey("candles"));
        assertTrue(result.containsKey("patterns"));

        List<CandleDTO> candles = (List<CandleDTO>) result.get("candles");
        assertTrue(candles.isEmpty());

        Map<Long, List<String>> patterns = (Map<Long, List<String>>) result.get("patterns");
        assertTrue(patterns.isEmpty());
    }
}