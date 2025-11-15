package org.sid.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.clients.KeynoteRestClient;
import org.sid.dto.ConferenceDTO;
import org.sid.entities.Conference;
import org.sid.mappers.ConferenceMapper;
import org.sid.models.KeynoteDTO;
import org.sid.repository.ConferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final ConferenceMapper conferenceMapper;
    private final KeynoteRestClient keynoteRestClient; // <-- INJECTION DU CLIENT FEIGN

    @Override
    public ConferenceDTO saveConference(ConferenceDTO conferenceDTO) {
        log.info("Sauvegarde d'une nouvelle Conférence pour le Keynote ID: {}", conferenceDTO.getKeynoteId());

        // On peut vérifier si le keynote existe avant de sauvegarder
        try {
            keynoteRestClient.findKeynoteById(conferenceDTO.getKeynoteId());
        } catch (Exception e) {
            log.warn("Keynote ID {} non trouvé, mais sauvegarde quand même...", conferenceDTO.getKeynoteId());
            // Selon la logique métier, on pourrait lever une exception ici
        }

        Conference conference = conferenceMapper.fromConferenceDTO(conferenceDTO);
        Conference savedConference = conferenceRepository.save(conference);
        return conferenceMapper.toConferenceDTO(savedConference);
    }

    @Override
    public ConferenceDTO getConferenceById(Long id) {
        log.info("Recherche de la Conférence ID: {}", id);

        // 1. Trouver la conférence dans notre BDD
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conférence non trouvée avec l'ID: " + id));

        // 2. APPEL MICRO-SERVICE (via OpenFeign)
        // Récupérer les détails du keynote depuis le keynote-service
        log.info("Appel du keynote-service pour l'ID: {}", conference.getKeynoteId());
        KeynoteDTO keynote = keynoteRestClient.findKeynoteById(conference.getKeynoteId());

        // 3. Placer le keynote (objet DTO) dans le champ @Transient de l'entité
        conference.setKeynote(keynote);

        // 4. Mapper l'entité (maintenant complète) en DTO
        return conferenceMapper.toConferenceDTO(conference);
    }

    @Override
    public List<ConferenceDTO> listConferences() {
        log.info("Récupération de la liste des conférences");
        List<Conference> conferences = conferenceRepository.findAll();

        // Pour chaque conférence, on appelle le keynote-service
        return conferences.stream()
                .map(conference -> {
                    // APPEL MICRO-SERVICE (N+1, pas optimal, mais simple)
                    try {
                        KeynoteDTO keynote = keynoteRestClient.findKeynoteById(conference.getKeynoteId());
                        conference.setKeynote(keynote);
                    } catch (Exception e) {
                        log.warn("Impossible de charger le keynote ID: {}", conference.getKeynoteId());
                        // On peut utiliser le fallback ici aussi si on veut
                        conference.setKeynote(keynoteRestClient.getDefaultKeynote(conference.getKeynoteId(), e));
                    }
                    return conferenceMapper.toConferenceDTO(conference);
                })
                .collect(Collectors.toList());
    }
}