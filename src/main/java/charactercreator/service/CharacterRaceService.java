package charactercreator.service;

import charactercreator.domain.CharacterRace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CharacterRace.
 */
public interface CharacterRaceService {

    /**
     * Save a characterRace.
     *
     * @param characterRace the entity to save
     * @return the persisted entity
     */
    CharacterRace save(CharacterRace characterRace);

    /**
     * Get all the characterRaces.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CharacterRace> findAll(Pageable pageable);

    /**
     * Get all the CharacterRace with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<CharacterRace> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" characterRace.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CharacterRace> findOne(Long id);

    /**
     * Delete the "id" characterRace.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
