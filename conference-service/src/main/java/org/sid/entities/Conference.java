package org.sid.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.enums.ConferenceType;
import org.sid.models.KeynoteDTO; // Le DTO externe

import java.util.Date;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Conference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Enumerated(EnumType.STRING)
    private ConferenceType type;

    @Temporal(TemporalType.DATE) // Stocke uniquement la date
    private Date date;

    private int duree; // en heures
    private int nombreInscrits;
    private double score; // Score moyen des reviews

    // L'ID du keynote qui présente cette conférence.
    // C'est la "clé étrangère" vers l'autre microservice.
    private Long keynoteId;

    // Un champ "transient" n'est PAS stocké en base de données.
    // Il ne sert qu'à contenir temporairement les infos du keynote
    // après l'appel OpenFeign.
    @Transient
    private KeynoteDTO keynote;

    // Une conférence a plusieurs reviews.
    // Si on supprime une conférence, on supprime aussi ses reviews.
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
}