package me.trading_assistant.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.trading_assistant.api.application.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/mail")
@Tag(name = "Mail", description = "Route pour l'envoi de mails")
@RequiredArgsConstructor
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Envoyer un email de test")
    @GetMapping("/send-email/{mail}")
    public String sendEmail(@PathVariable String mail) {
        emailService.envoyerEmail(mail, "Coucou", "Ceci est un email envoyé depuis Spring Boot !");
        return "Email envoyé !";
    }

}
