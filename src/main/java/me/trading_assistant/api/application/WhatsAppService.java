package me.trading_assistant.api.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@Service
public class WhatsAppService {

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String phoneNumber, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappApiToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messaging_product", "whatsapp");
        requestBody.put("to", phoneNumber);
        requestBody.put("type", "text");
        
        Map<String, String> text = new HashMap<>();
        text.put("body", message);
        requestBody.put("text", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            whatsappApiUrl,
            request,
            String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erreur lors de l'envoi du message WhatsApp: " + response.getBody());
        }
    }
} 