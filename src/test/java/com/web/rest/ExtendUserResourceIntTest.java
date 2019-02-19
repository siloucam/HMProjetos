package com.web.rest;

import com.HmProjetosApp;

import com.domain.ExtendUser;
import com.domain.User;
import com.domain.Situacao;
import com.repository.ExtendUserRepository;
import com.service.ExtendUserService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.ExtendUserCriteria;
import com.service.ExtendUserQueryService;

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
import java.util.List;

import static com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.domain.enumeration.TipoCargo;
/**
 * Test class for the ExtendUserResource REST controller.
 *
 * @see ExtendUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class ExtendUserResourceIntTest {

    private static final TipoCargo DEFAULT_TIPO = TipoCargo.SECRETARIA;
    private static final TipoCargo UPDATED_TIPO = TipoCargo.DESENHO;

    @Autowired
    private ExtendUserRepository extendUserRepository;

    @Autowired
    private ExtendUserService extendUserService;

    @Autowired
    private ExtendUserQueryService extendUserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExtendUserMockMvc;

    private ExtendUser extendUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtendUserResource extendUserResource = new ExtendUserResource(extendUserService, extendUserQueryService);
        this.restExtendUserMockMvc = MockMvcBuilders.standaloneSetup(extendUserResource)
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
    public static ExtendUser createEntity(EntityManager em) {
        ExtendUser extendUser = new ExtendUser()
            .tipo(DEFAULT_TIPO);
        return extendUser;
    }

    @Before
    public void initTest() {
        extendUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtendUser() throws Exception {
        int databaseSizeBeforeCreate = extendUserRepository.findAll().size();

        // Create the ExtendUser
        restExtendUserMockMvc.perform(post("/api/extend-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendUser)))
            .andExpect(status().isCreated());

        // Validate the ExtendUser in the database
        List<ExtendUser> extendUserList = extendUserRepository.findAll();
        assertThat(extendUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtendUser testExtendUser = extendUserList.get(extendUserList.size() - 1);
        assertThat(testExtendUser.getTipo()).isEqualTo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void createExtendUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extendUserRepository.findAll().size();

        // Create the ExtendUser with an existing ID
        extendUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtendUserMockMvc.perform(post("/api/extend-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendUser in the database
        List<ExtendUser> extendUserList = extendUserRepository.findAll();
        assertThat(extendUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExtendUsers() throws Exception {
        // Initialize the database
        extendUserRepository.saveAndFlush(extendUser);

        // Get all the extendUserList
        restExtendUserMockMvc.perform(get("/api/extend-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getExtendUser() throws Exception {
        // Initialize the database
        extendUserRepository.saveAndFlush(extendUser);

        // Get the extendUser
        restExtendUserMockMvc.perform(get("/api/extend-users/{id}", extendUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extendUser.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getAllExtendUsersByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        extendUserRepository.saveAndFlush(extendUser);

        // Get all the extendUserList where tipo equals to DEFAULT_TIPO
        defaultExtendUserShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the extendUserList where tipo equals to UPDATED_TIPO
        defaultExtendUserShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllExtendUsersByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        extendUserRepository.saveAndFlush(extendUser);

        // Get all the extendUserList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultExtendUserShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the extendUserList where tipo equals to UPDATED_TIPO
        defaultExtendUserShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllExtendUsersByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendUserRepository.saveAndFlush(extendUser);

        // Get all the extendUserList where tipo is not null
        defaultExtendUserShouldBeFound("tipo.specified=true");

        // Get all the extendUserList where tipo is null
        defaultExtendUserShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    public void getAllExtendUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        extendUser.setUser(user);
        extendUserRepository.saveAndFlush(extendUser);
        Long userId = user.getId();

        // Get all the extendUserList where user equals to userId
        defaultExtendUserShouldBeFound("userId.equals=" + userId);

        // Get all the extendUserList where user equals to userId + 1
        defaultExtendUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllExtendUsersBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        Situacao situacao = SituacaoResourceIntTest.createEntity(em);
        em.persist(situacao);
        em.flush();
        extendUser.addSituacao(situacao);
        extendUserRepository.saveAndFlush(extendUser);
        Long situacaoId = situacao.getId();

        // Get all the extendUserList where situacao equals to situacaoId
        defaultExtendUserShouldBeFound("situacaoId.equals=" + situacaoId);

        // Get all the extendUserList where situacao equals to situacaoId + 1
        defaultExtendUserShouldNotBeFound("situacaoId.equals=" + (situacaoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExtendUserShouldBeFound(String filter) throws Exception {
        restExtendUserMockMvc.perform(get("/api/extend-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExtendUserShouldNotBeFound(String filter) throws Exception {
        restExtendUserMockMvc.perform(get("/api/extend-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingExtendUser() throws Exception {
        // Get the extendUser
        restExtendUserMockMvc.perform(get("/api/extend-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtendUser() throws Exception {
        // Initialize the database
        extendUserService.save(extendUser);

        int databaseSizeBeforeUpdate = extendUserRepository.findAll().size();

        // Update the extendUser
        ExtendUser updatedExtendUser = extendUserRepository.findOne(extendUser.getId());
        // Disconnect from session so that the updates on updatedExtendUser are not directly saved in db
        em.detach(updatedExtendUser);
        updatedExtendUser
            .tipo(UPDATED_TIPO);

        restExtendUserMockMvc.perform(put("/api/extend-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtendUser)))
            .andExpect(status().isOk());

        // Validate the ExtendUser in the database
        List<ExtendUser> extendUserList = extendUserRepository.findAll();
        assertThat(extendUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendUser testExtendUser = extendUserList.get(extendUserList.size() - 1);
        assertThat(testExtendUser.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void updateNonExistingExtendUser() throws Exception {
        int databaseSizeBeforeUpdate = extendUserRepository.findAll().size();

        // Create the ExtendUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtendUserMockMvc.perform(put("/api/extend-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendUser)))
            .andExpect(status().isCreated());

        // Validate the ExtendUser in the database
        List<ExtendUser> extendUserList = extendUserRepository.findAll();
        assertThat(extendUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtendUser() throws Exception {
        // Initialize the database
        extendUserService.save(extendUser);

        int databaseSizeBeforeDelete = extendUserRepository.findAll().size();

        // Get the extendUser
        restExtendUserMockMvc.perform(delete("/api/extend-users/{id}", extendUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtendUser> extendUserList = extendUserRepository.findAll();
        assertThat(extendUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtendUser.class);
        ExtendUser extendUser1 = new ExtendUser();
        extendUser1.setId(1L);
        ExtendUser extendUser2 = new ExtendUser();
        extendUser2.setId(extendUser1.getId());
        assertThat(extendUser1).isEqualTo(extendUser2);
        extendUser2.setId(2L);
        assertThat(extendUser1).isNotEqualTo(extendUser2);
        extendUser1.setId(null);
        assertThat(extendUser1).isNotEqualTo(extendUser2);
    }
}
