package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountRepositoryTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountRepositoryTest accountRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        // Mock des données
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setEmail("test@example.com");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

        // Appeler la méthode
        Optional<Account> foundAccount = accountRepository.findById(1L);

        // Vérifications
        assertTrue(foundAccount.isPresent());
        assertEquals("test@example.com", foundAccount.get().getEmail());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByEmail() {
        // Mock des données
        Account mockAccount = new Account();
        mockAccount.setEmail("test@example.com");

        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockAccount));

        // Appeler la méthode
        Optional<Account> foundAccount = accountRepository.findByEmail("test@example.com");

        // Vérifications
        assertTrue(foundAccount.isPresent());
        assertEquals("test@example.com", foundAccount.get().getEmail());
        verify(accountRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testDeleteById() {
        // Mock de la méthode
        doNothing().when(accountRepository).deleteById(1L);

        // Appeler la méthode
        accountRepository.deleteById(1L);

        // Vérifications
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSave() {
        // Mock des données
        Account mockAccount = new Account();
        mockAccount.setEmail("test@example.com");

        when(accountRepository.save(mockAccount)).thenReturn(mockAccount);

        // Appeler la méthode
        Account savedAccount = accountRepository.save(mockAccount);

        // Vérifications
        assertNotNull(savedAccount);
        assertEquals("test@example.com", savedAccount.getEmail());
        verify(accountRepository, times(1)).save(mockAccount);
    }
}