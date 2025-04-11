package me.trading_assistant.api.application;

import me.trading_assistant.api.domain.CandleDTO;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CandlePatternService {

    public Map<String, Object> detectPatterns(Map<String, Object> chartData) {
        List<CandleDTO> candles = extractCandles(chartData);
        Map<Long, List<String>> patternsByTimestamp = new HashMap<>();

        // Détection des patterns
        for (int i = 0; i < candles.size(); i++) {
            CandleDTO current = candles.get(i);
            CandleDTO previous = i > 0 ? candles.get(i-1) : null;
            CandleDTO twoBefore = i > 1 ? candles.get(i-2) : null;

            List<String> patterns = new ArrayList<>();

            // Patterns qui nécessitent une bougie précédente
            if (previous != null) {
                // Bullish Engulfing
                if (isBullishEngulfing(previous, current)) {
                    patterns.add("Bullish Engulfing");
                }

                // Bearish Engulfing
                if (isBearishEngulfing(previous, current)) {
                    patterns.add("Bearish Engulfing");
                }
            }

            // Patterns qui nécessitent deux bougies précédentes
            if (twoBefore != null && previous != null) {
                // Morning Star
                if (isMorningStar(twoBefore, previous, current)) {
                    patterns.add("Morning Star");
                }

                // Evening Star
                if (isEveningStar(twoBefore, previous, current)) {
                    patterns.add("Evening Star");
                }
            }

            // Patterns qui ne nécessitent que la bougie courante
            // Hammer
            if (isHammer(current)) {
                patterns.add("Hammer");
            }

            // Shooting Star
            if (isShootingStar(current)) {
                patterns.add("Shooting Star");
            }

            // Dragonfly Doji
            if (isDragonflyDoji(current)) {
                patterns.add("Dragonfly Doji");
            }

            // Gravestone Doji
            if (isGravestoneDoji(current)) {
                patterns.add("Gravestone Doji");
            }

            // Small-Ranged Candle
            if (isSmallRangedCandle(current)) {
                patterns.add("Small-Ranged Candle");
            }

            // Doji
            if (isDoji(current)) {
                patterns.add("Doji");
            }

            // Small-Bodied Candle
            if (isSmallBodiedCandle(current)) {
                patterns.add("Small-Bodied Candle");
            }

            if (!patterns.isEmpty()) {
                patternsByTimestamp.put(current.getTimestamp(), patterns);
            }
        }

        // Ajout des patterns aux bougies
        for (CandleDTO candle : candles) {
            candle.setPatterns(patternsByTimestamp.getOrDefault(candle.getTimestamp(), new ArrayList<>()));
        }

        // Création de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("candles", candles);
        response.put("patterns", patternsByTimestamp);

        return response;
    }

    private List<CandleDTO> extractCandles(Map<String, Object> chartData) {
        List<CandleDTO> candles = new ArrayList<>();
        
        if (chartData == null || !chartData.containsKey("chart")) {
            return candles;
        }

        Map<String, Object> chart = (Map<String, Object>) chartData.get("chart");
        if (!chart.containsKey("result")) {
            return candles;
        }

        List<Object> results = (List<Object>) chart.get("result");
        if (results == null || results.isEmpty()) {
            return candles;
        }

        Map<String, Object> result = (Map<String, Object>) results.get(0);
        List<Long> timestamps = (List<Long>) result.get("timestamp");
        if (timestamps == null || timestamps.isEmpty()) {
            return candles;
        }

        Map<String, Object> indicators = (Map<String, Object>) result.get("indicators");
        if (indicators == null) {
            return candles;
        }

        List<Object> quotes = (List<Object>) indicators.get("quote");
        if (quotes == null || quotes.isEmpty()) {
            return candles;
        }

        Map<String, Object> quote = (Map<String, Object>) quotes.get(0);
        if (quote.isEmpty()) {
            return candles;
        }

        List<Double> opens = (List<Double>) quote.get("open");
        List<Double> highs = (List<Double>) quote.get("high");
        List<Double> lows = (List<Double>) quote.get("low");
        List<Double> closes = (List<Double>) quote.get("close");

        if (opens == null || highs == null || lows == null || closes == null) {
            return candles;
        }

        int size = timestamps.size();
        for (int i = 0; i < size; i++) {
            CandleDTO candle = new CandleDTO(
                timestamps.get(i),
                opens.get(i),
                highs.get(i),
                lows.get(i),
                closes.get(i)
            );
            candles.add(candle);
        }

        return candles;
    }

    private boolean isBullishEngulfing(CandleDTO previous, CandleDTO current) {
        return previous.getClose() < previous.getOpen() && // Previous candle is bearish
               current.getOpen() < previous.getClose() && // Current candle opens below previous close
               current.getClose() > previous.getOpen();   // Current candle closes above previous open
    }

    private boolean isBearishEngulfing(CandleDTO previous, CandleDTO current) {
        return previous.getClose() > previous.getOpen() && // Previous candle is bullish
               current.getOpen() > previous.getClose() && // Current candle opens above previous close
               current.getClose() < previous.getOpen();   // Current candle closes below previous open
    }

    private boolean isMorningStar(CandleDTO twoBefore, CandleDTO previous, CandleDTO current) {
        return twoBefore.getClose() < twoBefore.getOpen() && // First candle is bearish
               isSmallBodiedCandle(previous) &&              // Second candle is small-bodied
               current.getClose() > current.getOpen() &&     // Third candle is bullish
               current.getOpen() > previous.getClose() &&    // Third candle opens above second close
               current.getClose() > twoBefore.getOpen();     // Third candle closes above first open
    }

    private boolean isEveningStar(CandleDTO twoBefore, CandleDTO previous, CandleDTO current) {
        return twoBefore.getClose() > twoBefore.getOpen() && // First candle is bullish
               isSmallBodiedCandle(previous) &&              // Second candle is small-bodied
               current.getClose() < current.getOpen() &&     // Third candle is bearish
               current.getOpen() < previous.getClose() &&    // Third candle opens below second close
               current.getClose() < twoBefore.getOpen();     // Third candle closes below first open
    }

    private boolean isHammer(CandleDTO candle) {
        double bodySize = Math.abs(candle.getClose() - candle.getOpen());
        double lowerShadow = Math.min(candle.getOpen(), candle.getClose()) - candle.getLow();
        double upperShadow = candle.getHigh() - Math.max(candle.getOpen(), candle.getClose());

        return lowerShadow > bodySize * 2 && upperShadow < bodySize;
    }

    private boolean isShootingStar(CandleDTO candle) {
        double bodySize = Math.abs(candle.getClose() - candle.getOpen());
        double lowerShadow = Math.min(candle.getOpen(), candle.getClose()) - candle.getLow();
        double upperShadow = candle.getHigh() - Math.max(candle.getOpen(), candle.getClose());

        return upperShadow > bodySize * 2 && lowerShadow < bodySize;
    }

    private boolean isDragonflyDoji(CandleDTO candle) {
        if (!isDoji(candle)) return false;

        double lowerShadow = Math.min(candle.getOpen(), candle.getClose()) - candle.getLow();
        double upperShadow = candle.getHigh() - Math.max(candle.getOpen(), candle.getClose());

        return lowerShadow > upperShadow * 2;
    }

    private boolean isGravestoneDoji(CandleDTO candle) {
        if (!isDoji(candle)) return false;

        double lowerShadow = Math.min(candle.getOpen(), candle.getClose()) - candle.getLow();
        double upperShadow = candle.getHigh() - Math.max(candle.getOpen(), candle.getClose());

        return upperShadow > lowerShadow * 2;
    }

    private boolean isSmallRangedCandle(CandleDTO candle) {
        double totalSize = candle.getHigh() - candle.getLow();
        double averageRange = calculateAverageRange(candle);
        return totalSize < averageRange * 0.5;
    }

    private boolean isDoji(CandleDTO candle) {
        double bodySize = Math.abs(candle.getClose() - candle.getOpen());
        double totalSize = candle.getHigh() - candle.getLow();
        return bodySize <= totalSize * 0.1;
    }

    private boolean isSmallBodiedCandle(CandleDTO candle) {
        double bodySize = Math.abs(candle.getClose() - candle.getOpen());
        double totalSize = candle.getHigh() - candle.getLow();
        return bodySize < totalSize * 0.3;
    }

    private double calculateAverageRange(CandleDTO candle) {
        // Cette méthode devrait calculer la moyenne des ranges sur une période donnée
        // Pour simplifier, nous utilisons une valeur fixe ici
        return 5.0; // À remplacer par un calcul réel
    }
} 