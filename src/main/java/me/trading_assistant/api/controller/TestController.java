package me.trading_assistant.api.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Accueil", description = "Test si l'API fonctionne")
public class TestController {

    @GetMapping("/")
    @Operation(summary = "Page d'accueil de l'API")
    public String home() {
        return "Bienvenue sur l'API Trading Assistant !";
    }

}





