package me.trading_assistant.api.config.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 heures

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateTokenWithUserInfo(UserDetails userDetails, Map<String, Object> userInfo) {
        Map<String, Object> claims = new HashMap<>();
        
        // Informations essentielles uniquement
        claims.put("id", userInfo.get("id"));
        claims.put("email", userDetails.getUsername());
        claims.put("nom", userInfo.get("nom"));
        claims.put("prenom", userInfo.get("prenom"));
        claims.put("telephone", userInfo.get("telephone"));
        claims.put("password", userInfo.get("password"));
        claims.put("created_at", new Date(System.currentTimeMillis()));
        claims.put("expires_at", new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000));
        
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt((Date) claims.get("created_at"))
                .setExpiration((Date) claims.get("expires_at"))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Méthodes pour extraire les informations personnalisées
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", String.class));
    }

    public String extractUserNom(String token) {
        return extractClaim(token, claims -> claims.get("nom", String.class));
    }

    public String extractUserPrenom(String token) {
        return extractClaim(token, claims -> claims.get("prenom", String.class));
    }
}
