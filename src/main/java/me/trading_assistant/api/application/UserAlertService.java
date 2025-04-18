package me.trading_assistant.api.application;

import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.UserAlert;
import me.trading_assistant.api.infrastructure.UserAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAlertService {

    private final UserAlertRepository userAlertRepository;
    private final EmailService emailService;
    private final WebClient webClient = WebClient.create("https://query1.finance.yahoo.com"); // API Yahoo Finance

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
        // Récupérer toutes les alertes
        List<UserAlert> alerts = userAlertRepository.findAll();

        for (UserAlert alert : alerts) {
            // Vérifier si la condition de l'alerte est atteinte
            if (isAlertConditionMet(alert)) {
                // Envoyer un email à l'utilisateur
                sendAlertEmail(alert);
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
        // Récupérer les données des chandeliers pour le symbole
        List<Map<String, Object>> candles = getCandles(alert.getSymbol(), alert.getDays());
        if (candles == null || candles.isEmpty()) {
            System.err.println("Aucune donnée de chandeliers trouvée pour le symbole : " + alert.getSymbol());
            return false;
        }
    
        // Calculer les moyennes mobiles pour les périodes actuelles et précédentes
        double shortMA = calculateMovingAverage(candles, 5); // Moyenne mobile courte (5 périodes)
        double longMA = calculateMovingAverage(candles, 20); // Moyenne mobile longue (20 périodes)
    
        // Vérifier si un croisement s'est produit
        double previousShortMA = calculateMovingAverage(candles.subList(0, candles.size() - 1), 5);
        double previousLongMA = calculateMovingAverage(candles.subList(0, candles.size() - 1), 20);
    
        return previousShortMA <= previousLongMA && shortMA > longMA;
    }
    
    private double calculateMovingAverage(List<Map<String, Object>> candles, int period) {
        if (candles.size() < period) {
            return 0.0; // Pas assez de données pour calculer la moyenne
        }
    
        double sum = 0.0;
        for (int i = candles.size() - period; i < candles.size(); i++) {
            Map<String, Object> candle = candles.get(i);
            Double close = (Double) candle.get("close");
            if (close != null) {
                sum += close;
            }
        }
    
        return sum / period;
    }

    // Récupère les données des chandeliers pour un symbole donné
    private List<Map<String, Object>> getCandles(String symbol, Integer days) {
        try {
            // Appel à l'API Yahoo Finance pour récupérer les chandeliers
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/" + symbol)
                            .queryParam("interval", "1d")
                            .queryParam("range", days + "d")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
    
            if (response == null || !response.containsKey("chart")) {
                System.err.println("Réponse invalide ou vide pour le symbole : " + symbol);
                return null;
            }
    
            Map<String, Object> chart = (Map<String, Object>) response.get("chart");
            List<Object> results = (List<Object>) chart.get("result");
    
            if (results == null || results.isEmpty()) {
                System.err.println("Aucun résultat trouvé pour le symbole : " + symbol);
                return null;
            }
    
            Map<String, Object> result = (Map<String, Object>) results.get(0);
            Map<String, Object> indicators = (Map<String, Object>) result.get("indicators");
    
            if (indicators == null || !indicators.containsKey("quote")) {
                System.err.println("Indicateurs manquants pour le symbole : " + symbol);
                return null;
            }
    
            List<Object> quotes = (List<Object>) indicators.get("quote");
            if (quotes == null || quotes.isEmpty()) {
                System.err.println("Données de cotation manquantes pour le symbole : " + symbol);
                return null;
            }
    
            Map<String, Object> quote = (Map<String, Object>) quotes.get(0);
            List<Double> closes = (List<Double>) quote.get("close");
            List<Long> timestamps = (List<Long>) result.get("timestamp");
            List<Double> volumes = (List<Double>) quote.get("volume");
    
            if (closes == null || timestamps == null || volumes == null) {
                System.err.println("Données de chandeliers manquantes pour le symbole : " + symbol);
                return null;
            }
    
            // Construire la liste des chandeliers
            List<Map<String, Object>> candles = new java.util.ArrayList<>();
            for (int i = 0; i < closes.size(); i++) {
                Map<String, Object> candle = new java.util.HashMap<>();
                candle.put("close", closes.get(i));
                candle.put("timestamp", timestamps.get(i));
                candle.put("volume", volumes.get(i));
    
                // Ajouter les patterns détectés (si applicable)
                List<String> patterns = detectPatternsForCandle(candle);
                candle.put("patterns", patterns);
    
                candles.add(candle);
            }
    
            return candles;
    
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des chandeliers pour le symbole : " + symbol);
            e.printStackTrace();
            return null;
        }
    }

    private List<String> detectPatternsForCandle(Map<String, Object> candle) {
        // Exemple de détection de patterns
        List<String> patterns = new ArrayList<>();

        // Exemple : Ajouter un pattern "Doji" si les conditions sont remplies
        Double open = (Double) candle.get("open");
        Double close = (Double) candle.get("close");
        Double high = (Double) candle.get("high");
        Double low = (Double) candle.get("low");

        if (open != null && close != null && high != null && low != null) {
            double bodySize = Math.abs(close - open);
            double totalSize = high - low;

            if (bodySize <= totalSize * 0.1) {
                patterns.add("Doji");
            }
        }

        return patterns;
    }


    // Vérifie une alerte de type "volume_spike"
    private boolean checkVolumeSpike(UserAlert alert) {
        // Récupérer les données des chandeliers pour le symbole
        List<Map<String, Object>> candles = getCandles(alert.getSymbol(), alert.getDays());
        if (candles == null || candles.isEmpty()) {
            System.err.println("Aucune donnée de chandeliers trouvée pour le symbole : " + alert.getSymbol());
            return false;
        }
    
        if (alert.getThreshold() == null) {
            System.err.println("Seuil de volume non défini pour l'alerte de type 'volume_spike'.");
            return false;
        }
    
        // Vérifier si le volume dépasse le seuil
        for (Map<String, Object> candle : candles) {
            if (!candle.containsKey("volume")) {
                System.err.println("Donnée de volume manquante pour un chandelier.");
                continue;
            }
    
            Double volume = (Double) candle.get("volume");
            if (volume != null && volume >= alert.getThreshold()) {
                return true; // Pic de volume détecté
            }
        }
    
        return false; // Aucun pic de volume détecté
    }



    // Vérifie une alerte de type "pattern_detection"
    private boolean checkPatternDetection(UserAlert alert) {
        // Récupérer les données des chandeliers pour le symbole
        List<Map<String, Object>> candles = getCandles(alert.getSymbol(), alert.getDays());

        if (candles == null || candles.isEmpty()) {
            System.err.println("Aucune donnée de chandeliers trouvée pour le symbole : " + alert.getSymbol());
            return false;
        }

        // Vérifier si le pattern attendu est présent dans les chandeliers
        for (Map<String, Object> candle : candles) {
            List<String> patterns = (List<String>) candle.get("patterns");
            if (patterns != null && patterns.contains(alert.getPattern())) {
                System.out.println("Pattern détecté : " + alert.getPattern() + " pour le symbole : " + alert.getSymbol());
                return true; // Pattern détecté
            }
        }

        System.out.println("Pattern non détecté : " + alert.getPattern() + " pour le symbole : " + alert.getSymbol());
        return false; // Pattern non détecté
    }

    // Vérifie une alerte de type "price_breakout"
    private boolean checkPriceBreakout(UserAlert alert) {
        Double currentPrice = getCurrentPrice(alert.getSymbol());
        return currentPrice != null && alert.getPriceLevel() != null && currentPrice >= alert.getPriceLevel();
    }
    

    // Récupère le prix actuel d'une action depuis l'API Yahoo Finance
    private Double getCurrentPrice(String symbol) {
        try {
            // Appel à l'API Yahoo Finance
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/" + symbol)
                            .queryParam("interval", "1d")
                            .queryParam("range", "1d")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // Vérification de la structure de la réponse
            if (response == null || !response.containsKey("chart")) {
                System.err.println("Réponse invalide ou vide pour le symbole : " + symbol);
                return null;
            }

            Map<String, Object> chart = (Map<String, Object>) response.get("chart");
            List<Object> results = (List<Object>) chart.get("result");

            if (results == null || results.isEmpty()) {
                System.err.println("Aucun résultat trouvé pour le symbole : " + symbol);
                return null;
            }

            Map<String, Object> result = (Map<String, Object>) results.get(0);
            Map<String, Object> indicators = (Map<String, Object>) result.get("indicators");

            if (indicators == null || !indicators.containsKey("quote")) {
                System.err.println("Indicateurs manquants pour le symbole : " + symbol);
                return null;
            }

            List<Object> quotes = (List<Object>) indicators.get("quote");
            if (quotes == null || quotes.isEmpty()) {
                System.err.println("Données de cotation manquantes pour le symbole : " + symbol);
                return null;
            }

            Map<String, Object> quote = (Map<String, Object>) quotes.get(0);
            if (!quote.containsKey("close")) {
                System.err.println("Données de clôture manquantes pour le symbole : " + symbol);
                return null;
            }

            List<Double> closes = (List<Double>) quote.get("close");
            if (closes == null || closes.isEmpty()) {
                System.err.println("Données de clôture manquantes pour le symbole : " + symbol);
                return null;
            }

            // Retourner le dernier prix de clôture
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
        if (user == null || user.getEmail() == null) {
            System.err.println("Utilisateur ou email manquant pour l'alerte.");
            return;
        }
    
        String email = user.getEmail();
        String subject = "Alerte atteinte pour " + alert.getSymbol();
    
        // Construire le contenu de l'email en fonction du type d'alerte
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append("Bonjour ").append(user.getFirstname() != null ? user.getFirstname() : "Utilisateur").append(",\n\n");
        textBuilder.append("Votre alerte pour le symbole ").append(alert.getSymbol()).append(" a été atteinte.\n");
    
        switch (alert.getAlertType()) {
            case "moving_average_cross":
                textBuilder.append("Type d'alerte : Croisement de moyennes mobiles.\n");
                if (alert.getDays() != null) {
                    textBuilder.append("- Nombre de jours pour la moyenne mobile : ").append(alert.getDays()).append("\n");
                }
                break;
    
            case "price_variation":
                textBuilder.append("Type d'alerte : Variation de prix.\n");
                if (alert.getThreshold() != null) {
                    textBuilder.append("- Seuil de variation : ").append(alert.getThreshold()).append("%\n");
                }
                break;
    
            case "volume_spike":
                textBuilder.append("Type d'alerte : Pic de volume.\n");
                if (alert.getThreshold() != null) {
                    textBuilder.append("- Seuil de volume : ").append(alert.getThreshold()).append("\n");
                }
                break;
    
            case "pattern_detection":
                textBuilder.append("Type d'alerte : Détection de pattern.\n");
                if (alert.getPattern() != null) {
                    textBuilder.append("- Pattern détecté : ").append(alert.getPattern()).append("\n");
                }
                break;
    
            case "price_breakout":
                textBuilder.append("Type d'alerte : Franchissement de seuil de prix.\n");
                if (alert.getPriceLevel() != null) {
                    textBuilder.append("- Niveau de prix : ").append(alert.getPriceLevel()).append("\n");
                }
                break;
    
            default:
                textBuilder.append("Type d'alerte : Inconnu.\n");
                break;
        }
    
        textBuilder.append("\nCordialement,\n");
        textBuilder.append("L'équipe Trading Assistant");
    
        // Envoyer l'email
        emailService.envoyerEmail(email, subject, textBuilder.toString());
    }
}