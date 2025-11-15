package org.sid.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.dto.KeynoteDTO;
import org.sid.entities.Keynote;
import org.sid.mappers.KeynoteMapper;
import org.sid.repository.KeynoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation de la couche Service.
 * @Service indique à Spring que c'est un Bean de service.
 * @Transactional gère les transactions avec la base de données.
 */
@Service
@Transactional
@AllArgsConstructor // Injection de dépendances via le constructeur
@Slf4j // Logger
public class KeynoteServiceImpl implements KeynoteService {

    private final KeynoteRepository keynoteRepository;
    private final KeynoteMapper keynoteMapper;

    @Override
    public KeynoteDTO saveKeynote(KeynoteDTO keynoteDTO) {
        log.info("Sauvegarde d'un nouveau Keynote: {}", keynoteDTO.getEmail());
        // 1. Convertir DTO en Entité
        Keynote keynote = keynoteMapper.fromKeynoteDTO(keynoteDTO);
        // 2. Sauvegarder l'entité
        Keynote savedKeynote = keynoteRepository.save(keynote);
        // 3. Re-convertir l'entité sauvegardée en DTO et la retourner
        return keynoteMapper.toKeynoteDTO(savedKeynote);
    }

    @Override
    public KeynoteDTO getKeynoteById(Long id) {
        log.info("Recherche du Keynote ID: {}", id);
        Keynote keynote = keynoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keynote non trouvé avec l'ID: " + id));
        return keynoteMapper.toKeynoteDTO(keynote);
    }

    @Override
    public List<KeynoteDTO> listKeynotes() {
        log.info("Récupération de la liste de tous les keynotes");
        List<Keynote> keynotes = keynoteRepository.findAll();
        // Conversion de la liste d'entités en liste de DTOs
        return keynotes.stream()
                .map(keynoteMapper::toKeynoteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public KeynoteDTO updateKeynote(Long id, KeynoteDTO keynoteDTO) {
        log.info("Mise à jour du Keynote ID: {}", id);
        // 1. Vérifier que le keynote existe
        Keynote existingKeynote = keynoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keynote non trouvé avec l'ID: " + id));

        // 2. Mettre à jour les champs
        existingKeynote.setNom(keynoteDTO.getNom());
        existingKeynote.setPrenom(keynoteDTO.getPrenom());
        existingKeynote.setEmail(keynoteDTO.getEmail());
        existingKeynote.setFonction(keynoteDTO.getFonction());

        // 3. Sauvegarder (JPA fera un UPDATE car l'ID existe)
        Keynote updatedKeynote = keynoteRepository.save(existingKeynote);
        return keynoteMapper.toKeynoteDTO(updatedKeynote);
    }

    @Override
    public void deleteKeynote(Long id) {
        log.info("Suppression du Keynote ID: {}", id);
        // Vérifie d'abord s'il existe (optionnel, mais plus propre)
        if (!keynoteRepository.existsById(id)) {
            throw new RuntimeException("Keynote non trouvé avec l'ID: " + id);
        }
        keynoteRepository.deleteById(id);
    }
}