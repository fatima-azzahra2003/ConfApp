package org.sid.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * C'EST LE POINT CLÉ.
 * Ceci est un "modèle" (une copie) du KeynoteDTO qui existe dans le keynote-service.
 * Nous en avons besoin pour que OpenFeign sache comment "désérialiser" (lire)
 * la réponse JSON du keynote-service.
 * Il doit avoir exactements les mêmes champs.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class KeynoteDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}