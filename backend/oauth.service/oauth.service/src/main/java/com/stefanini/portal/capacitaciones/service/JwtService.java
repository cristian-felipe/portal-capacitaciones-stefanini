package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.constants.AuthConstants;
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
    
    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthConstants.CLAIM_USER_ID, usuario.getId().toString());
        claims.put(AuthConstants.CLAIM_EMAIL, usuario.getCorreoElectronico());
        claims.put(AuthConstants.CLAIM_ROLE, usuario.getRol());
        claims.put(AuthConstants.CLAIM_NOMBRE, usuario.getNombre());
        claims.put(AuthConstants.CLAIM_APELLIDO, usuario.getApellido());
        
        return createToken(claims, usuario.getCorreoElectronico());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
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
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateToken(String token, Usuario usuario) {
        final String username = extractUsername(token);
        return (username.equals(usuario.getCorreoElectronico()) && !isTokenExpired(token));
    }
    
    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get(AuthConstants.CLAIM_USER_ID, String.class));
    }
    
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get(AuthConstants.CLAIM_ROLE, String.class));
    }
    
    public String extractNombre(String token) {
        return extractClaim(token, claims -> claims.get(AuthConstants.CLAIM_NOMBRE, String.class));
    }
    
    public String extractApellido(String token) {
        return extractClaim(token, claims -> claims.get(AuthConstants.CLAIM_APELLIDO, String.class));
    }
    
    public long getExpirationTime() {
        return expiration;
    }
}

