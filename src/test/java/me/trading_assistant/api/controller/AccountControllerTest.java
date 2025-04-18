package me.trading_assistant.api.controller;

import me.trading_assistant.api.config.JWT.JwtUtil;
import me.trading_assistant.api.infrastructure.Account;
import me.trading_assistant.api.infrastructure.LoginRequest;
import me.trading_assistant.api.application.TrademateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final TrademateService trademateService = mock(TrademateService.class);
    private final AccountController accountController = new AccountController(jwtUtil, trademateService);

    @Test
    void testGetAllAccounts() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setEmail("test@example.com");

        when(trademateService.getAllAccounts()).thenReturn(List.of(mockAccount));

        List<Account> accounts = accountController.getAllAccounts();

        assertEquals(1, accounts.size());
        assertEquals("test@example.com", accounts.get(0).getEmail());
        verify(trademateService, times(1)).getAllAccounts();
    }

    @Test
    void testGetAccountById() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setEmail("test@example.com");

        when(trademateService.getAccountById(1L)).thenReturn(mockAccount);

        Account account = accountController.getAccountById(1L);

        assertEquals("test@example.com", account.getEmail());
        verify(trademateService, times(1)).getAccountById(1L);
    }

    @Test
    void testCreateAccount() {
        Account newAccount = new Account();
        newAccount.setEmail("test@example.com");
        newAccount.setPassword("password"); // Assurez-vous que le mot de passe est défini

        Account createdAccount = new Account();
        createdAccount.setId(1L);
        createdAccount.setEmail("test@example.com");
        createdAccount.setPassword("password"); // Ajoutez également un mot de passe ici

        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(null);
        when(trademateService.createAccount(newAccount)).thenReturn(createdAccount);
        when(jwtUtil.generateTokenWithUserInfo(any(), any())).thenReturn("mockToken");

        ResponseEntity<?> response = accountController.createAccount(newAccount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken", ((Map<?, ?>) response.getBody()).get("token"));
        verify(trademateService, times(1)).createAccount(newAccount);
    }

    @Test
    void testCreateAccountConflict() {
        Account existingAccount = new Account();
        existingAccount.setEmail("test@example.com");

        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(existingAccount);

        ResponseEntity<?> response = accountController.createAccount(existingAccount);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Un compte avec cet email existe déjà.", response.getBody());
        verify(trademateService, never()).createAccount(any());
    }

    @Test
    void testLoginSuccess() {
        Account mockAccount = new Account();
        mockAccount.setEmail("test@example.com");
        mockAccount.setPassword("123456");

        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(mockAccount);
        when(jwtUtil.generateTokenWithUserInfo(any(), any())).thenReturn("mockToken");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("123456");

        ResponseEntity<?> response = accountController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken", ((Map<?, ?>) response.getBody()).get("token"));
        verify(trademateService, times(1)).getAccountByEmail("test@example.com");
    }

    @Test
    void testLoginFailure() {
        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(null);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        ResponseEntity<?> response = accountController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Email ou mot de passe incorrect.", response.getBody());
        verify(trademateService, times(1)).getAccountByEmail("test@example.com");
    }

    @Test
    void testDeleteAccount() {
        accountController.deleteAccount(1L);

        verify(trademateService, times(1)).deleteAccount(1L);
    }
}