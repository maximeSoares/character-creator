package charactercreator.service.impl;

import charactercreator.service.CharacterClassService;
import charactercreator.domain.CharacterClass;
import charactercreator.repository.CharacterClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing CharacterClass.
 */
@Service
@Transactional
public class CharacterClassServiceImpl implements CharacterClassService {

    private final Logger log = LoggerFactory.getLogger(CharacterClassServiceImpl.class);

    private final CharacterClassRepository characterClassRepository;

    public CharacterClassServiceImpl(CharacterClassRepository characterClassRepository) {
        this.characterClassRepository = characterClassRepository;
    }

    /**
     * Save a characterClass.
     *
     * @param characterClass the entity to save
     * @return the persisted entity
     */
    @Override
    public CharacterClass save(CharacterClass characterClass) {
        log.debug("Request to save CharacterClass : {}", characterClass);        return characterClassRepository.save(characterClass);
    }

    /**
     * Get all the characterClasses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CharacterClass> findAll(Pageable pageable) {
        log.debug("Request to get all CharacterClasses");
        return characterClassRepository.findAll(pageable);
    }

    /**
     * Get all the CharacterClass with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<CharacterClass> findAllWithEagerRelationships(Pageable pageable) {
        return characterClassRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one characterClass by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CharacterClass> findOne(Long id) {
        log.debug("Request to get CharacterClass : {}", id);
        return characterClassRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the characterClass by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CharacterClass : {}", id);
        characterClassRepository.deleteById(id);
    }
}
