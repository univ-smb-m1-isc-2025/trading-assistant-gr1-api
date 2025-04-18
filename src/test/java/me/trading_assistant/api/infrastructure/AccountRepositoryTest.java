/*
package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAndFindById() {
        // Créer un compte
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setFirstname("John");
        account.setLastname("Doe");
        account.setPassword("123456");
        account.setPhone("123456789");

        // Sauvegarder le compte
        Account savedAccount = accountRepository.save(account);

        // Rechercher le compte par ID
        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        // Vérifications
        assertTrue(foundAccount.isPresent());
        assertEquals("test@example.com", foundAccount.get().getEmail());
        assertEquals("John", foundAccount.get().getFirstname());
        assertEquals("Doe", foundAccount.get().getLastname());
    }

    @Test
    void testFindByEmail() {
        // Créer un compte
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setFirstname("John");
        account.setLastname("Doe");
        account.setPassword("123456");
        account.setPhone("123456789");

        // Sauvegarder le compte
        accountRepository.save(account);

        // Rechercher le compte par email
        Optional<Account> foundAccount = accountRepository.findByEmail("test@example.com");

        // Vérifications
        assertTrue(foundAccount.isPresent());
        assertEquals("test@example.com", foundAccount.get().getEmail());
        assertEquals("John", foundAccount.get().getFirstname());
        assertEquals("Doe", foundAccount.get().getLastname());
    }

    @Test
    void testDeleteById() {
        // Créer un compte
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setFirstname("John");
        account.setLastname("Doe");
        account.setPassword("123456");
        account.setPhone("123456789");

        // Sauvegarder le compte
        Account savedAccount = accountRepository.save(account);

        // Supprimer le compte
        accountRepository.deleteById(savedAccount.getId());

        // Vérifier que le compte n'existe plus
        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());
        assertFalse(foundAccount.isPresent());
    }

    @Test
    void testExistsById() {
        // Créer un compte
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setFirstname("John");
        account.setLastname("Doe");
        account.setPassword("123456");
        account.setPhone("123456789");

        // Sauvegarder le compte
        Account savedAccount = accountRepository.save(account);

        // Vérifier que le compte existe
        boolean exists = accountRepository.existsById(savedAccount.getId());
        assertTrue(exists);

        // Supprimer le compte
        accountRepository.deleteById(savedAccount.getId());

        // Vérifier que le compte n'existe plus
        exists = accountRepository.existsById(savedAccount.getId());
        assertFalse(exists);
    }
}*/
