package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    /**
     * Genera un token JWT para un usuario
     */
    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId().toString());
        claims.put("email", usuario.getCorreoElectronico());
        claims.put("role", usuario.getRol());
        claims.put("nombre", usuario.getNombre());
        claims.put("apellido", usuario.getApellido());
        
        return createToken(claims, usuario.getCorreoElectronico());
    }
    
    /**
     * Crea un token JWT con los claims especificados
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }
    
    /**
     * Extrae el username del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrae la fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extrae un claim específico del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extrae todos los claims del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Verifica si el token ha expirado
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Valida un token para un usuario específico
     */
    public Boolean validateToken(String token, Usuario usuario) {
        final String username = extractUsername(token);
        return (username.equals(usuario.getCorreoElectronico()) && !isTokenExpired(token));
    }
    
    /**
     * Obtiene la clave de firma
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Extrae el ID del usuario del token
     */
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }
    
    /**
     * Extrae el rol del usuario del token
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    
    /**
     * Extrae el nombre del usuario del token
     */
    public String extractNombre(String token) {
        return extractClaim(token, claims -> claims.get("nombre", String.class));
    }
    
    /**
     * Extrae el apellido del usuario del token
     */
    public String extractApellido(String token) {
        return extractClaim(token, claims -> claims.get("apellido", String.class));
    }
    
    /**
     * Obtiene el tiempo de expiración en milisegundos
     */
    public long getExpirationTime() {
        return expiration;
    }
}

