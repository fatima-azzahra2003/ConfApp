package org.sid.repository;

import org.sid.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface Repository (DAO) pour l'entité Conference.
 */
@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}