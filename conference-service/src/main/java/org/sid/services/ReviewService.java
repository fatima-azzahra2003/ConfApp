package org.sid.services;

import org.sid.dto.ReviewDTO;
import java.util.List;

/**
 * Interface pour la logique métier des Reviews.
 */
public interface ReviewService {

    /**
     * Ajoute une review à une conférence.
     * @param conferenceId L'ID de la conférence à reviewer.
     * @param reviewDTO Le DTO de la review.
     * @return Le DTO de la review sauvegardée.
     */
    ReviewDTO addReviewToConference(Long conferenceId, ReviewDTO reviewDTO);

    /**
     * Récupère toutes les reviews pour une conférence donnée.
     * @param conferenceId L'ID de la conférence.
     * @return Une liste de ReviewDTO.
     */
    List<ReviewDTO> getReviewsByConferenceId(Long conferenceId);
}