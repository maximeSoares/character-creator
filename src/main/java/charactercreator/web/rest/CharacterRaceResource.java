package charactercreator.web.rest;

import com.codahale.metrics.annotation.Timed;
import charactercreator.domain.CharacterRace;
import charactercreator.service.CharacterRaceService;
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
 * REST controller for managing CharacterRace.
 */
@RestController
@RequestMapping("/api")
public class CharacterRaceResource {

    private final Logger log = LoggerFactory.getLogger(CharacterRaceResource.class);

    private static final String ENTITY_NAME = "characterRace";

    private final CharacterRaceService characterRaceService;

    public CharacterRaceResource(CharacterRaceService characterRaceService) {
        this.characterRaceService = characterRaceService;
    }

    /**
     * POST  /character-races : Create a new characterRace.
     *
     * @param characterRace the characterRace to create
     * @return the ResponseEntity with status 201 (Created) and with body the new characterRace, or with status 400 (Bad Request) if the characterRace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/character-races")
    @Timed
    public ResponseEntity<CharacterRace> createCharacterRace(@Valid @RequestBody CharacterRace characterRace) throws URISyntaxException {
        log.debug("REST request to save CharacterRace : {}", characterRace);
        if (characterRace.getId() != null) {
            throw new BadRequestAlertException("A new characterRace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharacterRace result = characterRaceService.save(characterRace);
        return ResponseEntity.created(new URI("/api/character-races/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /character-races : Updates an existing characterRace.
     *
     * @param characterRace the characterRace to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated characterRace,
     * or with status 400 (Bad Request) if the characterRace is not valid,
     * or with status 500 (Internal Server Error) if the characterRace couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/character-races")
    @Timed
    public ResponseEntity<CharacterRace> updateCharacterRace(@Valid @RequestBody CharacterRace characterRace) throws URISyntaxException {
        log.debug("REST request to update CharacterRace : {}", characterRace);
        if (characterRace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CharacterRace result = characterRaceService.save(characterRace);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, characterRace.getId().toString()))
            .body(result);
    }

    /**
     * GET  /character-races : get all the characterRaces.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of characterRaces in body
     */
    @GetMapping("/character-races")
    @Timed
    public ResponseEntity<List<CharacterRace>> getAllCharacterRaces(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of CharacterRaces");
        Page<CharacterRace> page;
        if (eagerload) {
            page = characterRaceService.findAllWithEagerRelationships(pageable);
        } else {
            page = characterRaceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/character-races?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /character-races/:id : get the "id" characterRace.
     *
     * @param id the id of the characterRace to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the characterRace, or with status 404 (Not Found)
     */
    @GetMapping("/character-races/{id}")
    @Timed
    public ResponseEntity<CharacterRace> getCharacterRace(@PathVariable Long id) {
        log.debug("REST request to get CharacterRace : {}", id);
        Optional<CharacterRace> characterRace = characterRaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(characterRace);
    }

    /**
     * DELETE  /character-races/:id : delete the "id" characterRace.
     *
     * @param id the id of the characterRace to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/character-races/{id}")
    @Timed
    public ResponseEntity<Void> deleteCharacterRace(@PathVariable Long id) {
        log.debug("REST request to delete CharacterRace : {}", id);
        characterRaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
