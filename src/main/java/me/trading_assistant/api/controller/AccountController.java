package me.trading_assistant.api.controller;

import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.LoginRequest;
import me.trading_assistant.api.application.TrademateService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Account", description = "API pour la gestion des comptes utilisateurs")
@RequiredArgsConstructor
public class AccountController {

    
    private final TrademateService trademateService;


    @GetMapping("/all")
    @Operation(summary = "Récupérer la liste de tous les comptes")
    public List<Account> getAllAccounts() {
        return trademateService.getAllAccounts();
    }

    @GetMapping("/{account_id}")
    @Operation(summary = "Récupérer un compte par son ID")
    public Account getAccountById(@PathVariable Long account_id) {
        return trademateService.getAccountById(account_id);
    }

    @PostMapping
    @Operation(summary = "Créer un compte")
    public Account createAccount(@RequestBody Account account) {
        return trademateService.createAccount(account);
    }

    @DeleteMapping("/{account_id}")
    @Operation(summary = "Supprimer un compte par son ID")
    public void deleteAccount(@PathVariable Long account_id) {
        trademateService.deleteAccount(account_id);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur")
    public String login(@RequestBody LoginRequest loginRequest) {
        Account account = trademateService.getAccountByEmail(loginRequest.getEmail());
        if (account != null && account.getPassword().equals(loginRequest.getPassword())) {
            return "Connexion réussie !";
        } else {
            throw new RuntimeException("Email ou mot de passe incorrect.");
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Test si l'API fonctionne")
    public String test() {
        return "Bienvenue sur l'API Trading Assistant !";
    }

}