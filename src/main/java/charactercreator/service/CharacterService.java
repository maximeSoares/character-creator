package charactercreator.service;

import charactercreator.domain.Character;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Character.
 */
public interface CharacterService {

    /**
     * Save a character.
     *
     * @param character the entity to save
     * @return the persisted entity
     */
    Character save(Character character);

    /**
     * Get all the characters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Character> findAll(Pageable pageable);

    /**
     * Get all the Character with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Character> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" character.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Character> findOne(Long id);

    /**
     * Delete the "id" character.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
