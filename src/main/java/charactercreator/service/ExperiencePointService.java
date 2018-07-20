package charactercreator.service;

import charactercreator.domain.ExperiencePoint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ExperiencePoint.
 */
public interface ExperiencePointService {

    /**
     * Save a experiencePoint.
     *
     * @param experiencePoint the entity to save
     * @return the persisted entity
     */
    ExperiencePoint save(ExperiencePoint experiencePoint);

    /**
     * Get all the experiencePoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExperiencePoint> findAll(Pageable pageable);


    /**
     * Get the "id" experiencePoint.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ExperiencePoint> findOne(Long id);

    /**
     * Delete the "id" experiencePoint.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
