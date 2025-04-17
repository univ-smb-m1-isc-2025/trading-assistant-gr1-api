package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.FinanceService;
import me.trading_assistant.api.application.CandlePatternService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/finance")
@Tag(name = "Finance", description = "API pour la gestion des données financières")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;
    private final CandlePatternService patternService;

    @GetMapping("chart/{symbol}")
    @Operation(summary = "Récupérer les données du graphique d'une action par son symbole et sa plage")
    public ResponseEntity<?> getStockChart(
            @PathVariable String symbol,
            @RequestParam String range) {
        try {
            Map<String, Object> chartData = financeService.getStockChart(symbol, range);
            return ResponseEntity.ok(chartData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("patterns/{symbol}")
    @Operation(summary = "Détecter les patterns de chandeliers pour une action")
    public ResponseEntity<?> detectPatterns(
            @PathVariable String symbol,
            @RequestParam String range) {
        try {
            Map<String, Object> chartData = financeService.getStockChart(symbol, range);
            Map<String, Object> patterns = patternService.detectPatterns(chartData);
            
            // Créer une nouvelle réponse avec uniquement les informations essentielles
            Map<String, Object> response = new HashMap<>();
            response.put("symbol", symbol);
            response.put("range", range);
            response.put("candles", patterns.get("candles"));
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("combined/{symbol}")
    @Operation(summary = "Récupérer les données combinées des routes chart et patterns")
    public ResponseEntity<?> getCombinedData(
            @PathVariable String symbol,
            @RequestParam String range) {
        try {
            // Appeler le service pour récupérer les données du graphique (chart)
            Map<String, Object> chartData = financeService.getStockChart(symbol, range);
    
            // Appeler le service pour détecter les patterns
            Map<String, Object> patternsData = patternService.detectPatterns(chartData);
    
            // Combiner les deux réponses dans un JSON
            Map<String, Object> combinedResponse = new HashMap<>();
            combinedResponse.put("chart", chartData);
            combinedResponse.put("patterns", patternsData);
    
            return ResponseEntity.ok(combinedResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur s'est produite : " + e.getMessage()));
        }
    }
    
}

