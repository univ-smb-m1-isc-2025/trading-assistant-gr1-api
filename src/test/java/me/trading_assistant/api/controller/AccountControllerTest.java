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
        // Mock des données
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setEmail("test@example.com");
        mockAccount.setFirstname("John");
        mockAccount.setLastname("Doe");
        mockAccount.setPassword("123456");
        mockAccount.setPhone("123456789");

        List<Account> mockAccounts = List.of(mockAccount);
        when(trademateService.getAllAccounts()).thenReturn(mockAccounts);

        // Appeler la méthode
        List<Account> accounts = accountController.getAllAccounts();

        // Vérifier les résultats
        assertEquals(1, accounts.size());
        assertEquals("test@example.com", accounts.get(0).getEmail());
        verify(trademateService, times(1)).getAllAccounts();
    }

    @Test
    void testGetAccountById() {
        // Mock des données
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setEmail("test@example.com");
        mockAccount.setFirstname("John");
        mockAccount.setLastname("Doe");
        mockAccount.setPassword("123456");
        mockAccount.setPhone("123456789");

        when(trademateService.getAccountById(1L)).thenReturn(mockAccount);

        // Appeler la méthode
        Account account = accountController.getAccountById(1L);

        // Vérifier les résultats
        assertEquals("test@example.com", account.getEmail());
        verify(trademateService, times(1)).getAccountById(1L);
    }

    @Test
    void testCreateAccount() {
        // Mock des données
        Account newAccount = new Account();
        newAccount.setEmail("test@example.com");
        newAccount.setFirstname("John");
        newAccount.setLastname("Doe");
        newAccount.setPassword("123456");
        newAccount.setPhone("123456789");

        Account createdAccount = new Account();
        createdAccount.setId(1L);
        createdAccount.setEmail("test@example.com");
        createdAccount.setFirstname("John");
        createdAccount.setLastname("Doe");
        createdAccount.setPassword("123456");
        createdAccount.setPhone("123456789");

        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(null);
        when(trademateService.createAccount(newAccount)).thenReturn(createdAccount);
        when(jwtUtil.generateTokenWithUserInfo(any(), any())).thenReturn("mockToken");

        // Appeler la méthode
        ResponseEntity<?> response = accountController.createAccount(newAccount);

        // Vérifier les résultats
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken", ((Map<?, ?>) response.getBody()).get("token"));
        verify(trademateService, times(1)).createAccount(newAccount);
    }

    @Test
    void testCreateAccountConflict() {
        // Mock des données
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setEmail("test@example.com");
        existingAccount.setFirstname("John");
        existingAccount.setLastname("Doe");
        existingAccount.setPassword("123456");
        existingAccount.setPhone("123456789");

        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(existingAccount);

        // Appeler la méthode
        ResponseEntity<?> response = accountController.createAccount(existingAccount);

        // Vérifier les résultats
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Un compte avec cet email existe déjà.", response.getBody());
        verify(trademateService, never()).createAccount(any());
    }

    @Test
    void testLoginSuccess() {
        // Mock des données
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setEmail("test@example.com");
        mockAccount.setFirstname("John");
        mockAccount.setLastname("Doe");
        mockAccount.setPassword("123456");
        mockAccount.setPhone("123456789");

        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(mockAccount);
        when(jwtUtil.generateTokenWithUserInfo(any(), any())).thenReturn("mockToken");

        // Appeler la méthode
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("123456");
        ResponseEntity<?> response = accountController.login(loginRequest);

        // Vérifier les résultats
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken", ((Map<?, ?>) response.getBody()).get("token"));
        verify(trademateService, times(1)).getAccountByEmail("test@example.com");
    }

    @Test
    void testLoginFailure() {
        // Mock des données
        when(trademateService.getAccountByEmail("test@example.com")).thenReturn(null);

        // Appeler la méthode
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");
        ResponseEntity<?> response = accountController.login(loginRequest);

        // Vérifier les résultats
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Email ou mot de passe incorrect.", response.getBody());
        verify(trademateService, times(1)).getAccountByEmail("test@example.com");
    }

    @Test
    void testDeleteAccount() {
        // Appeler la méthode
        accountController.deleteAccount(1L);

        // Vérifier que le service a été appelé
        verify(trademateService, times(1)).deleteAccount(1L);
    }
}