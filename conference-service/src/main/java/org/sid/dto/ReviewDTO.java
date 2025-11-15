package org.sid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDTO {
    private Long id;
    private Date date;
    private String texte;
    private int note;
    private Long conferenceId; // On ne met que l'ID pour éviter les boucles
}