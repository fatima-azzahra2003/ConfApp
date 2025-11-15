package org.sid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.enums.ConferenceType;
import org.sid.models.KeynoteDTO; // Le DTO externe

import java.util.Date;
import java.util.List;

/**
 * DTO pour la Conférence. C'est ce que notre API va exposer.
 * Notez qu'il contient le KeynoteDTO complet (pas seulement l'ID).
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ConferenceDTO {
    private Long id;
    private String titre;
    private ConferenceType type;
    private Date date;
    private int duree;
    private int nombreInscrits;
    private double score;
    private Long keynoteId; // On garde l'ID
    private KeynoteDTO keynote; // Et on ajoute l'objet complet
    private List<ReviewDTO> reviews;
}