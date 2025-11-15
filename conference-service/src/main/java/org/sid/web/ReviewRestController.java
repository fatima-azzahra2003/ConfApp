package org.sid.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.sid.dto.ReviewDTO;
import org.sid.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conferences") // On se greffe sur l'API des conférences
@AllArgsConstructor
@Tag(name = "Review API", description = "API pour la gestion des Reviews d'une Conférence")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Operation(summary = "Ajouter une review à une conférence")
    @PostMapping("/{conferenceId}/reviews")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long conferenceId,
            @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO savedReview = reviewService.addReviewToConference(conferenceId, reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Récupérer toutes les reviews d'une conférence")
    @GetMapping("/{conferenceId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long conferenceId) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsByConferenceId(conferenceId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}