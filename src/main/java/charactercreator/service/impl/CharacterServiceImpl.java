package charactercreator.service.impl;

import charactercreator.service.CharacterService;
import charactercreator.domain.Character;
import charactercreator.repository.CharacterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Character.
 */
@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    private final Logger log = LoggerFactory.getLogger(CharacterServiceImpl.class);

    private final CharacterRepository characterRepository;

    public CharacterServiceImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    /**
     * Save a character.
     *
     * @param character the entity to save
     * @return the persisted entity
     */
    @Override
    public Character save(Character character) {
        log.debug("Request to save Character : {}", character);        return characterRepository.save(character);
    }

    /**
     * Get all the characters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Character> findAll(Pageable pageable) {
        log.debug("Request to get all Characters");
        return characterRepository.findAll(pageable);
    }

    /**
     * Get all the Character with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Character> findAllWithEagerRelationships(Pageable pageable) {
        return characterRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one character by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Character> findOne(Long id) {
        log.debug("Request to get Character : {}", id);
        return characterRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the character by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Character : {}", id);
        characterRepository.deleteById(id);
    }
}
