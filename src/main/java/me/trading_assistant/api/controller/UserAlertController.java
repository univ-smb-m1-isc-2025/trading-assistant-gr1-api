package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.TrademateService;
import me.trading_assistant.api.application.UserAlertService;
import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.UserAlert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "User Alerts", description = "Gestion des alertes des utilisateurs")
@RequiredArgsConstructor
public class UserAlertController {

    private final UserAlertService userAlertService;
    private final TrademateService trademateService;

    @GetMapping("/{userId}")
    @Operation(summary = "Récupérer les alertes d'un utilisateur")
    public ResponseEntity<List<UserAlert>> getAlertsByUserId(@PathVariable Long userId) {
        List<UserAlert> alerts = userAlertService.getAlertsByUserId(userId);
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Ajouter une alerte pour un utilisateur")
    public ResponseEntity<?> addAlert(
            @PathVariable Long userId,
            @RequestParam String symbol,
            @RequestParam String alertType,
            @RequestParam(required = false) Double threshold,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String pattern,
            @RequestParam(required = false) Double priceLevel) {
        Account user = trademateService.getAccountById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }
        UserAlert alert = userAlertService.addAlert(user, symbol, alertType, threshold, days, pattern, priceLevel);
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{alertId}")
    @Operation(summary = "Mettre à jour une alerte")
    public ResponseEntity<?> updateAlert(
            @PathVariable Long alertId,
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) Double threshold,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String pattern,
            @RequestParam(required = false) Double priceLevel) {
        try {
            UserAlert updatedAlert = userAlertService.updateAlert(alertId, symbol, alertType, threshold, days, pattern, priceLevel);
            return ResponseEntity.ok(updatedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{alertId}")
    @Operation(summary = "Supprimer une alerte")
    public ResponseEntity<?> removeAlert(@PathVariable Long alertId) {
        userAlertService.removeAlert(alertId);
        return ResponseEntity.ok("Alerte supprimée avec succès");
    }


    @PostMapping("/check-alerts")
    @Operation(summary = "Vérifier les alertes et envoyer des emails")
    public ResponseEntity<?> checkAlerts() {
        userAlertService.checkAndSendAlerts();
        return ResponseEntity.ok("Vérification des alertes terminée et emails envoyés si nécessaire.");
    }
}