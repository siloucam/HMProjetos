package com.web.rest;

import com.HmProjetosApp;

import com.domain.Terceiro;
import com.repository.TerceiroRepository;
import com.service.TerceiroService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.TerceiroCriteria;
import com.service.TerceiroQueryService;

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

/**
 * Test class for the TerceiroResource REST controller.
 *
 * @see TerceiroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class TerceiroResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private TerceiroRepository terceiroRepository;

    @Autowired
    private TerceiroService terceiroService;

    @Autowired
    private TerceiroQueryService terceiroQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTerceiroMockMvc;

    private Terceiro terceiro;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TerceiroResource terceiroResource = new TerceiroResource(terceiroService, terceiroQueryService);
        this.restTerceiroMockMvc = MockMvcBuilders.standaloneSetup(terceiroResource)
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
    public static Terceiro createEntity(EntityManager em) {
        Terceiro terceiro = new Terceiro()
            .nome(DEFAULT_NOME);
        return terceiro;
    }

    @Before
    public void initTest() {
        terceiro = createEntity(em);
    }

    @Test
    @Transactional
    public void createTerceiro() throws Exception {
        int databaseSizeBeforeCreate = terceiroRepository.findAll().size();

        // Create the Terceiro
        restTerceiroMockMvc.perform(post("/api/terceiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terceiro)))
            .andExpect(status().isCreated());

        // Validate the Terceiro in the database
        List<Terceiro> terceiroList = terceiroRepository.findAll();
        assertThat(terceiroList).hasSize(databaseSizeBeforeCreate + 1);
        Terceiro testTerceiro = terceiroList.get(terceiroList.size() - 1);
        assertThat(testTerceiro.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createTerceiroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = terceiroRepository.findAll().size();

        // Create the Terceiro with an existing ID
        terceiro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerceiroMockMvc.perform(post("/api/terceiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terceiro)))
            .andExpect(status().isBadRequest());

        // Validate the Terceiro in the database
        List<Terceiro> terceiroList = terceiroRepository.findAll();
        assertThat(terceiroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTerceiros() throws Exception {
        // Initialize the database
        terceiroRepository.saveAndFlush(terceiro);

        // Get all the terceiroList
        restTerceiroMockMvc.perform(get("/api/terceiros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terceiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getTerceiro() throws Exception {
        // Initialize the database
        terceiroRepository.saveAndFlush(terceiro);

        // Get the terceiro
        restTerceiroMockMvc.perform(get("/api/terceiros/{id}", terceiro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(terceiro.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllTerceirosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        terceiroRepository.saveAndFlush(terceiro);

        // Get all the terceiroList where nome equals to DEFAULT_NOME
        defaultTerceiroShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the terceiroList where nome equals to UPDATED_NOME
        defaultTerceiroShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllTerceirosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        terceiroRepository.saveAndFlush(terceiro);

        // Get all the terceiroList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTerceiroShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the terceiroList where nome equals to UPDATED_NOME
        defaultTerceiroShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllTerceirosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terceiroRepository.saveAndFlush(terceiro);

        // Get all the terceiroList where nome is not null
        defaultTerceiroShouldBeFound("nome.specified=true");

        // Get all the terceiroList where nome is null
        defaultTerceiroShouldNotBeFound("nome.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTerceiroShouldBeFound(String filter) throws Exception {
        restTerceiroMockMvc.perform(get("/api/terceiros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terceiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTerceiroShouldNotBeFound(String filter) throws Exception {
        restTerceiroMockMvc.perform(get("/api/terceiros?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTerceiro() throws Exception {
        // Get the terceiro
        restTerceiroMockMvc.perform(get("/api/terceiros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTerceiro() throws Exception {
        // Initialize the database
        terceiroService.save(terceiro);

        int databaseSizeBeforeUpdate = terceiroRepository.findAll().size();

        // Update the terceiro
        Terceiro updatedTerceiro = terceiroRepository.findOne(terceiro.getId());
        // Disconnect from session so that the updates on updatedTerceiro are not directly saved in db
        em.detach(updatedTerceiro);
        updatedTerceiro
            .nome(UPDATED_NOME);

        restTerceiroMockMvc.perform(put("/api/terceiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTerceiro)))
            .andExpect(status().isOk());

        // Validate the Terceiro in the database
        List<Terceiro> terceiroList = terceiroRepository.findAll();
        assertThat(terceiroList).hasSize(databaseSizeBeforeUpdate);
        Terceiro testTerceiro = terceiroList.get(terceiroList.size() - 1);
        assertThat(testTerceiro.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingTerceiro() throws Exception {
        int databaseSizeBeforeUpdate = terceiroRepository.findAll().size();

        // Create the Terceiro

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTerceiroMockMvc.perform(put("/api/terceiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terceiro)))
            .andExpect(status().isCreated());

        // Validate the Terceiro in the database
        List<Terceiro> terceiroList = terceiroRepository.findAll();
        assertThat(terceiroList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTerceiro() throws Exception {
        // Initialize the database
        terceiroService.save(terceiro);

        int databaseSizeBeforeDelete = terceiroRepository.findAll().size();

        // Get the terceiro
        restTerceiroMockMvc.perform(delete("/api/terceiros/{id}", terceiro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Terceiro> terceiroList = terceiroRepository.findAll();
        assertThat(terceiroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Terceiro.class);
        Terceiro terceiro1 = new Terceiro();
        terceiro1.setId(1L);
        Terceiro terceiro2 = new Terceiro();
        terceiro2.setId(terceiro1.getId());
        assertThat(terceiro1).isEqualTo(terceiro2);
        terceiro2.setId(2L);
        assertThat(terceiro1).isNotEqualTo(terceiro2);
        terceiro1.setId(null);
        assertThat(terceiro1).isNotEqualTo(terceiro2);
    }
}
