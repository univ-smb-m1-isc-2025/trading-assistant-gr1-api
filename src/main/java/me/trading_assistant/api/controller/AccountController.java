package me.trading_assistant.api.controller;

import me.trading_assistant.api.config.JWT.JwtUtil;
import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.LoginRequest;
import me.trading_assistant.api.application.TrademateService;
import org.springframework.security.core.userdetails.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Account", description = "API pour la gestion des comptes utilisateurs")
@RequiredArgsConstructor
public class AccountController {

    private final JwtUtil jwtUtil;
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
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        // Créer le compte
        Account createdAccount = trademateService.createAccount(account);
        
        // Créer un map avec les informations de l'utilisateur
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", createdAccount.getId());
        userInfo.put("nom", createdAccount.getLastname());
        userInfo.put("prenom", createdAccount.getFirstname());
        userInfo.put("telephone", createdAccount.getPhone());
        userInfo.put("password", createdAccount.getPassword());
        
        // Générer un token JWT avec les informations de l'utilisateur
        String token = jwtUtil.generateTokenWithUserInfo(
            User.builder()
                .username(createdAccount.getEmail())
                .password(createdAccount.getPassword())
                .authorities("USER")
                .build(),
            userInfo
        );
        
        // Retourner uniquement le token
        return ResponseEntity.ok(Map.of("token", token));
    }

    @DeleteMapping("/{account_id}")
    @Operation(summary = "Supprimer un compte par son ID")
    public void deleteAccount(@PathVariable Long account_id) {
        trademateService.deleteAccount(account_id);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Account account = trademateService.getAccountByEmail(loginRequest.getEmail());
        if (account != null && account.getPassword().equals(loginRequest.getPassword())) {
            // Créer un map avec les informations de l'utilisateur
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", account.getId());
            userInfo.put("nom", account.getLastname());
            userInfo.put("prenom", account.getFirstname());
            userInfo.put("telephone", account.getPhone());
            userInfo.put("password", account.getPassword());
            
            // Générer un token JWT avec les informations de l'utilisateur
            String token = jwtUtil.generateTokenWithUserInfo(
                User.builder()
                    .username(account.getEmail())
                    .password(account.getPassword())
                    .authorities("USER")
                    .build(),
                userInfo
            );
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect.");
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Test si l'API fonctionne")
    public String test() {
        return "Bienvenue sur l'API Trading Assistant !";
    }

}