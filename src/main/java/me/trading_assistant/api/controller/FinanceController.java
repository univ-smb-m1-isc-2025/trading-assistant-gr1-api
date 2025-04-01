package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.FinanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/finance")
@Tag(name = "Finance", description = "API pour la gestion des données financières")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("stock/{symbol}")
    @Operation(summary = "Récupérer le prix d'une action par son symbole")
    public BigDecimal getStockPrice(@PathVariable String symbol) throws IOException {
        return financeService.getStockPrice(symbol);
    }
}