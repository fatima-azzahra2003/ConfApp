package org.sid.repository;

import org.sid.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface Repository (DAO) pour l'entité Review.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}