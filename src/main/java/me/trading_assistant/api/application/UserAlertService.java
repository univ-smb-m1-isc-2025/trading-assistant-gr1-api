package me.trading_assistant.api.application;

import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.UserAlert;
import me.trading_assistant.api.infrastructure.UserAlertRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAlertService {

    private final UserAlertRepository userAlertRepository;

    public List<UserAlert> getAlertsByUserId(Long userId) {
        return userAlertRepository.findByUserId(userId);
    }

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

    public void removeAlert(Long alertId) {
        userAlertRepository.deleteById(alertId);
    }

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

}
