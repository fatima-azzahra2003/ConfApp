package org.sid.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sid.dto.ConferenceDTO;
import org.sid.entities.Conference;

@Mapper(componentModel = "spring", uses = {ReviewMapper.class}) // Indique à MapStruct d'utiliser aussi le ReviewMapper
public interface ConferenceMapper {

    /**
     * Convertit l'entité Conference en DTO.
     * 'keynote' (l'objet) est déjà un champ @Transient dans l'entité,
     * donc MapStruct le copiera automatiquement dans le DTO.
     * 'reviews' sera automatiquement converti par le ReviewMapper.
     */
    ConferenceDTO toConferenceDTO(Conference conference);

    /**
     * Convertit le DTO Conference en Entité.
     * On ignore 'keynote' (l'objet DTO externe) car il n'est pas
     * stocké en BDD (seul keynoteId l'est).
     * On ignore 'reviews' car ils seront gérés séparément.
     */
    @Mapping(target = "keynote", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Conference fromConferenceDTO(ConferenceDTO conferenceDTO);
}