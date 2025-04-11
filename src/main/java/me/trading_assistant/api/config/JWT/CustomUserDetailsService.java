package me.trading_assistant.api.config.JWT;

import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.application.TrademateService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TrademateService trademateService;

    public CustomUserDetailsService(TrademateService trademateService) {
        this.trademateService = trademateService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = trademateService.getAccountByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }

        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .authorities("USER")
                .build();
    }
} 