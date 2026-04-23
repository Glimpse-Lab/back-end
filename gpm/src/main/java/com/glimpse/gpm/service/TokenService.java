package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {
    @Value("${app.jwt.secret:glimpse-lab-gpm-chave-secreta-temporaria}")
    private String secretKey;

    @Value("${app.jwt.expiration:3600000}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Gera um JWT token para o usuário
     * @param usuario Usuário para o qual gerar o token
     * @return Token JWT gerado
     */
    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setIssuer("Glimpse API")
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtém o subject (email) do token
     * @param token Token JWT
     * @return Email do usuário
     */
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Obtém o ID do usuário do token
     * @param token Token JWT
     * @return ID do usuário
     */
    public Long getIdFromToken(String token) {
        return getClaims(token).get("id", Long.class);
    }

    /**
     * Obtém o nome do usuário do token
     * @param token Token JWT
     * @return Nome do usuário
     */
    public String getNomeFromToken(String token) {
        return getClaims(token).get("nome", String.class);
    }

    /**
     * Valida se o token é válido e não está expirado
     * @param token Token JWT
     * @return true se válido, false caso contrário
     */
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtém todas as claims do token
     * @param token Token JWT
     * @return Claims do token
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica se o token está expirado
     * @param token Token JWT
     * @return true se expirado, false caso contrário
     */
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
