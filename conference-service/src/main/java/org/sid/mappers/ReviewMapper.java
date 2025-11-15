package org.sid.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sid.dto.ReviewDTO;
import org.sid.entities.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    /**
     * Convertit l'entité Review en DTO.
     * On mappe manuellement l'ID de la conférence.
     */
    @Mapping(source = "conference.id", target = "conferenceId")
    ReviewDTO toReviewDTO(Review review);

    /**
     * Convertit le DTO Review en Entité.
     * On ignore la conférence, car elle sera définie manuellement
     * dans la couche Service (pour éviter les problèmes de conversion).
     */
    @Mapping(target = "conference", ignore = true)
    Review fromReviewDTO(ReviewDTO reviewDTO);
}