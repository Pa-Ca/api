package com.paca.paca.auth.service;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.paca.paca.user.model.User;
import com.paca.paca.auth.model.JwtBlackList;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${auth.expiration.token}")
    private Integer EXPIRATION_TOKEN;

    @Value("${auth.expiration.refresh}")
    private Integer EXPIRATION_REFRESH;

    @Value("${auth.expiration.reset.password}")
    private Integer EXPIRATION_RESET_PASSWORD;

    @Value("${auth.expiration.verify.email}")
    private Integer EXPIRATION_VERIFY_EMAIL;

    @Value("${auth.secret.key}")
    private String SECRET_KEY;

    public enum TokenType {
        TOKEN, REFRESH, RESET_PASSWORD, VERIFY_EMAIL
    }

    private final JwtBlackListRepository jwtBlackListRepository;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user, TokenType type) {
        return generateToken(new HashMap<>(), user, type);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User user,
            TokenType type) {
        Integer expiration = 0;

        switch (type) {
            case TOKEN:
                expiration = EXPIRATION_TOKEN;
                break;
            case REFRESH:
                expiration = EXPIRATION_REFRESH;
                break;
            case RESET_PASSWORD:
                expiration = EXPIRATION_RESET_PASSWORD;
                break;
            case VERIFY_EMAIL:
                expiration = EXPIRATION_VERIFY_EMAIL;
                break;
        }

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("type", type.name())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token) &&
                !jwtBlackListRepository.findByToken(token).isPresent();
    }

    public boolean isTokenValid(String token, User user) {
        final String email = extractEmail(token);
        return (email.equals(user.getEmail())) &&
                !isTokenExpired(token) &&
                !jwtBlackListRepository.findByToken(token).isPresent();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isToken(String token) {
        Claims claims = extractAllClaims(token);
        return ((String) claims.get("type")).equals(TokenType.TOKEN.name());
    }

    public boolean isTokenRefresh(String token) {
        Claims claims = extractAllClaims(token);
        return ((String) claims.get("type")).equals(TokenType.REFRESH.name());
    }

    public boolean isTokenResetPassword(String token) {
        Claims claims = extractAllClaims(token);
        return ((String) claims.get("type")).equals(TokenType.RESET_PASSWORD.name());
    }

    public boolean isTokenVerifyEmail(String token) {
        Claims claims = extractAllClaims(token);
        return ((String) claims.get("type")).equals(TokenType.VERIFY_EMAIL.name());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void addTokenToBlackList(String token) {
        try {
            JwtBlackList jwt = JwtBlackList.builder()
                    .token(token)
                    .expiration(extractExpiration(token))
                    .build();

            jwtBlackListRepository.save(jwt);
        } catch (Exception e) {
            throw new ForbiddenException("Invalid token");
        }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void deleteExpiredTokens() {
        Date now = new Date(System.currentTimeMillis());
        jwtBlackListRepository.deleteAllByExpirationLessThan(now);
    }
}
