package me.trading_assistant.api.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.UserAlert;
import me.trading_assistant.api.infrastructure.UserAlertRepository;

@Service
@RequiredArgsConstructor
public class UserAlertService {

    private final UserAlertRepository userAlertRepository;
    private final EmailService emailService;
    private final WebClient webClient = WebClient.create("https://query1.finance.yahoo.com");

    // Récupérer les alertes d'un utilisateur par son ID
    public List<UserAlert> getAlertsByUserId(Long userId) {
        return userAlertRepository.findByUserId(userId);
    }

    // Ajouter une nouvelle alerte pour un utilisateur
    public UserAlert addAlert(Account user, String symbol, String alertType, Double threshold, Integer days, String pattern, Double priceLevel) {
        UserAlert alert = new UserAlert();
        alert.setUser(user);
        alert.setSymbol(symbol);
        alert.setAlertType(alertType);
        alert.setThreshold(threshold);
        alert.setDays(days);
        alert.setPattern(pattern);
        alert.setPriceLevel(priceLevel);
        return userAlertRepository.save(alert);
    }

    // Supprimer une alerte par son ID
    public void removeAlert(Long alertId) {
        if (!userAlertRepository.existsById(alertId)) {
            throw new RuntimeException("Alerte non trouvée avec l'ID : " + alertId);
        }
        userAlertRepository.deleteById(alertId);
    }

    // Mettre à jour une alerte existante
    public UserAlert updateAlert(Long alertId, String symbol, String alertType, Double threshold, Integer days, String pattern, Double priceLevel) {
        UserAlert alert = userAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée avec l'ID : " + alertId));

        if (symbol != null) alert.setSymbol(symbol);
        if (alertType != null) alert.setAlertType(alertType);
        if (threshold != null) alert.setThreshold(threshold);
        if (days != null) alert.setDays(days);
        if (pattern != null) alert.setPattern(pattern);
        if (priceLevel != null) alert.setPriceLevel(priceLevel);

        return userAlertRepository.save(alert);
    }

    // Vérifier les alertes et envoyer des emails si nécessaire
    public void checkAndSendAlerts() {
        List<UserAlert> alerts = userAlertRepository.findAll();

        for (UserAlert alert : alerts) {
            try {
                if (isAlertConditionMet(alert)) {
                    sendAlertEmail(alert);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la vérification de l'alerte ID : " + alert.getId());
                e.printStackTrace();
            }
        }
    }

    // Vérifie si la condition de l'alerte est atteinte
    private boolean isAlertConditionMet(UserAlert alert) {
        switch (alert.getAlertType()) {
            case "price_variation":
                return checkPriceVariation(alert);
            case "moving_average_cross":
                return checkMovingAverageCross(alert);
            case "volume_spike":
                return checkVolumeSpike(alert);
            case "pattern_detection":
                return checkPatternDetection(alert);
            case "price_breakout":
                return checkPriceBreakout(alert);
            default:
                return false;
        }
    }

    // Vérifie une alerte de type "price_variation"
    private boolean checkPriceVariation(UserAlert alert) {
        Double currentPrice = getCurrentPrice(alert.getSymbol());
        return currentPrice != null && alert.getThreshold() != null && currentPrice >= alert.getThreshold();
    }

    // Vérifie une alerte de type "moving_average_cross"
    private boolean checkMovingAverageCross(UserAlert alert) {
        List<Map<String, Object>> candles = getCandles(alert.getSymbol(), alert.getDays());
        if (candles.isEmpty()) return false;

        double shortMA = calculateMovingAverage(candles, 5);
        double longMA = calculateMovingAverage(candles, 20);
        double previousShortMA = calculateMovingAverage(candles.subList(0, candles.size() - 1), 5);
        double previousLongMA = calculateMovingAverage(candles.subList(0, candles.size() - 1), 20);

        return previousShortMA <= previousLongMA && shortMA > longMA;
    }

    // Calculer une moyenne mobile
    private double calculateMovingAverage(List<Map<String, Object>> candles, int period) {
        if (candles.size() < period) return 0.0;

        double sum = 0.0;
        for (int i = candles.size() - period; i < candles.size(); i++) {
            Double close = (Double) candles.get(i).get("close");
            if (close != null) sum += close;
        }
        return sum / period;
    }

    // Récupère les données des chandeliers pour un symbole donné
    private List<Map<String, Object>> getCandles(String symbol, Integer days) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/" + symbol)
                            .queryParam("interval", "1d")
                            .queryParam("range", days + "d")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("chart")) return new ArrayList<>();

            Map<String, Object> chart = (Map<String, Object>) response.get("chart");
            List<Object> results = (List<Object>) chart.get("result");
            if (results == null || results.isEmpty()) return new ArrayList<>();

            Map<String, Object> result = (Map<String, Object>) results.get(0);
            Map<String, Object> indicators = (Map<String, Object>) result.get("indicators");
            if (indicators == null || !indicators.containsKey("quote")) return new ArrayList<>();

            Map<String, Object> quote = (Map<String, Object>) ((List<Object>) indicators.get("quote")).get(0);
            List<Double> opens = (List<Double>) quote.get("open");
            List<Double> highs = (List<Double>) quote.get("high");
            List<Double> lows = (List<Double>) quote.get("low");
            List<Double> closes = (List<Double>) quote.get("close");
            List<Long> timestamps = (List<Long>) result.get("timestamp");

            if (opens == null || highs == null || lows == null || closes == null || timestamps == null) return new ArrayList<>();

            List<Map<String, Object>> candles = new ArrayList<>();
            for (int i = 0; i < timestamps.size(); i++) {
                Map<String, Object> candle = new HashMap<>();
                candle.put("timestamp", timestamps.get(i));
                candle.put("open", opens.get(i));
                candle.put("high", highs.get(i));
                candle.put("low", lows.get(i));
                candle.put("close", closes.get(i));
                candle.put("patterns", detectPatternsForCandle(candle));
                candles.add(candle);
            }
            return candles;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des chandeliers pour le symbole : " + symbol);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Détecter les patterns dans un chandelier
    private List<String> detectPatternsForCandle(Map<String, Object> candle) {
        List<String> patterns = new ArrayList<>();
        Double open = (Double) candle.get("open");
        Double close = (Double) candle.get("close");
        Double high = (Double) candle.get("high");
        Double low = (Double) candle.get("low");

        if (open == null || close == null || high == null || low == null) return patterns;

        double bodySize = Math.abs(close - open);
        double totalSize = high - low;

        if (bodySize <= totalSize * 0.1) patterns.add("Doji");
        if (close > open && bodySize > totalSize * 0.5) patterns.add("Bullish Engulfing");
        if (open > close && bodySize > totalSize * 0.5) patterns.add("Bearish Engulfing");

        return patterns;
    }

    // Vérifie une alerte de type "volume_spike"
    private boolean checkVolumeSpike(UserAlert alert) {
        List<Map<String, Object>> candles = getCandles(alert.getSymbol(), alert.getDays());
        if (candles.isEmpty() || alert.getThreshold() == null) return false;

        for (Map<String, Object> candle : candles) {
            Double volume = (Double) candle.get("volume");
            if (volume != null && volume >= alert.getThreshold()) return true;
        }
        return false;
    }

    // Vérifie une alerte de type "pattern_detection"
    private boolean checkPatternDetection(UserAlert alert) {
        List<Map<String, Object>> candles = getCandles(alert.getSymbol(), alert.getDays());
        if (candles.isEmpty()) return false;

        for (Map<String, Object> candle : candles) {
            List<String> patterns = (List<String>) candle.get("patterns");
            if (patterns != null && patterns.contains(alert.getPattern())) return true;
        }
        return false;
    }

    // Vérifie une alerte de type "price_breakout"
    private boolean checkPriceBreakout(UserAlert alert) {
        Double currentPrice = getCurrentPrice(alert.getSymbol());
        return currentPrice != null && alert.getPriceLevel() != null && currentPrice >= alert.getPriceLevel();
    }

    // Récupère le prix actuel d'une action depuis l'API Yahoo Finance
    private Double getCurrentPrice(String symbol) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/" + symbol)
                            .queryParam("interval", "1d")
                            .queryParam("range", "1d")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("chart")) return null;

            Map<String, Object> chart = (Map<String, Object>) response.get("chart");
            List<Object> results = (List<Object>) chart.get("result");
            if (results == null || results.isEmpty()) return null;

            Map<String, Object> result = (Map<String, Object>) results.get(0);
            Map<String, Object> indicators = (Map<String, Object>) result.get("indicators");
            if (indicators == null || !indicators.containsKey("quote")) return null;

            Map<String, Object> quote = (Map<String, Object>) ((List<Object>) indicators.get("quote")).get(0);
            List<Double> closes = (List<Double>) quote.get("close");
            if (closes == null || closes.isEmpty()) return null;

            return closes.get(closes.size() - 1);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du prix pour le symbole : " + symbol);
            e.printStackTrace();
            return null;
        }
    }

    // Envoie un email à l'utilisateur pour une alerte atteinte
    private void sendAlertEmail(UserAlert alert) {
        Account user = alert.getUser();
        if (user == null || user.getEmail() == null) return;

        String email = user.getEmail();
        String subject = "Alerte atteinte pour " + alert.getSymbol();
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("Bonjour ").append(user.getFirstname() != null ? user.getFirstname() : "Utilisateur").append(",\n\n");
        textBuilder.append("Votre alerte pour le symbole ").append(alert.getSymbol()).append(" a été atteinte.\n");

        switch (alert.getAlertType()) {
            case "moving_average_cross":
                textBuilder.append("Type d'alerte : Croisement de moyennes mobiles.\n");
                textBuilder.append("Période courte : 5 jours, Période longue : 20 jours.\n");
                break;
            case "price_variation":
                textBuilder.append("Type d'alerte : Variation de prix.\n");
                textBuilder.append("Seuil demandé : ").append(alert.getThreshold()).append("%.\n");
                Double currentPrice = getCurrentPrice(alert.getSymbol());
                if (currentPrice != null) {
                    textBuilder.append("Prix actuel : ").append(currentPrice).append(" USD.\n");
                }
                break;
            case "volume_spike":
                textBuilder.append("Type d'alerte : Pic de volume.\n");
                textBuilder.append("Seuil de volume demandé : ").append(alert.getThreshold()).append(".\n");
                break;
            case "pattern_detection":
                textBuilder.append("Type d'alerte : Détection de pattern.\n");
                textBuilder.append("Pattern demandé : ").append(alert.getPattern()).append(".\n");
                textBuilder.append("Pattern(s) détecté(s) : ").append(alert.getPattern()).append(".\n");
                break;
            case "price_breakout":
                textBuilder.append("Type d'alerte : Franchissement de seuil de prix.\n");
                textBuilder.append("Seuil demandé : ").append(alert.getPriceLevel()).append(" USD.\n");
                Double breakoutPrice = getCurrentPrice(alert.getSymbol());
                if (breakoutPrice != null) {
                    textBuilder.append("Prix actuel : ").append(breakoutPrice).append(" USD.\n");
                }
                break;
            default:
                textBuilder.append("Type d'alerte : Inconnu.\n");
                break;
        }

        textBuilder.append("\nCordialement,\nL'équipe TradeMate !");
        emailService.envoyerEmail(email, subject, textBuilder.toString());
    }
}