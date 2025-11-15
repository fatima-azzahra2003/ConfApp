package org.sid.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP) // Stocke date + heure
    private Date date;

    @Column(columnDefinition = "TEXT") // Pour un texte long
    private String texte;

    private int note; // Note de 1 à 5

    // Plusieurs reviews appartiennent à UNE conférence
    @ManyToOne
    @JoinColumn(name = "conference_id") // Clé étrangère
    private Conference conference;
}