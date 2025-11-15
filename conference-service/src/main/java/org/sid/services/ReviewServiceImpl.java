package org.sid.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.dto.ReviewDTO;
import org.sid.entities.Conference;
import org.sid.entities.Review;
import org.sid.mappers.ReviewMapper;
import org.sid.repository.ConferenceRepository;
import org.sid.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ConferenceRepository conferenceRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDTO addReviewToConference(Long conferenceId, ReviewDTO reviewDTO) {
        log.info("Ajout d'une review pour la conférence ID: {}", conferenceId);

        // 1. Trouver la conférence
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conférence non trouvée: " + conferenceId));

        // 2. Convertir le DTO en entité
        Review review = reviewMapper.fromReviewDTO(reviewDTO);

        // 3. Définir les relations
        review.setConference(conference);
        review.setDate(new Date()); // Mettre la date actuelle

        // 4. Sauvegarder la review
        Review savedReview = reviewRepository.save(review);

        // 5. (Optionnel mais bien) Mettre à jour le score moyen de la conférence
        updateConferenceScore(conference);

        return reviewMapper.toReviewDTO(savedReview);
    }

    private void updateConferenceScore(Conference conference) {
        // Récupère toutes les reviews pour cette conférence (elles sont en BDD)
        List<Review> reviews = conference.getReviews();

        // Si on vient d'ajouter la première, la liste n'est peut-être pas à jour
        // On la rafraîchit (ou on ajoute juste la nouvelle review à la liste)
        // Pour faire simple, on recalcule tout :

        // On recharge la conférence avec ses reviews à jour
        Conference conferenceAvecReviews = conferenceRepository.findById(conference.getId()).get();

        if (conferenceAvecReviews.getReviews() == null || conferenceAvecReviews.getReviews().isEmpty()) {
            conference.setScore(0);
            return;
        }

        double average = conferenceAvecReviews.getReviews().stream()
                .mapToInt(Review::getNote)
                .average()
                .orElse(0.0);

        conference.setScore(average);
        conferenceRepository.save(conference); // Sauvegarde le score mis à jour
    }

    @Override
    public List<ReviewDTO> getReviewsByConferenceId(Long conferenceId) {
        log.info("Récupération des reviews pour la conférence ID: {}", conferenceId);

        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conférence non trouvée: " + conferenceId));

        return conference.getReviews().stream()
                .map(reviewMapper::toReviewDTO)
                .collect(Collectors.toList());
    }
}