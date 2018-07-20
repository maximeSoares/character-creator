package charactercreator.service.impl;

import charactercreator.service.CharacterRaceService;
import charactercreator.domain.CharacterRace;
import charactercreator.repository.CharacterRaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing CharacterRace.
 */
@Service
@Transactional
public class CharacterRaceServiceImpl implements CharacterRaceService {

    private final Logger log = LoggerFactory.getLogger(CharacterRaceServiceImpl.class);

    private final CharacterRaceRepository characterRaceRepository;

    public CharacterRaceServiceImpl(CharacterRaceRepository characterRaceRepository) {
        this.characterRaceRepository = characterRaceRepository;
    }

    /**
     * Save a characterRace.
     *
     * @param characterRace the entity to save
     * @return the persisted entity
     */
    @Override
    public CharacterRace save(CharacterRace characterRace) {
        log.debug("Request to save CharacterRace : {}", characterRace);        return characterRaceRepository.save(characterRace);
    }

    /**
     * Get all the characterRaces.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CharacterRace> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterRaces");
        return characterRaceRepository.findAll(pageable);
    }

    /**
     * Get all the CharacterRace with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<CharacterRace> findAllWithEagerRelationships(Pageable pageable) {
        return characterRaceRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one characterRace by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CharacterRace> findOne(Long id) {
        log.debug("Request to get CharacterRace : {}", id);
        return characterRaceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the characterRace by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CharacterRace : {}", id);
        characterRaceRepository.deleteById(id);
    }
}
