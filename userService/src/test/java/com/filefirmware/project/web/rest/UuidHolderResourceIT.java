package com.filefirmware.project.web.rest;

import com.filefirmware.project.UserserviceApp;
import com.filefirmware.project.domain.UuidHolder;
import com.filefirmware.project.repository.UuidHolderRepository;
import com.filefirmware.project.service.UuidHolderService;
import com.filefirmware.project.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.filefirmware.project.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UuidHolderResource} REST controller.
 */
@SpringBootTest(classes = UserserviceApp.class)
public class UuidHolderResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    @Autowired
    private UuidHolderRepository uuidHolderRepository;

    @Autowired
    private UuidHolderService uuidHolderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUuidHolderMockMvc;

    private UuidHolder uuidHolder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UuidHolderResource uuidHolderResource = new UuidHolderResource(uuidHolderService);
        this.restUuidHolderMockMvc = MockMvcBuilders.standaloneSetup(uuidHolderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UuidHolder createEntity(EntityManager em) {
        UuidHolder uuidHolder = new UuidHolder()
            .uuid(DEFAULT_UUID);
        return uuidHolder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UuidHolder createUpdatedEntity(EntityManager em) {
        UuidHolder uuidHolder = new UuidHolder()
            .uuid(UPDATED_UUID);
        return uuidHolder;
    }

    @BeforeEach
    public void initTest() {
        uuidHolder = createEntity(em);
    }

    @Test
    @Transactional
    public void createUuidHolder() throws Exception {
        int databaseSizeBeforeCreate = uuidHolderRepository.findAll().size();

        // Create the UuidHolder
        restUuidHolderMockMvc.perform(post("/api/uuid-holders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uuidHolder)))
            .andExpect(status().isCreated());

        // Validate the UuidHolder in the database
        List<UuidHolder> uuidHolderList = uuidHolderRepository.findAll();
        assertThat(uuidHolderList).hasSize(databaseSizeBeforeCreate + 1);
        UuidHolder testUuidHolder = uuidHolderList.get(uuidHolderList.size() - 1);
        assertThat(testUuidHolder.getUuid()).isEqualTo(DEFAULT_UUID);
    }

    @Test
    @Transactional
    public void createUuidHolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uuidHolderRepository.findAll().size();

        // Create the UuidHolder with an existing ID
        uuidHolder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUuidHolderMockMvc.perform(post("/api/uuid-holders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uuidHolder)))
            .andExpect(status().isBadRequest());

        // Validate the UuidHolder in the database
        List<UuidHolder> uuidHolderList = uuidHolderRepository.findAll();
        assertThat(uuidHolderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUuidHolders() throws Exception {
        // Initialize the database
        uuidHolderRepository.saveAndFlush(uuidHolder);

        // Get all the uuidHolderList
        restUuidHolderMockMvc.perform(get("/api/uuid-holders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uuidHolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())));
    }
    
    @Test
    @Transactional
    public void getUuidHolder() throws Exception {
        // Initialize the database
        uuidHolderRepository.saveAndFlush(uuidHolder);

        // Get the uuidHolder
        restUuidHolderMockMvc.perform(get("/api/uuid-holders/{id}", uuidHolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(uuidHolder.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUuidHolder() throws Exception {
        // Get the uuidHolder
        restUuidHolderMockMvc.perform(get("/api/uuid-holders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUuidHolder() throws Exception {
        // Initialize the database
        uuidHolderService.save(uuidHolder);

        int databaseSizeBeforeUpdate = uuidHolderRepository.findAll().size();

        // Update the uuidHolder
        UuidHolder updatedUuidHolder = uuidHolderRepository.findById(uuidHolder.getId()).get();
        // Disconnect from session so that the updates on updatedUuidHolder are not directly saved in db
        em.detach(updatedUuidHolder);
        updatedUuidHolder
            .uuid(UPDATED_UUID);

        restUuidHolderMockMvc.perform(put("/api/uuid-holders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUuidHolder)))
            .andExpect(status().isOk());

        // Validate the UuidHolder in the database
        List<UuidHolder> uuidHolderList = uuidHolderRepository.findAll();
        assertThat(uuidHolderList).hasSize(databaseSizeBeforeUpdate);
        UuidHolder testUuidHolder = uuidHolderList.get(uuidHolderList.size() - 1);
        assertThat(testUuidHolder.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    public void updateNonExistingUuidHolder() throws Exception {
        int databaseSizeBeforeUpdate = uuidHolderRepository.findAll().size();

        // Create the UuidHolder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUuidHolderMockMvc.perform(put("/api/uuid-holders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uuidHolder)))
            .andExpect(status().isBadRequest());

        // Validate the UuidHolder in the database
        List<UuidHolder> uuidHolderList = uuidHolderRepository.findAll();
        assertThat(uuidHolderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUuidHolder() throws Exception {
        // Initialize the database
        uuidHolderService.save(uuidHolder);

        int databaseSizeBeforeDelete = uuidHolderRepository.findAll().size();

        // Delete the uuidHolder
        restUuidHolderMockMvc.perform(delete("/api/uuid-holders/{id}", uuidHolder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UuidHolder> uuidHolderList = uuidHolderRepository.findAll();
        assertThat(uuidHolderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UuidHolder.class);
        UuidHolder uuidHolder1 = new UuidHolder();
        uuidHolder1.setId(1L);
        UuidHolder uuidHolder2 = new UuidHolder();
        uuidHolder2.setId(uuidHolder1.getId());
        assertThat(uuidHolder1).isEqualTo(uuidHolder2);
        uuidHolder2.setId(2L);
        assertThat(uuidHolder1).isNotEqualTo(uuidHolder2);
        uuidHolder1.setId(null);
        assertThat(uuidHolder1).isNotEqualTo(uuidHolder2);
    }
}
