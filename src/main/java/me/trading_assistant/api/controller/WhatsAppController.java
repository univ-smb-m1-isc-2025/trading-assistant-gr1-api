package me.trading_assistant.api.controller;

import me.trading_assistant.api.application.WhatsAppService;
import me.trading_assistant.api.application.TrademateService;
import me.trading_assistant.api.infrastructure.Account;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/whatsapp")
@Tag(name = "WhatsApp", description = "API pour l'envoi de messages WhatsApp")
@RequiredArgsConstructor
public class WhatsAppController {

    private final WhatsAppService whatsAppService;
    private final TrademateService trademateService;

    @PostMapping("/send/{accountId}")
    @Operation(summary = "Envoyer un message WhatsApp à un utilisateur")
    public String sendMessage(@PathVariable Long accountId, @RequestBody String message) {
        Account account = trademateService.getAccountById(accountId);
        if (account == null || account.getPhone() == null) {
            throw new RuntimeException("Compte non trouvé ou numéro de téléphone non enregistré");
        }
        
        whatsAppService.sendMessage(account.getPhone(), message);
        return "Message envoyé avec succès à " + account.getPhone();
    }
} 