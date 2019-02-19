package com.web.rest;

import com.HmProjetosApp;

import com.domain.TipoSituacao;
import com.repository.TipoSituacaoRepository;
import com.service.TipoSituacaoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.TipoSituacaoCriteria;
import com.service.TipoSituacaoQueryService;

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
 * Test class for the TipoSituacaoResource REST controller.
 *
 * @see TipoSituacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class TipoSituacaoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    @Autowired
    private TipoSituacaoRepository tipoSituacaoRepository;

    @Autowired
    private TipoSituacaoService tipoSituacaoService;

    @Autowired
    private TipoSituacaoQueryService tipoSituacaoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipoSituacaoMockMvc;

    private TipoSituacao tipoSituacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoSituacaoResource tipoSituacaoResource = new TipoSituacaoResource(tipoSituacaoService, tipoSituacaoQueryService);
        this.restTipoSituacaoMockMvc = MockMvcBuilders.standaloneSetup(tipoSituacaoResource)
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
    public static TipoSituacao createEntity(EntityManager em) {
        TipoSituacao tipoSituacao = new TipoSituacao()
            .nome(DEFAULT_NOME)
            .sigla(DEFAULT_SIGLA);
        return tipoSituacao;
    }

    @Before
    public void initTest() {
        tipoSituacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoSituacao() throws Exception {
        int databaseSizeBeforeCreate = tipoSituacaoRepository.findAll().size();

        // Create the TipoSituacao
        restTipoSituacaoMockMvc.perform(post("/api/tipo-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoSituacao)))
            .andExpect(status().isCreated());

        // Validate the TipoSituacao in the database
        List<TipoSituacao> tipoSituacaoList = tipoSituacaoRepository.findAll();
        assertThat(tipoSituacaoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoSituacao testTipoSituacao = tipoSituacaoList.get(tipoSituacaoList.size() - 1);
        assertThat(testTipoSituacao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoSituacao.getSigla()).isEqualTo(DEFAULT_SIGLA);
    }

    @Test
    @Transactional
    public void createTipoSituacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoSituacaoRepository.findAll().size();

        // Create the TipoSituacao with an existing ID
        tipoSituacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoSituacaoMockMvc.perform(post("/api/tipo-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoSituacao)))
            .andExpect(status().isBadRequest());

        // Validate the TipoSituacao in the database
        List<TipoSituacao> tipoSituacaoList = tipoSituacaoRepository.findAll();
        assertThat(tipoSituacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTipoSituacaos() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList
        restTipoSituacaoMockMvc.perform(get("/api/tipo-situacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoSituacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA.toString())));
    }

    @Test
    @Transactional
    public void getTipoSituacao() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get the tipoSituacao
        restTipoSituacaoMockMvc.perform(get("/api/tipo-situacaos/{id}", tipoSituacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoSituacao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA.toString()));
    }

    @Test
    @Transactional
    public void getAllTipoSituacaosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList where nome equals to DEFAULT_NOME
        defaultTipoSituacaoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the tipoSituacaoList where nome equals to UPDATED_NOME
        defaultTipoSituacaoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllTipoSituacaosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultTipoSituacaoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the tipoSituacaoList where nome equals to UPDATED_NOME
        defaultTipoSituacaoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllTipoSituacaosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList where nome is not null
        defaultTipoSituacaoShouldBeFound("nome.specified=true");

        // Get all the tipoSituacaoList where nome is null
        defaultTipoSituacaoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllTipoSituacaosBySiglaIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList where sigla equals to DEFAULT_SIGLA
        defaultTipoSituacaoShouldBeFound("sigla.equals=" + DEFAULT_SIGLA);

        // Get all the tipoSituacaoList where sigla equals to UPDATED_SIGLA
        defaultTipoSituacaoShouldNotBeFound("sigla.equals=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllTipoSituacaosBySiglaIsInShouldWork() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList where sigla in DEFAULT_SIGLA or UPDATED_SIGLA
        defaultTipoSituacaoShouldBeFound("sigla.in=" + DEFAULT_SIGLA + "," + UPDATED_SIGLA);

        // Get all the tipoSituacaoList where sigla equals to UPDATED_SIGLA
        defaultTipoSituacaoShouldNotBeFound("sigla.in=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllTipoSituacaosBySiglaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoSituacaoRepository.saveAndFlush(tipoSituacao);

        // Get all the tipoSituacaoList where sigla is not null
        defaultTipoSituacaoShouldBeFound("sigla.specified=true");

        // Get all the tipoSituacaoList where sigla is null
        defaultTipoSituacaoShouldNotBeFound("sigla.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTipoSituacaoShouldBeFound(String filter) throws Exception {
        restTipoSituacaoMockMvc.perform(get("/api/tipo-situacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoSituacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTipoSituacaoShouldNotBeFound(String filter) throws Exception {
        restTipoSituacaoMockMvc.perform(get("/api/tipo-situacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTipoSituacao() throws Exception {
        // Get the tipoSituacao
        restTipoSituacaoMockMvc.perform(get("/api/tipo-situacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoSituacao() throws Exception {
        // Initialize the database
        tipoSituacaoService.save(tipoSituacao);

        int databaseSizeBeforeUpdate = tipoSituacaoRepository.findAll().size();

        // Update the tipoSituacao
        TipoSituacao updatedTipoSituacao = tipoSituacaoRepository.findOne(tipoSituacao.getId());
        // Disconnect from session so that the updates on updatedTipoSituacao are not directly saved in db
        em.detach(updatedTipoSituacao);
        updatedTipoSituacao
            .nome(UPDATED_NOME)
            .sigla(UPDATED_SIGLA);

        restTipoSituacaoMockMvc.perform(put("/api/tipo-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoSituacao)))
            .andExpect(status().isOk());

        // Validate the TipoSituacao in the database
        List<TipoSituacao> tipoSituacaoList = tipoSituacaoRepository.findAll();
        assertThat(tipoSituacaoList).hasSize(databaseSizeBeforeUpdate);
        TipoSituacao testTipoSituacao = tipoSituacaoList.get(tipoSituacaoList.size() - 1);
        assertThat(testTipoSituacao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoSituacao.getSigla()).isEqualTo(UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoSituacao() throws Exception {
        int databaseSizeBeforeUpdate = tipoSituacaoRepository.findAll().size();

        // Create the TipoSituacao

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipoSituacaoMockMvc.perform(put("/api/tipo-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoSituacao)))
            .andExpect(status().isCreated());

        // Validate the TipoSituacao in the database
        List<TipoSituacao> tipoSituacaoList = tipoSituacaoRepository.findAll();
        assertThat(tipoSituacaoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipoSituacao() throws Exception {
        // Initialize the database
        tipoSituacaoService.save(tipoSituacao);

        int databaseSizeBeforeDelete = tipoSituacaoRepository.findAll().size();

        // Get the tipoSituacao
        restTipoSituacaoMockMvc.perform(delete("/api/tipo-situacaos/{id}", tipoSituacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TipoSituacao> tipoSituacaoList = tipoSituacaoRepository.findAll();
        assertThat(tipoSituacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoSituacao.class);
        TipoSituacao tipoSituacao1 = new TipoSituacao();
        tipoSituacao1.setId(1L);
        TipoSituacao tipoSituacao2 = new TipoSituacao();
        tipoSituacao2.setId(tipoSituacao1.getId());
        assertThat(tipoSituacao1).isEqualTo(tipoSituacao2);
        tipoSituacao2.setId(2L);
        assertThat(tipoSituacao1).isNotEqualTo(tipoSituacao2);
        tipoSituacao1.setId(null);
        assertThat(tipoSituacao1).isNotEqualTo(tipoSituacao2);
    }
}
