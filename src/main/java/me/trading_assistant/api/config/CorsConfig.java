package me.trading_assistant.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Autorise toutes les routes
                        .allowedOrigins("http://localhost:5173", "https://www.trademate.oups.net") // Autorise toutes les origines (remplacez par des domaines spécifiques si nécessaire)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Autorise les méthodes HTTP
                        .allowedHeaders("*") // Autorise tous les en-têtes
                        .allowCredentials(true); // Autorise l'envoi de cookies
            }
        };
    }
}