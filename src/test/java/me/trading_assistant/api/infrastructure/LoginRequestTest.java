package me.trading_assistant.api.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testSettersAndGetters() {
        // Création d'un objet LoginRequest
        LoginRequest loginRequest = new LoginRequest();

        // Définir les valeurs
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("123456");

        // Vérifications
        assertEquals("test@example.com", loginRequest.getEmail());
        assertEquals("123456", loginRequest.getPassword());
    }

    @Test
    void testDefaultValues() {
        // Création d'un objet LoginRequest sans définir de valeurs
        LoginRequest loginRequest = new LoginRequest();

        // Vérifications
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }
}