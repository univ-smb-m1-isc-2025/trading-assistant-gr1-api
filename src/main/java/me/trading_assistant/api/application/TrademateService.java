package me.trading_assistant.api.application;

import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.AccountRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TrademateService {

    
    private final AccountRepository accountRepository;


    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long account_id) {
        return accountRepository.findById(account_id).orElse(null);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(Long account_id) {
        accountRepository.deleteById(account_id);
    }


}
