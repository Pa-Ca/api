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
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;

import com.paca.paca.auth.model.JwtBlackList;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.auth.repository.JwtBlackListRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtBlackListRepository jwtBlackListRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, Boolean isRefresh) {
        return generateToken(new HashMap<>(), userDetails, isRefresh);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Boolean isRefresh) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(
                        System.currentTimeMillis() + (
                            isRefresh ? AuthenticationStatics.Jwt.REFRESH_EXPIRATION :
                                        AuthenticationStatics.Jwt.TOKEN_EXPIRATION
                            )
                ))
                .claim("isRefresh", isRefresh)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(token) && 
                !jwtBlackListRepository.findByToken(token).isPresent();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenRefresh(String token) {
        Claims claims = extractAllClaims(token);
        return (boolean) claims.get("isRefresh");
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
        byte[] keyBytes = Decoders.BASE64.decode(AuthenticationStatics.Jwt.SECRET_KEY);
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
