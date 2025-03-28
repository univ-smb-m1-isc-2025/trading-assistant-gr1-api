package me.trading_assistant.api.controller;

import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.application.TrademateService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AccountController {

    
    private final TrademateService trademateService;


    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return trademateService.getAllAccounts();
    }

    @GetMapping("/{account_id}")
    public Account getAccountById(@PathVariable Long account_id) {
        return trademateService.getAccountById(account_id);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return trademateService.createAccount(account);
    }

    @DeleteMapping("/{account_id}")
    public void deleteAccount(@PathVariable Long account_id) {
        trademateService.deleteAccount(account_id);
    }

}