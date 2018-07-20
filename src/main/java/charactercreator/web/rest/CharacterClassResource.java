package charactercreator.web.rest;

import com.codahale.metrics.annotation.Timed;
import charactercreator.domain.CharacterClass;
import charactercreator.service.CharacterClassService;
import charactercreator.web.rest.errors.BadRequestAlertException;
import charactercreator.web.rest.util.HeaderUtil;
import charactercreator.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CharacterClass.
 */
@RestController
@RequestMapping("/api")
public class CharacterClassResource {

    private final Logger log = LoggerFactory.getLogger(CharacterClassResource.class);

    private static final String ENTITY_NAME = "characterClass";

    private final CharacterClassService characterClassService;

    public CharacterClassResource(CharacterClassService characterClassService) {
        this.characterClassService = characterClassService;
    }

    /**
     * POST  /character-classes : Create a new characterClass.
     *
     * @param characterClass the characterClass to create
     * @return the ResponseEntity with status 201 (Created) and with body the new characterClass, or with status 400 (Bad Request) if the characterClass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/character-classes")
    @Timed
    public ResponseEntity<CharacterClass> createCharacterClass(@Valid @RequestBody CharacterClass characterClass) throws URISyntaxException {
        log.debug("REST request to save CharacterClass : {}", characterClass);
        if (characterClass.getId() != null) {
            throw new BadRequestAlertException("A new characterClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterClass result = characterClassService.save(characterClass);
        return ResponseEntity.created(new URI("/api/character-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /character-classes : Updates an existing characterClass.
     *
     * @param characterClass the characterClass to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated characterClass,
     * or with status 400 (Bad Request) if the characterClass is not valid,
     * or with status 500 (Internal Server Error) if the characterClass couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/character-classes")
    @Timed
    public ResponseEntity<CharacterClass> updateCharacterClass(@Valid @RequestBody CharacterClass characterClass) throws URISyntaxException {
        log.debug("REST request to update CharacterClass : {}", characterClass);
        if (characterClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CharacterClass result = characterClassService.save(characterClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, characterClass.getId().toString()))
            .body(result);
    }

    /**
     * GET  /character-classes : get all the characterClasses.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of characterClasses in body
     */
    @GetMapping("/character-classes")
    @Timed
    public ResponseEntity<List<CharacterClass>> getAllCharacterClasses(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of CharacterClasses");
        Page<CharacterClass> page;
        if (eagerload) {
            page = characterClassService.findAllWithEagerRelationships(pageable);
        } else {
            page = characterClassService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/character-classes?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /character-classes/:id : get the "id" characterClass.
     *
     * @param id the id of the characterClass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the characterClass, or with status 404 (Not Found)
     */
    @GetMapping("/character-classes/{id}")
    @Timed
    public ResponseEntity<CharacterClass> getCharacterClass(@PathVariable Long id) {
        log.debug("REST request to get CharacterClass : {}", id);
        Optional<CharacterClass> characterClass = characterClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterClass);
    }

    /**
     * DELETE  /character-classes/:id : delete the "id" characterClass.
     *
     * @param id the id of the characterClass to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/character-classes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCharacterClass(@PathVariable Long id) {
        log.debug("REST request to delete CharacterClass : {}", id);
        characterClassService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
