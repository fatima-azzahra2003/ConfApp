package org.sid.services;

import org.sid.dto.KeynoteDTO;
import java.util.List;

/**
 * Interface pour la logique métier (Service) du Keynote.
 * Définit les opérations CRUD que nous exposons.
 */
public interface KeynoteService {

    /**
     * Enregistre un nouveau Keynote.
     * @param keynoteDTO Le DTO du keynote à créer.
     * @return Le DTO du keynote sauvegardé (avec son ID).
     */
    KeynoteDTO saveKeynote(KeynoteDTO keynoteDTO);

    /**
     * Récupère un Keynote par son ID.
     * @param id L'ID du keynote.
     * @return Le DTO du keynote trouvé.
     * @throws RuntimeException si non trouvé.
     */
    KeynoteDTO getKeynoteById(Long id);

    /**
     * Récupère la liste de tous les Keynotes.
     * @return Une liste de KeynoteDTO.
     */
    List<KeynoteDTO> listKeynotes();

    /**
     * Met à jour un Keynote existant.
     * @param id L'ID du keynote à mettre à jour.
     * @param keynoteDTO Les nouvelles informations.
     * @return Le DTO du keynote mis à jour.
     * @throws RuntimeException si non trouvé.
     */
    KeynoteDTO updateKeynote(Long id, KeynoteDTO keynoteDTO);

    /**
     * Supprime un Keynote par son ID.
     * @param id L'ID du keynote à supprimer.
     */
    void deleteKeynote(Long id);
}