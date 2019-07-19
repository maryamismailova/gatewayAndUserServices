package com.filefirmware.project.web.rest;

import com.filefirmware.project.UserDetailsApp;
import com.filefirmware.project.domain.UserDetailsDTO;
import com.filefirmware.project.repository.UserDetailsDTORepository;
import com.filefirmware.project.service.UserDetailsDTOService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.filefirmware.project.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserDetailsDTOResource} REST controller.
 */
@SpringBootTest(classes = UserDetailsApp.class)
public class UserDetailsDTOResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private UserDetailsDTORepository userDetailsDTORepository;

    @Autowired
    private UserDetailsDTOService userDetailsDTOService;

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

    private MockMvc restUserDetailsDTOMockMvc;

    private UserDetailsDTO userDetailsDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserDetailsDTOResource userDetailsDTOResource = new UserDetailsDTOResource(userDetailsDTOService);
        this.restUserDetailsDTOMockMvc = MockMvcBuilders.standaloneSetup(userDetailsDTOResource)
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
    public static UserDetailsDTO createEntity(EntityManager em) {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO()
            .login(DEFAULT_LOGIN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .activated(DEFAULT_ACTIVATED)
            .langKey(DEFAULT_LANG_KEY)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .birthdate(DEFAULT_BIRTHDATE);
        return userDetailsDTO;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetailsDTO createUpdatedEntity(EntityManager em) {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO()
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .birthdate(UPDATED_BIRTHDATE);
        return userDetailsDTO;
    }

    @BeforeEach
    public void initTest() {
        userDetailsDTO = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserDetailsDTO() throws Exception {
        int databaseSizeBeforeCreate = userDetailsDTORepository.findAll().size();

        // Create the UserDetailsDTO
        restUserDetailsDTOMockMvc.perform(post("/api/user-details-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the UserDetailsDTO in the database
        List<UserDetailsDTO> userDetailsDTOList = userDetailsDTORepository.findAll();
        assertThat(userDetailsDTOList).hasSize(databaseSizeBeforeCreate + 1);
        UserDetailsDTO testUserDetailsDTO = userDetailsDTOList.get(userDetailsDTOList.size() - 1);
        assertThat(testUserDetailsDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUserDetailsDTO.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserDetailsDTO.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserDetailsDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserDetailsDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUserDetailsDTO.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testUserDetailsDTO.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testUserDetailsDTO.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserDetailsDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserDetailsDTO.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserDetailsDTO.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    public void createUserDetailsDTOWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDetailsDTORepository.findAll().size();

        // Create the UserDetailsDTO with an existing ID
        userDetailsDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsDTOMockMvc.perform(post("/api/user-details-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetailsDTO in the database
        List<UserDetailsDTO> userDetailsDTOList = userDetailsDTORepository.findAll();
        assertThat(userDetailsDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserDetailsDTOS() throws Exception {
        // Initialize the database
        userDetailsDTORepository.saveAndFlush(userDetailsDTO);

        // Get all the userDetailsDTOList
        restUserDetailsDTOMockMvc.perform(get("/api/user-details-dtos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetailsDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));
    }
    
    @Test
    @Transactional
    public void getUserDetailsDTO() throws Exception {
        // Initialize the database
        userDetailsDTORepository.saveAndFlush(userDetailsDTO);

        // Get the userDetailsDTO
        restUserDetailsDTOMockMvc.perform(get("/api/user-details-dtos/{id}", userDetailsDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userDetailsDTO.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserDetailsDTO() throws Exception {
        // Get the userDetailsDTO
        restUserDetailsDTOMockMvc.perform(get("/api/user-details-dtos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserDetailsDTO() throws Exception {
        // Initialize the database
        userDetailsDTOService.save(userDetailsDTO);

        int databaseSizeBeforeUpdate = userDetailsDTORepository.findAll().size();

        // Update the userDetailsDTO
        UserDetailsDTO updatedUserDetailsDTO = userDetailsDTORepository.findById(userDetailsDTO.getId()).get();
        // Disconnect from session so that the updates on updatedUserDetailsDTO are not directly saved in db
        em.detach(updatedUserDetailsDTO);
        updatedUserDetailsDTO
            .login(UPDATED_LOGIN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .birthdate(UPDATED_BIRTHDATE);

        restUserDetailsDTOMockMvc.perform(put("/api/user-details-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the UserDetailsDTO in the database
        List<UserDetailsDTO> userDetailsDTOList = userDetailsDTORepository.findAll();
        assertThat(userDetailsDTOList).hasSize(databaseSizeBeforeUpdate);
        UserDetailsDTO testUserDetailsDTO = userDetailsDTOList.get(userDetailsDTOList.size() - 1);
        assertThat(testUserDetailsDTO.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUserDetailsDTO.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserDetailsDTO.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserDetailsDTO.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserDetailsDTO.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUserDetailsDTO.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUserDetailsDTO.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testUserDetailsDTO.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserDetailsDTO.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserDetailsDTO.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserDetailsDTO.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserDetailsDTO() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsDTORepository.findAll().size();

        // Create the UserDetailsDTO

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsDTOMockMvc.perform(put("/api/user-details-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetailsDTO in the database
        List<UserDetailsDTO> userDetailsDTOList = userDetailsDTORepository.findAll();
        assertThat(userDetailsDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserDetailsDTO() throws Exception {
        // Initialize the database
        userDetailsDTOService.save(userDetailsDTO);

        int databaseSizeBeforeDelete = userDetailsDTORepository.findAll().size();

        // Delete the userDetailsDTO
        restUserDetailsDTOMockMvc.perform(delete("/api/user-details-dtos/{id}", userDetailsDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserDetailsDTO> userDetailsDTOList = userDetailsDTORepository.findAll();
        assertThat(userDetailsDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDetailsDTO.class);
        UserDetailsDTO userDetailsDTO1 = new UserDetailsDTO();
        userDetailsDTO1.setId(1L);
        UserDetailsDTO userDetailsDTO2 = new UserDetailsDTO();
        userDetailsDTO2.setId(userDetailsDTO1.getId());
        assertThat(userDetailsDTO1).isEqualTo(userDetailsDTO2);
        userDetailsDTO2.setId(2L);
        assertThat(userDetailsDTO1).isNotEqualTo(userDetailsDTO2);
        userDetailsDTO1.setId(null);
        assertThat(userDetailsDTO1).isNotEqualTo(userDetailsDTO2);
    }
}
