package org.sid.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.sid.dto.ConferenceDTO;
import org.sid.services.ConferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conferences") // Le chemin de base de notre API
@AllArgsConstructor
@Tag(name = "Conference API", description = "API pour le CRUD des Conférences")
public class ConferenceRestController {

    private final ConferenceService conferenceService;

    @Operation(summary = "Récupérer toutes les conférences (avec détails du keynote)")
    @GetMapping
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        return ResponseEntity.ok(conferenceService.listConferences());
    }

    @Operation(summary = "Récupérer une conférence par ID (avec détails du keynote)")
    @ApiResponse(responseCode = "200", description = "Conférence trouvée")
    @ApiResponse(responseCode = "404", description = "Conférence non trouvée")
    @GetMapping("/{id}")
    public ResponseEntity<ConferenceDTO> getConferenceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(conferenceService.getConferenceById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Créer une nouvelle conférence")
    @ApiResponse(responseCode = "201", description = "Conférence créée")
    @PostMapping
    public ResponseEntity<ConferenceDTO> saveConference(@RequestBody ConferenceDTO conferenceDTO) {
        // Note : le DTO en entrée n'a besoin que du "keynoteId"
        ConferenceDTO savedConference = conferenceService.saveConference(conferenceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedConference);
    }
}