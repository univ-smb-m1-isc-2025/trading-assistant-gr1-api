package me.trading_assistant.api.config.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private final long expirationTime = 3600000; // Durée de validité du token (1 heure)

    // Générer un token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Le sujet du token (par exemple, l'email de l'utilisateur)
                .setIssuedAt(new Date()) // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Date d'expiration
                .signWith(key) // Signer avec la clé secrète
                .compact();
    }

    // Valider un token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token invalide
        }
    }

    // Extraire l'email (ou le sujet) du token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
