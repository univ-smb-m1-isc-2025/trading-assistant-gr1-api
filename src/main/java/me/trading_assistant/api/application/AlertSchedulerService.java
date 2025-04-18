package me.trading_assistant.api.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertSchedulerService {

    private final UserAlertService userAlertService;

    // Exécution tous les jours à minuit (0 0 0 * * *)
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAlertCheck() {
        System.out.println("Début de la vérification quotidienne des alertes...");
        try {
            userAlertService.checkAndSendAlerts();
            System.out.println("Vérification des alertes terminée avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification des alertes : " + e.getMessage());
            e.printStackTrace();
        }
    }
}