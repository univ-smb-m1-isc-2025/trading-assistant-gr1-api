package me.trading_assistant.api.trading;

import java.util.ArrayList;
import java.util.List;

public class CandlePatternDetector {

    public static class Candle {
        double open;
        double close;
        double high;
        double low;

        public Candle(double open, double close, double high, double low) {
            this.open = open;
            this.close = close;
            this.high = high;
            this.low = low;
        }
    }

    public static class Pattern {
        String name;
        int dayIndex;

        public Pattern(String name, int dayIndex) {
            this.name = name;
            this.dayIndex = dayIndex;
        }

        @Override
        public String toString() {
            return "Day " + dayIndex + ": " + name;
        }
    }

    public static List<Pattern> detectPatterns(List<Candle> candles) {
        List<Pattern> patterns = new ArrayList<>();

        for (int i = 0; i < candles.size(); i++) {
            Candle current = candles.get(i);
            Candle previous = i > 0 ? candles.get(i - 1) : null;

            // Doji
            if (Math.abs(current.open - current.close) < 0.1 * (current.high - current.low)) {
                patterns.add(new Pattern("Doji", i));
            }

            // Small-Bodied Candle
            if (Math.abs(current.open - current.close) < 0.2 * (current.high - current.low)) {
                patterns.add(new Pattern("Small-Bodied Candle", i));
            }

            // Small-Ranged Candle
            if ((current.high - current.low) < 0.5 * (current.high + current.low) / 2) {
                patterns.add(new Pattern("Small-Ranged Candle", i));
            }

            // Hammer
            if ((current.high - current.low) > 2 * (current.open - current.close) &&
                    (current.close - current.low) > 2 * (current.high - current.close)) {
                patterns.add(new Pattern("Hammer", i));
            }

            // Dragonfly Doji
            if (Math.abs(current.open - current.close) < 0.1 * (current.high - current.low) &&
                    (current.high - current.close) < 0.1 * (current.high - current.low)) {
                patterns.add(new Pattern("Dragonfly Doji", i));
            }

            // Gravestone Doji
            if (Math.abs(current.open - current.close) < 0.1 * (current.high - current.low) &&
                    (current.close - current.low) < 0.1 * (current.high - current.low)) {
                patterns.add(new Pattern("Gravestone Doji", i));
            }

            // Shooting Star
            if ((current.high - current.low) > 2 * (current.open - current.close) &&
                    (current.high - current.open) > 2 * (current.close - current.low)) {
                patterns.add(new Pattern("Shooting Star", i));
            }

            // Bullish Engulfing
            if (previous != null && current.open < previous.close && current.close > previous.open) {
                patterns.add(new Pattern("Bullish Engulfing", i));
            }

            // Bearish Engulfing
            if (previous != null && current.open > previous.close && current.close < previous.open) {
                patterns.add(new Pattern("Bearish Engulfing", i));
            }

            // Morning Star
            if (i >= 2) {
                Candle beforePrevious = candles.get(i - 2);
                if (beforePrevious.close > beforePrevious.open &&
                        current.close > current.open &&
                        previous.close < previous.open &&
                        current.open > previous.close) {
                    patterns.add(new Pattern("Morning Star", i));
                }
            }

            // Evening Star
            if (i >= 2) {
                Candle beforePrevious = candles.get(i - 2);
                if (beforePrevious.open > beforePrevious.close &&
                        current.open < current.close &&
                        previous.open > previous.close &&
                        current.close < previous.open) {
                    patterns.add(new Pattern("Evening Star", i));
                }
            }
        }

        return patterns;
    }

    public static void main(String[] args) {
        // Exemple de données
        List<Candle> candles = List.of(
                new Candle(166.67, 165.97, 168.65, 163.24),
                new Candle(168.47, 169.00, 169.52, 165.48),
                new Candle(167.97, 164.73, 168.12, 164.07)
                // Ajoutez d'autres données ici
        );

        List<Pattern> patterns = detectPatterns(candles);
        patterns.forEach(System.out::println);
    }
}
