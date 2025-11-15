package org.sid.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.sid.models.KeynoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interface OpenFeign pour communiquer avec le keynote-service.
 *
 * @FeignClient(name = "KEYNOTE-SERVICE")
 * 'name' DOIT correspondre au 'spring.application.name' du service distant
 * (tel qu'il est enregistré dans Eureka).
 */
@FeignClient(name = "KEYNOTE-SERVICE")
public interface KeynoteRestClient {

    /**
     * Appelle le point de terminaison GET /api/v1/keynotes/{id} du keynote-service.
     *
     * @CircuitBreaker : C'est ici qu'on active Resilience4J.
     * Si le 'keynote-service' est en panne ou met trop de temps à répondre,
     * au lieu de planter, il appellera la méthode de 'fallback'.
     *
     * CORRECTION: Ajout de ("id") à @PathVariable
     */
    @GetMapping("/api/v1/keynotes/{id}")
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "getDefaultKeynote")
    KeynoteDTO findKeynoteById(@PathVariable("id") Long id);

    /**
     * Méthode de Fallback (secours) pour le Circuit Breaker.
     * Appelée si 'findKeynoteById' échoue.
     * Elle DOIT avoir la même signature (paramètres + type de retour).
     *
     * CORRECTION: Ajout de ("id") à @PathVariable
     */
    default KeynoteDTO getDefaultKeynote(@PathVariable("id") Long id, Throwable exception) {
        // En cas d'erreur, on retourne un Keynote "par défaut"
        return KeynoteDTO.builder()
                .id(id)
                .nom("Non disponible")
                .prenom("Non disponible")
                .email("N/A")
                .fonction("Service Keynote en maintenance")
                .build();
    }
}