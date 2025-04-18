package me.trading_assistant.api.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(mailSender); // Injecter le mock via le constructeur
    }

    @Test
    void testEnvoyerEmail() {
        // Préparer les données
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        // Capturer l'email envoyé
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Appeler la méthode
        emailService.envoyerEmail(to, subject, text);

        // Vérifier que le mailSender a été appelé
        verify(mailSender, times(1)).send(messageCaptor.capture());

        // Vérifier le contenu de l'email
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("ayrron01300@gmail.com", sentMessage.getFrom());
        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());
    }
}