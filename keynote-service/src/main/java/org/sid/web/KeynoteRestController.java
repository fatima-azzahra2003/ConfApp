package org.sid.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.sid.dto.KeynoteDTO;
import org.sid.services.KeynoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller pour exposer l'API de gestion des Keynotes.
 */
@RestController
@RequestMapping("/api/v1/keynotes") // Le chemin de base de notre API
@AllArgsConstructor
@Tag(name = "Keynote API", description = "API pour le CRUD des Keynotes") // Pour OpenAPI/Swagger
public class KeynoteRestController {

    private final KeynoteService keynoteService;

    @Operation(summary = "Récupérer tous les keynotes")
    @ApiResponse(responseCode = "200", description = "Liste des keynotes trouvée")
    @GetMapping
    public ResponseEntity<List<KeynoteDTO>> getAllKeynotes() {
        List<KeynoteDTO> keynotes = keynoteService.listKeynotes();
        return ResponseEntity.ok(keynotes);
    }

    @Operation(summary = "Récupérer un keynote par son ID")
    @ApiResponse(responseCode = "200", description = "Keynote trouvé")
    @ApiResponse(responseCode = "404", description = "Keynote non trouvé")
    @GetMapping("/{id}")
    public ResponseEntity<KeynoteDTO> getKeynoteById(@PathVariable Long id) {
        try {
            KeynoteDTO keynote = keynoteService.getKeynoteById(id);
            return ResponseEntity.ok(keynote);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Créer un nouveau keynote")
    @ApiResponse(responseCode = "201", description = "Keynote créé avec succès")
    @PostMapping
    public ResponseEntity<KeynoteDTO> saveKeynote(@RequestBody KeynoteDTO keynoteDTO) {
        KeynoteDTO savedKeynote = keynoteService.saveKeynote(keynoteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedKeynote);
    }

    @Operation(summary = "Mettre à jour un keynote existant")
    @ApiResponse(responseCode = "200", description = "Keynote mis à jour")
    @ApiResponse(responseCode = "404", description = "Keynote non trouvé")
    @PutMapping("/{id}")
    public ResponseEntity<KeynoteDTO> updateKeynote(@PathVariable Long id, @RequestBody KeynoteDTO keynoteDTO) {
        try {
            KeynoteDTO updatedKeynote = keynoteService.updateKeynote(id, keynoteDTO);
            return ResponseEntity.ok(updatedKeynote);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Supprimer un keynote")
    @ApiResponse(responseCode = "204", description = "Keynote supprimé")
    @ApiResponse(responseCode = "404", description = "Keynote non trouvé")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeynote(@PathVariable Long id) {
        try {
            keynoteService.deleteKeynote(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}