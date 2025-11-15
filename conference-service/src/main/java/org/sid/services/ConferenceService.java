package org.sid.services;

import org.sid.dto.ConferenceDTO;
import java.util.List;

/**
 * Interface pour la logique métier (Service) de la Conférence.
 */
public interface ConferenceService {

    /**
     * Crée une nouvelle conférence.
     * @param conferenceDTO Le DTO de la conférence à créer.
     * @return Le DTO de la conférence sauvegardée.
     */
    ConferenceDTO saveConference(ConferenceDTO conferenceDTO);

    /**
     * Récupère une conférence par son ID, en incluant les détails du Keynote.
     * @param id L'ID de la conférence.
     * @return Le DTO complet de la conférence.
     * @throws RuntimeException si la conférence n'est pas trouvée.
     */
    ConferenceDTO getConferenceById(Long id);

    /**
     * Récupère la liste de toutes les conférences.
     * @return Une liste de ConferenceDTO.
     */
    List<ConferenceDTO> listConferences();

    // Note : On pourrait ajouter update/delete, mais on se concentre sur le principal.
}