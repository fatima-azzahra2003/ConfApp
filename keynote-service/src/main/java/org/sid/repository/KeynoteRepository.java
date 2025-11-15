package org.sid.repository;

import org.sid.entities.Keynote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface Repository (DAO) pour l'entité Keynote.
 * Spring Data JPA va automatiquement implémenter les méthodes CRUD (findById, save, delete...).
 */
@Repository
public interface KeynoteRepository extends JpaRepository<Keynote, Long> {
    // Nous pouvons ajouter des méthodes de recherche personnalisées ici si besoin.
}