package charactercreator.service.impl;

import charactercreator.service.ExperiencePointService;
import charactercreator.domain.ExperiencePoint;
import charactercreator.repository.ExperiencePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ExperiencePoint.
 */
@Service
@Transactional
public class ExperiencePointServiceImpl implements ExperiencePointService {

    private final Logger log = LoggerFactory.getLogger(ExperiencePointServiceImpl.class);

    private final ExperiencePointRepository experiencePointRepository;

    public ExperiencePointServiceImpl(ExperiencePointRepository experiencePointRepository) {
        this.experiencePointRepository = experiencePointRepository;
    }

    /**
     * Save a experiencePoint.
     *
     * @param experiencePoint the entity to save
     * @return the persisted entity
     */
    @Override
    public ExperiencePoint save(ExperiencePoint experiencePoint) {
        log.debug("Request to save ExperiencePoint : {}", experiencePoint);        return experiencePointRepository.save(experiencePoint);
    }

    /**
     * Get all the experiencePoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExperiencePoint> findAll(Pageable pageable) {
        log.debug("Request to get all ExperiencePoints");
        return experiencePointRepository.findAll(pageable);
    }


    /**
     * Get one experiencePoint by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExperiencePoint> findOne(Long id) {
        log.debug("Request to get ExperiencePoint : {}", id);
        return experiencePointRepository.findById(id);
    }

    /**
     * Delete the experiencePoint by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExperiencePoint : {}", id);
        experiencePointRepository.deleteById(id);
    }
}
