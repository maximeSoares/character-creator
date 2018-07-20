package charactercreator.web.rest;

import charactercreator.CharacterCreatorApp;

import charactercreator.domain.CharacterRace;
import charactercreator.repository.CharacterRaceRepository;
import charactercreator.service.CharacterRaceService;
import charactercreator.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static charactercreator.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CharacterRaceResource REST controller.
 *
 * @see CharacterRaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CharacterCreatorApp.class)
public class CharacterRaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CharacterRaceRepository characterRaceRepository;
    @Mock
    private CharacterRaceRepository characterRaceRepositoryMock;
    
    @Mock
    private CharacterRaceService characterRaceServiceMock;

    @Autowired
    private CharacterRaceService characterRaceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCharacterRaceMockMvc;

    private CharacterRace characterRace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CharacterRaceResource characterRaceResource = new CharacterRaceResource(characterRaceService);
        this.restCharacterRaceMockMvc = MockMvcBuilders.standaloneSetup(characterRaceResource)
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
    public static CharacterRace createEntity(EntityManager em) {
        CharacterRace characterRace = new CharacterRace()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return characterRace;
    }

    @Before
    public void initTest() {
        characterRace = createEntity(em);
    }

    @Test
    @Transactional
    public void createCharacterRace() throws Exception {
        int databaseSizeBeforeCreate = characterRaceRepository.findAll().size();

        // Create the CharacterRace
        restCharacterRaceMockMvc.perform(post("/api/character-races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterRace)))
            .andExpect(status().isCreated());

        // Validate the CharacterRace in the database
        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterRace testCharacterRace = characterRaceList.get(characterRaceList.size() - 1);
        assertThat(testCharacterRace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacterRace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCharacterRaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = characterRaceRepository.findAll().size();

        // Create the CharacterRace with an existing ID
        characterRace.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterRaceMockMvc.perform(post("/api/character-races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterRace)))
            .andExpect(status().isBadRequest());

        // Validate the CharacterRace in the database
        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRaceRepository.findAll().size();
        // set the field null
        characterRace.setName(null);

        // Create the CharacterRace, which fails.

        restCharacterRaceMockMvc.perform(post("/api/character-races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterRace)))
            .andExpect(status().isBadRequest());

        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRaceRepository.findAll().size();
        // set the field null
        characterRace.setDescription(null);

        // Create the CharacterRace, which fails.

        restCharacterRaceMockMvc.perform(post("/api/character-races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterRace)))
            .andExpect(status().isBadRequest());

        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCharacterRaces() throws Exception {
        // Initialize the database
        characterRaceRepository.saveAndFlush(characterRace);

        // Get all the characterRaceList
        restCharacterRaceMockMvc.perform(get("/api/character-races?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characterRace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    public void getAllCharacterRacesWithEagerRelationshipsIsEnabled() throws Exception {
        CharacterRaceResource characterRaceResource = new CharacterRaceResource(characterRaceServiceMock);
        when(characterRaceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCharacterRaceMockMvc = MockMvcBuilders.standaloneSetup(characterRaceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCharacterRaceMockMvc.perform(get("/api/character-races?eagerload=true"))
        .andExpect(status().isOk());

        verify(characterRaceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllCharacterRacesWithEagerRelationshipsIsNotEnabled() throws Exception {
        CharacterRaceResource characterRaceResource = new CharacterRaceResource(characterRaceServiceMock);
            when(characterRaceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCharacterRaceMockMvc = MockMvcBuilders.standaloneSetup(characterRaceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCharacterRaceMockMvc.perform(get("/api/character-races?eagerload=true"))
        .andExpect(status().isOk());

            verify(characterRaceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCharacterRace() throws Exception {
        // Initialize the database
        characterRaceRepository.saveAndFlush(characterRace);

        // Get the characterRace
        restCharacterRaceMockMvc.perform(get("/api/character-races/{id}", characterRace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(characterRace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCharacterRace() throws Exception {
        // Get the characterRace
        restCharacterRaceMockMvc.perform(get("/api/character-races/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCharacterRace() throws Exception {
        // Initialize the database
        characterRaceService.save(characterRace);

        int databaseSizeBeforeUpdate = characterRaceRepository.findAll().size();

        // Update the characterRace
        CharacterRace updatedCharacterRace = characterRaceRepository.findById(characterRace.getId()).get();
        // Disconnect from session so that the updates on updatedCharacterRace are not directly saved in db
        em.detach(updatedCharacterRace);
        updatedCharacterRace
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restCharacterRaceMockMvc.perform(put("/api/character-races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCharacterRace)))
            .andExpect(status().isOk());

        // Validate the CharacterRace in the database
        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeUpdate);
        CharacterRace testCharacterRace = characterRaceList.get(characterRaceList.size() - 1);
        assertThat(testCharacterRace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacterRace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCharacterRace() throws Exception {
        int databaseSizeBeforeUpdate = characterRaceRepository.findAll().size();

        // Create the CharacterRace

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCharacterRaceMockMvc.perform(put("/api/character-races")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterRace)))
            .andExpect(status().isBadRequest());

        // Validate the CharacterRace in the database
        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCharacterRace() throws Exception {
        // Initialize the database
        characterRaceService.save(characterRace);

        int databaseSizeBeforeDelete = characterRaceRepository.findAll().size();

        // Get the characterRace
        restCharacterRaceMockMvc.perform(delete("/api/character-races/{id}", characterRace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CharacterRace> characterRaceList = characterRaceRepository.findAll();
        assertThat(characterRaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterRace.class);
        CharacterRace characterRace1 = new CharacterRace();
        characterRace1.setId(1L);
        CharacterRace characterRace2 = new CharacterRace();
        characterRace2.setId(characterRace1.getId());
        assertThat(characterRace1).isEqualTo(characterRace2);
        characterRace2.setId(2L);
        assertThat(characterRace1).isNotEqualTo(characterRace2);
        characterRace1.setId(null);
        assertThat(characterRace1).isNotEqualTo(characterRace2);
    }
}
