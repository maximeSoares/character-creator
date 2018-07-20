package charactercreator.web.rest;

import com.codahale.metrics.annotation.Timed;
import charactercreator.domain.ExperiencePoint;
import charactercreator.service.ExperiencePointService;
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
 * REST controller for managing ExperiencePoint.
 */
@RestController
@RequestMapping("/api")
public class ExperiencePointResource {

    private final Logger log = LoggerFactory.getLogger(ExperiencePointResource.class);

    private static final String ENTITY_NAME = "experiencePoint";

    private final ExperiencePointService experiencePointService;

    public ExperiencePointResource(ExperiencePointService experiencePointService) {
        this.experiencePointService = experiencePointService;
    }

    /**
     * POST  /experience-points : Create a new experiencePoint.
     *
     * @param experiencePoint the experiencePoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new experiencePoint, or with status 400 (Bad Request) if the experiencePoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/experience-points")
    @Timed
    public ResponseEntity<ExperiencePoint> createExperiencePoint(@Valid @RequestBody ExperiencePoint experiencePoint) throws URISyntaxException {
        log.debug("REST request to save ExperiencePoint : {}", experiencePoint);
        if (experiencePoint.getId() != null) {
            throw new BadRequestAlertException("A new experiencePoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExperiencePoint result = experiencePointService.save(experiencePoint);
        return ResponseEntity.created(new URI("/api/experience-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /experience-points : Updates an existing experiencePoint.
     *
     * @param experiencePoint the experiencePoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated experiencePoint,
     * or with status 400 (Bad Request) if the experiencePoint is not valid,
     * or with status 500 (Internal Server Error) if the experiencePoint couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/experience-points")
    @Timed
    public ResponseEntity<ExperiencePoint> updateExperiencePoint(@Valid @RequestBody ExperiencePoint experiencePoint) throws URISyntaxException {
        log.debug("REST request to update ExperiencePoint : {}", experiencePoint);
        if (experiencePoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExperiencePoint result = experiencePointService.save(experiencePoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, experiencePoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /experience-points : get all the experiencePoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of experiencePoints in body
     */
    @GetMapping("/experience-points")
    @Timed
    public ResponseEntity<List<ExperiencePoint>> getAllExperiencePoints(Pageable pageable) {
        log.debug("REST request to get a page of ExperiencePoints");
        Page<ExperiencePoint> page = experiencePointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/experience-points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /experience-points/:id : get the "id" experiencePoint.
     *
     * @param id the id of the experiencePoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the experiencePoint, or with status 404 (Not Found)
     */
    @GetMapping("/experience-points/{id}")
    @Timed
    public ResponseEntity<ExperiencePoint> getExperiencePoint(@PathVariable Long id) {
        log.debug("REST request to get ExperiencePoint : {}", id);
        Optional<ExperiencePoint> experiencePoint = experiencePointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(experiencePoint);
    }

    /**
     * DELETE  /experience-points/:id : delete the "id" experiencePoint.
     *
     * @param id the id of the experiencePoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/experience-points/{id}")
    @Timed
    public ResponseEntity<Void> deleteExperiencePoint(@PathVariable Long id) {
        log.debug("REST request to delete ExperiencePoint : {}", id);
        experiencePointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
