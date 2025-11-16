package org.sid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuration de sécurité pour la Gateway (qui utilise WebFlux, pas MVC)
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // Les requêtes vers les services de ressources (keynote, conference)
                        // DOIVENT être authentifiées
                        .pathMatchers("/api/v1/keynotes/**", "/api/v1/conferences/**").authenticated()
                        // Toutes les autres requêtes sont autorisées (ex: Eureka, etc.)
                        .anyExchange().permitAll()
                )
                // On active la validation des tokens JWT (Resource Server)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}