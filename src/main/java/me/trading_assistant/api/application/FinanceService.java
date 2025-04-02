package me.trading_assistant.api.application;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class FinanceService {

    private final WebClient webClient;

    public FinanceService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://query1.finance.yahoo.com").build();
    }

    public Map<String, Object> getStockChart(String symbol, String range) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/" + symbol)
                            .queryParam("interval", "1d")
                            .queryParam("range", range)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // Vérifiez si la réponse contient des données valides
            if (response == null || !response.containsKey("chart")) {
                throw new RuntimeException("Données introuvables pour le symbole : " + symbol);
            }

            return response;
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erreur lors de la récupération des données pour le symbole : " + symbol, e);
        }
    }
}