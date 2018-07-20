package charactercreator.web.rest;

import charactercreator.CharacterCreatorApp;

import charactercreator.domain.ExperiencePoint;
import charactercreator.repository.ExperiencePointRepository;
import charactercreator.service.ExperiencePointService;
import charactercreator.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static charactercreator.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExperiencePointResource REST controller.
 *
 * @see ExperiencePointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CharacterCreatorApp.class)
public class ExperiencePointResourceIntTest {

    private static final Instant DEFAULT_ACQUISITION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACQUISITION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STARTING_EXPERIENCE_POINT = false;
    private static final Boolean UPDATED_STARTING_EXPERIENCE_POINT = true;

    @Autowired
    private ExperiencePointRepository experiencePointRepository;

    

    @Autowired
    private ExperiencePointService experiencePointService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExperiencePointMockMvc;

    private ExperiencePoint experiencePoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExperiencePointResource experiencePointResource = new ExperiencePointResource(experiencePointService);
        this.restExperiencePointMockMvc = MockMvcBuilders.standaloneSetup(experiencePointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExperiencePoint createEntity(EntityManager em) {
        ExperiencePoint experiencePoint = new ExperiencePoint()
            .acquisitionDate(DEFAULT_ACQUISITION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .startingExperiencePoint(DEFAULT_STARTING_EXPERIENCE_POINT);
        return experiencePoint;
    }

    @Before
    public void initTest() {
        experiencePoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createExperiencePoint() throws Exception {
        int databaseSizeBeforeCreate = experiencePointRepository.findAll().size();

        // Create the ExperiencePoint
        restExperiencePointMockMvc.perform(post("/api/experience-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiencePoint)))
            .andExpect(status().isCreated());

        // Validate the ExperiencePoint in the database
        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeCreate + 1);
        ExperiencePoint testExperiencePoint = experiencePointList.get(experiencePointList.size() - 1);
        assertThat(testExperiencePoint.getAcquisitionDate()).isEqualTo(DEFAULT_ACQUISITION_DATE);
        assertThat(testExperiencePoint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExperiencePoint.isStartingExperiencePoint()).isEqualTo(DEFAULT_STARTING_EXPERIENCE_POINT);
    }

    @Test
    @Transactional
    public void createExperiencePointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = experiencePointRepository.findAll().size();

        // Create the ExperiencePoint with an existing ID
        experiencePoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperiencePointMockMvc.perform(post("/api/experience-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiencePoint)))
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePoint in the database
        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAcquisitionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = experiencePointRepository.findAll().size();
        // set the field null
        experiencePoint.setAcquisitionDate(null);

        // Create the ExperiencePoint, which fails.

        restExperiencePointMockMvc.perform(post("/api/experience-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiencePoint)))
            .andExpect(status().isBadRequest());

        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartingExperiencePointIsRequired() throws Exception {
        int databaseSizeBeforeTest = experiencePointRepository.findAll().size();
        // set the field null
        experiencePoint.setStartingExperiencePoint(null);

        // Create the ExperiencePoint, which fails.

        restExperiencePointMockMvc.perform(post("/api/experience-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiencePoint)))
            .andExpect(status().isBadRequest());

        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExperiencePoints() throws Exception {
        // Initialize the database
        experiencePointRepository.saveAndFlush(experiencePoint);

        // Get all the experiencePointList
        restExperiencePointMockMvc.perform(get("/api/experience-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiencePoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].acquisitionDate").value(hasItem(DEFAULT_ACQUISITION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startingExperiencePoint").value(hasItem(DEFAULT_STARTING_EXPERIENCE_POINT.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getExperiencePoint() throws Exception {
        // Initialize the database
        experiencePointRepository.saveAndFlush(experiencePoint);

        // Get the experiencePoint
        restExperiencePointMockMvc.perform(get("/api/experience-points/{id}", experiencePoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(experiencePoint.getId().intValue()))
            .andExpect(jsonPath("$.acquisitionDate").value(DEFAULT_ACQUISITION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startingExperiencePoint").value(DEFAULT_STARTING_EXPERIENCE_POINT.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingExperiencePoint() throws Exception {
        // Get the experiencePoint
        restExperiencePointMockMvc.perform(get("/api/experience-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExperiencePoint() throws Exception {
        // Initialize the database
        experiencePointService.save(experiencePoint);

        int databaseSizeBeforeUpdate = experiencePointRepository.findAll().size();

        // Update the experiencePoint
        ExperiencePoint updatedExperiencePoint = experiencePointRepository.findById(experiencePoint.getId()).get();
        // Disconnect from session so that the updates on updatedExperiencePoint are not directly saved in db
        em.detach(updatedExperiencePoint);
        updatedExperiencePoint
            .acquisitionDate(UPDATED_ACQUISITION_DATE)
            .description(UPDATED_DESCRIPTION)
            .startingExperiencePoint(UPDATED_STARTING_EXPERIENCE_POINT);

        restExperiencePointMockMvc.perform(put("/api/experience-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExperiencePoint)))
            .andExpect(status().isOk());

        // Validate the ExperiencePoint in the database
        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeUpdate);
        ExperiencePoint testExperiencePoint = experiencePointList.get(experiencePointList.size() - 1);
        assertThat(testExperiencePoint.getAcquisitionDate()).isEqualTo(UPDATED_ACQUISITION_DATE);
        assertThat(testExperiencePoint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExperiencePoint.isStartingExperiencePoint()).isEqualTo(UPDATED_STARTING_EXPERIENCE_POINT);
    }

    @Test
    @Transactional
    public void updateNonExistingExperiencePoint() throws Exception {
        int databaseSizeBeforeUpdate = experiencePointRepository.findAll().size();

        // Create the ExperiencePoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExperiencePointMockMvc.perform(put("/api/experience-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiencePoint)))
            .andExpect(status().isBadRequest());

        // Validate the ExperiencePoint in the database
        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExperiencePoint() throws Exception {
        // Initialize the database
        experiencePointService.save(experiencePoint);

        int databaseSizeBeforeDelete = experiencePointRepository.findAll().size();

        // Get the experiencePoint
        restExperiencePointMockMvc.perform(delete("/api/experience-points/{id}", experiencePoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExperiencePoint> experiencePointList = experiencePointRepository.findAll();
        assertThat(experiencePointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperiencePoint.class);
        ExperiencePoint experiencePoint1 = new ExperiencePoint();
        experiencePoint1.setId(1L);
        ExperiencePoint experiencePoint2 = new ExperiencePoint();
        experiencePoint2.setId(experiencePoint1.getId());
        assertThat(experiencePoint1).isEqualTo(experiencePoint2);
        experiencePoint2.setId(2L);
        assertThat(experiencePoint1).isNotEqualTo(experiencePoint2);
        experiencePoint1.setId(null);
        assertThat(experiencePoint1).isNotEqualTo(experiencePoint2);
    }
}
