package charactercreator.web.rest;

import charactercreator.CharacterCreatorApp;

import charactercreator.domain.CharacterClass;
import charactercreator.repository.CharacterClassRepository;
import charactercreator.service.CharacterClassService;
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
 * Test class for the CharacterClassResource REST controller.
 *
 * @see CharacterClassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CharacterCreatorApp.class)
public class CharacterClassResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CharacterClassRepository characterClassRepository;
    @Mock
    private CharacterClassRepository characterClassRepositoryMock;
    
    @Mock
    private CharacterClassService characterClassServiceMock;

    @Autowired
    private CharacterClassService characterClassService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCharacterClassMockMvc;

    private CharacterClass characterClass;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CharacterClassResource characterClassResource = new CharacterClassResource(characterClassService);
        this.restCharacterClassMockMvc = MockMvcBuilders.standaloneSetup(characterClassResource)
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
    public static CharacterClass createEntity(EntityManager em) {
        CharacterClass characterClass = new CharacterClass()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return characterClass;
    }

    @Before
    public void initTest() {
        characterClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createCharacterClass() throws Exception {
        int databaseSizeBeforeCreate = characterClassRepository.findAll().size();

        // Create the CharacterClass
        restCharacterClassMockMvc.perform(post("/api/character-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterClass)))
            .andExpect(status().isCreated());

        // Validate the CharacterClass in the database
        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeCreate + 1);
        CharacterClass testCharacterClass = characterClassList.get(characterClassList.size() - 1);
        assertThat(testCharacterClass.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacterClass.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCharacterClassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = characterClassRepository.findAll().size();

        // Create the CharacterClass with an existing ID
        characterClass.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterClassMockMvc.perform(post("/api/character-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterClass)))
            .andExpect(status().isBadRequest());

        // Validate the CharacterClass in the database
        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterClassRepository.findAll().size();
        // set the field null
        characterClass.setName(null);

        // Create the CharacterClass, which fails.

        restCharacterClassMockMvc.perform(post("/api/character-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterClass)))
            .andExpect(status().isBadRequest());

        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterClassRepository.findAll().size();
        // set the field null
        characterClass.setDescription(null);

        // Create the CharacterClass, which fails.

        restCharacterClassMockMvc.perform(post("/api/character-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterClass)))
            .andExpect(status().isBadRequest());

        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCharacterClasses() throws Exception {
        // Initialize the database
        characterClassRepository.saveAndFlush(characterClass);

        // Get all the characterClassList
        restCharacterClassMockMvc.perform(get("/api/character-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characterClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    public void getAllCharacterClassesWithEagerRelationshipsIsEnabled() throws Exception {
        CharacterClassResource characterClassResource = new CharacterClassResource(characterClassServiceMock);
        when(characterClassServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCharacterClassMockMvc = MockMvcBuilders.standaloneSetup(characterClassResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCharacterClassMockMvc.perform(get("/api/character-classes?eagerload=true"))
        .andExpect(status().isOk());

        verify(characterClassServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllCharacterClassesWithEagerRelationshipsIsNotEnabled() throws Exception {
        CharacterClassResource characterClassResource = new CharacterClassResource(characterClassServiceMock);
            when(characterClassServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCharacterClassMockMvc = MockMvcBuilders.standaloneSetup(characterClassResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCharacterClassMockMvc.perform(get("/api/character-classes?eagerload=true"))
        .andExpect(status().isOk());

            verify(characterClassServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCharacterClass() throws Exception {
        // Initialize the database
        characterClassRepository.saveAndFlush(characterClass);

        // Get the characterClass
        restCharacterClassMockMvc.perform(get("/api/character-classes/{id}", characterClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(characterClass.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCharacterClass() throws Exception {
        // Get the characterClass
        restCharacterClassMockMvc.perform(get("/api/character-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCharacterClass() throws Exception {
        // Initialize the database
        characterClassService.save(characterClass);

        int databaseSizeBeforeUpdate = characterClassRepository.findAll().size();

        // Update the characterClass
        CharacterClass updatedCharacterClass = characterClassRepository.findById(characterClass.getId()).get();
        // Disconnect from session so that the updates on updatedCharacterClass are not directly saved in db
        em.detach(updatedCharacterClass);
        updatedCharacterClass
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restCharacterClassMockMvc.perform(put("/api/character-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCharacterClass)))
            .andExpect(status().isOk());

        // Validate the CharacterClass in the database
        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeUpdate);
        CharacterClass testCharacterClass = characterClassList.get(characterClassList.size() - 1);
        assertThat(testCharacterClass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacterClass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCharacterClass() throws Exception {
        int databaseSizeBeforeUpdate = characterClassRepository.findAll().size();

        // Create the CharacterClass

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCharacterClassMockMvc.perform(put("/api/character-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(characterClass)))
            .andExpect(status().isBadRequest());

        // Validate the CharacterClass in the database
        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCharacterClass() throws Exception {
        // Initialize the database
        characterClassService.save(characterClass);

        int databaseSizeBeforeDelete = characterClassRepository.findAll().size();

        // Get the characterClass
        restCharacterClassMockMvc.perform(delete("/api/character-classes/{id}", characterClass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CharacterClass> characterClassList = characterClassRepository.findAll();
        assertThat(characterClassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterClass.class);
        CharacterClass characterClass1 = new CharacterClass();
        characterClass1.setId(1L);
        CharacterClass characterClass2 = new CharacterClass();
        characterClass2.setId(characterClass1.getId());
        assertThat(characterClass1).isEqualTo(characterClass2);
        characterClass2.setId(2L);
        assertThat(characterClass1).isNotEqualTo(characterClass2);
        characterClass1.setId(null);
        assertThat(characterClass1).isNotEqualTo(characterClass2);
    }
}
