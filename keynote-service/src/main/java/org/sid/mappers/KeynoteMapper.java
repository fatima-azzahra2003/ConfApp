package org.sid.mappers;

import org.mapstruct.Mapper;
import org.sid.dto.KeynoteDTO;
import org.sid.entities.Keynote;

/**
 * Interface Mapper (MapStruct) pour la conversion entre Entité et DTO.
 * MapStruct va générer l'implémentation de cette interface lors de la compilation.
 */
@Mapper(componentModel = "spring") // Indique à MapStruct de générer un Bean Spring
public interface KeynoteMapper {

    /**
     * Convertit une Entité Keynote en KeynoteDTO.
     */
    KeynoteDTO toKeynoteDTO(Keynote keynote);

    /**
     * Convertit un KeynoteDTO en Entité Keynote.
     */
    Keynote fromKeynoteDTO(KeynoteDTO keynoteDTO);
}