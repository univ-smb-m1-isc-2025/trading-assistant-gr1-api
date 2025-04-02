package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.FinanceService;
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

@RestController
@RequestMapping("/api/finance")
@Tag(name = "Finance", description = "API pour la gestion des données financières")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

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

}