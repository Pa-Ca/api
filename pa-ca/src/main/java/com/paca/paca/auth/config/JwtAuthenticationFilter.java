package com.paca.paca.auth.config;

import java.util.Optional;
import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.paca.paca.user.model.User;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.auth.statics.AuthenticationStatics;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ForbiddenException, ServletException, IOException {
        final String jwt;
        final String userEmail;
        final AntPathMatcher matcher = new AntPathMatcher();
        final String authHeader = request.getHeader("Authorization");

        if (matcher.match(AuthenticationStatics.Endpoint.AUTH_PATH + "/**", request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ForbiddenException("Authentication failed", 9);
        }

        jwt = authHeader.substring(7);
        try {
            userEmail = jwtService.extractEmail(jwt);
        } catch (Exception e) {
            throw new ForbiddenException("Authentication failed", 9);
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> user = userRepository.findByEmail(userEmail);
            if (user.isEmpty()) {
                throw new ForbiddenException("Authentication failed", 9);
            }
            if (jwtService.isTokenValid(jwt, user.get()) && jwtService.isToken(jwt)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.get(),
                        null,
                        user.get().getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
