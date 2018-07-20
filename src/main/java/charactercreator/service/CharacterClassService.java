package charactercreator.service;

import charactercreator.domain.CharacterClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CharacterClass.
 */
public interface CharacterClassService {

    /**
     * Save a characterClass.
     *
     * @param characterClass the entity to save
     * @return the persisted entity
     */
    CharacterClass save(CharacterClass characterClass);

    /**
     * Get all the characterClasses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CharacterClass> findAll(Pageable pageable);

    /**
     * Get all the CharacterClass with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<CharacterClass> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" characterClass.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CharacterClass> findOne(Long id);

    /**
     * Delete the "id" characterClass.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
