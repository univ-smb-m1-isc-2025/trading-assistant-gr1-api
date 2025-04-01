package me.trading_assistant.api.application;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class FinanceService {

    public BigDecimal getStockPrice(String symbol) throws IOException {
        Stock stock = YahooFinance.get(symbol); // Récupère les données de l'action
        return stock.getQuote().getPrice(); // Retourne le prix actuel
    }
}