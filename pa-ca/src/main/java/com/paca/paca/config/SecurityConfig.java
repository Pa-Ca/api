package com.paca.paca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // Disable Cross-Site Request Forgery (CSRF)
            .csrf(csrf -> csrf.disable())
            // The user should be authenticated for any request in the application.
            .authorizeHttpRequests( 
                auth -> auth.anyRequest().authenticated() 
            )
            // OAuth 2 with JWT configuration.
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            // Spring Security will never create an HttpSession and it will 
            // never use it to obtain the Security Context.
            .sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
            ) 
            // Spring Security’s HTTP Basic Authentication support is enabled 
            // by default. However, as soon as any servlet-based configuration 
            // is provided, HTTP Basic must be explicitly provided.
            .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    public InMemoryUserDetailsManager users() {
        // The following configuration will create an in-memory user using the 
        // NoOpPasswordEncoder This is a password encoder that does nothing 
        // and is useful for testing but should NOT be used in production.
        return new InMemoryUserDetailsManager(
            User.withUsername("amin")
                .password("{noop}password")
                .authorities("read")
                .build()
        );
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey())
            .privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}