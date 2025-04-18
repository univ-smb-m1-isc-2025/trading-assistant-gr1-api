package me.trading_assistant.api.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CandleDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Données de test
        long timestamp = 1672531200L; // Correspond à 2023-01-01T00:00:00Z
        double open = 100.0;
        double high = 110.0;
        double low = 90.0;
        double close = 105.0;

        // Création de l'objet CandleDTO
        CandleDTO candle = new CandleDTO(timestamp, open, high, low, close);

        // Vérifications
        assertEquals(timestamp, candle.getTimestamp());
        assertEquals("2023-01-01T01:00", candle.getDate()); // Vérifiez le fuseau horaire local
        assertEquals(open, candle.getOpen());
        assertEquals(high, candle.getHigh());
        assertEquals(low, candle.getLow());
        assertEquals(close, candle.getClose());
    }

    @Test
    void testSetPatterns() {
        // Données de test
        List<String> patterns = List.of("Bullish Engulfing", "Hammer");

        // Création de l'objet CandleDTO
        CandleDTO candle = new CandleDTO(1672531200L, 100.0, 110.0, 90.0, 105.0);

        // Définir les patterns
        candle.setPatterns(patterns);

        // Vérifications
        assertNotNull(candle.getPatterns());
        assertEquals(2, candle.getPatterns().size());
        assertTrue(candle.getPatterns().contains("Bullish Engulfing"));
        assertTrue(candle.getPatterns().contains("Hammer"));
    }

    @Test
    void testInvalidTimestamp() {
        // Vérifier qu'une exception est levée pour un timestamp invalide
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CandleDTO("invalid", 100.0, 110.0, 90.0, 105.0);
        });

        // Vérification du message d'erreur
        assertEquals("Le timestamp doit être de type Long ou Integer", exception.getMessage());
    }
}