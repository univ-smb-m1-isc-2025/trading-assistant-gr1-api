package me.trading_assistant.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        // Créer une configuration CORS
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost");
        corsConfig.addAllowedOrigin("https://www.trademate.oups.net");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedHeader("*"); // Autoriser tous les en-têtes
        corsConfig.setAllowCredentials(true); // Si vous devez autoriser les cookies ou l'authentification

        // Appliquer la configuration à toutes les URLs
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        System.out.println("CORS configuration applied: " + corsConfig);

        return new CorsFilter(source); // Créer et retourner un CorsFilter
    }
}


// curl -X POST http://localhost:8080/api/users \
// -H "Origin: http://localhost:5173" \
// -H "Content-Type: application/json" \
// -d '{"firstname": "Mathis","lastname": "Feltrin","email": "mathis.feltrin@gmail.com","password": "meilleur test","phone": "0133555566"}'
