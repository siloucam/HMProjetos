package com.web.rest;

import com.HmProjetosApp;

import com.domain.DescricaoSituacao;
import com.domain.TipoSituacao;
import com.repository.DescricaoSituacaoRepository;
import com.service.DescricaoSituacaoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.DescricaoSituacaoCriteria;
import com.service.DescricaoSituacaoQueryService;

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
 * Test class for the DescricaoSituacaoResource REST controller.
 *
 * @see DescricaoSituacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class DescricaoSituacaoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private DescricaoSituacaoRepository descricaoSituacaoRepository;

    @Autowired
    private DescricaoSituacaoService descricaoSituacaoService;

    @Autowired
    private DescricaoSituacaoQueryService descricaoSituacaoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDescricaoSituacaoMockMvc;

    private DescricaoSituacao descricaoSituacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescricaoSituacaoResource descricaoSituacaoResource = new DescricaoSituacaoResource(descricaoSituacaoService, descricaoSituacaoQueryService);
        this.restDescricaoSituacaoMockMvc = MockMvcBuilders.standaloneSetup(descricaoSituacaoResource)
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
    public static DescricaoSituacao createEntity(EntityManager em) {
        DescricaoSituacao descricaoSituacao = new DescricaoSituacao()
            .nome(DEFAULT_NOME);
        return descricaoSituacao;
    }

    @Before
    public void initTest() {
        descricaoSituacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createDescricaoSituacao() throws Exception {
        int databaseSizeBeforeCreate = descricaoSituacaoRepository.findAll().size();

        // Create the DescricaoSituacao
        restDescricaoSituacaoMockMvc.perform(post("/api/descricao-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoSituacao)))
            .andExpect(status().isCreated());

        // Validate the DescricaoSituacao in the database
        List<DescricaoSituacao> descricaoSituacaoList = descricaoSituacaoRepository.findAll();
        assertThat(descricaoSituacaoList).hasSize(databaseSizeBeforeCreate + 1);
        DescricaoSituacao testDescricaoSituacao = descricaoSituacaoList.get(descricaoSituacaoList.size() - 1);
        assertThat(testDescricaoSituacao.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createDescricaoSituacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descricaoSituacaoRepository.findAll().size();

        // Create the DescricaoSituacao with an existing ID
        descricaoSituacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescricaoSituacaoMockMvc.perform(post("/api/descricao-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoSituacao)))
            .andExpect(status().isBadRequest());

        // Validate the DescricaoSituacao in the database
        List<DescricaoSituacao> descricaoSituacaoList = descricaoSituacaoRepository.findAll();
        assertThat(descricaoSituacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDescricaoSituacaos() throws Exception {
        // Initialize the database
        descricaoSituacaoRepository.saveAndFlush(descricaoSituacao);

        // Get all the descricaoSituacaoList
        restDescricaoSituacaoMockMvc.perform(get("/api/descricao-situacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descricaoSituacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getDescricaoSituacao() throws Exception {
        // Initialize the database
        descricaoSituacaoRepository.saveAndFlush(descricaoSituacao);

        // Get the descricaoSituacao
        restDescricaoSituacaoMockMvc.perform(get("/api/descricao-situacaos/{id}", descricaoSituacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(descricaoSituacao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getAllDescricaoSituacaosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        descricaoSituacaoRepository.saveAndFlush(descricaoSituacao);

        // Get all the descricaoSituacaoList where nome equals to DEFAULT_NOME
        defaultDescricaoSituacaoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the descricaoSituacaoList where nome equals to UPDATED_NOME
        defaultDescricaoSituacaoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDescricaoSituacaosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        descricaoSituacaoRepository.saveAndFlush(descricaoSituacao);

        // Get all the descricaoSituacaoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDescricaoSituacaoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the descricaoSituacaoList where nome equals to UPDATED_NOME
        defaultDescricaoSituacaoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDescricaoSituacaosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        descricaoSituacaoRepository.saveAndFlush(descricaoSituacao);

        // Get all the descricaoSituacaoList where nome is not null
        defaultDescricaoSituacaoShouldBeFound("nome.specified=true");

        // Get all the descricaoSituacaoList where nome is null
        defaultDescricaoSituacaoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllDescricaoSituacaosBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        TipoSituacao situacao = TipoSituacaoResourceIntTest.createEntity(em);
        em.persist(situacao);
        em.flush();
        descricaoSituacao.setSituacao(situacao);
        descricaoSituacaoRepository.saveAndFlush(descricaoSituacao);
        Long situacaoId = situacao.getId();

        // Get all the descricaoSituacaoList where situacao equals to situacaoId
        defaultDescricaoSituacaoShouldBeFound("situacaoId.equals=" + situacaoId);

        // Get all the descricaoSituacaoList where situacao equals to situacaoId + 1
        defaultDescricaoSituacaoShouldNotBeFound("situacaoId.equals=" + (situacaoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDescricaoSituacaoShouldBeFound(String filter) throws Exception {
        restDescricaoSituacaoMockMvc.perform(get("/api/descricao-situacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descricaoSituacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDescricaoSituacaoShouldNotBeFound(String filter) throws Exception {
        restDescricaoSituacaoMockMvc.perform(get("/api/descricao-situacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDescricaoSituacao() throws Exception {
        // Get the descricaoSituacao
        restDescricaoSituacaoMockMvc.perform(get("/api/descricao-situacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDescricaoSituacao() throws Exception {
        // Initialize the database
        descricaoSituacaoService.save(descricaoSituacao);

        int databaseSizeBeforeUpdate = descricaoSituacaoRepository.findAll().size();

        // Update the descricaoSituacao
        DescricaoSituacao updatedDescricaoSituacao = descricaoSituacaoRepository.findOne(descricaoSituacao.getId());
        // Disconnect from session so that the updates on updatedDescricaoSituacao are not directly saved in db
        em.detach(updatedDescricaoSituacao);
        updatedDescricaoSituacao
            .nome(UPDATED_NOME);

        restDescricaoSituacaoMockMvc.perform(put("/api/descricao-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDescricaoSituacao)))
            .andExpect(status().isOk());

        // Validate the DescricaoSituacao in the database
        List<DescricaoSituacao> descricaoSituacaoList = descricaoSituacaoRepository.findAll();
        assertThat(descricaoSituacaoList).hasSize(databaseSizeBeforeUpdate);
        DescricaoSituacao testDescricaoSituacao = descricaoSituacaoList.get(descricaoSituacaoList.size() - 1);
        assertThat(testDescricaoSituacao.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingDescricaoSituacao() throws Exception {
        int databaseSizeBeforeUpdate = descricaoSituacaoRepository.findAll().size();

        // Create the DescricaoSituacao

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDescricaoSituacaoMockMvc.perform(put("/api/descricao-situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoSituacao)))
            .andExpect(status().isCreated());

        // Validate the DescricaoSituacao in the database
        List<DescricaoSituacao> descricaoSituacaoList = descricaoSituacaoRepository.findAll();
        assertThat(descricaoSituacaoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDescricaoSituacao() throws Exception {
        // Initialize the database
        descricaoSituacaoService.save(descricaoSituacao);

        int databaseSizeBeforeDelete = descricaoSituacaoRepository.findAll().size();

        // Get the descricaoSituacao
        restDescricaoSituacaoMockMvc.perform(delete("/api/descricao-situacaos/{id}", descricaoSituacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DescricaoSituacao> descricaoSituacaoList = descricaoSituacaoRepository.findAll();
        assertThat(descricaoSituacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescricaoSituacao.class);
        DescricaoSituacao descricaoSituacao1 = new DescricaoSituacao();
        descricaoSituacao1.setId(1L);
        DescricaoSituacao descricaoSituacao2 = new DescricaoSituacao();
        descricaoSituacao2.setId(descricaoSituacao1.getId());
        assertThat(descricaoSituacao1).isEqualTo(descricaoSituacao2);
        descricaoSituacao2.setId(2L);
        assertThat(descricaoSituacao1).isNotEqualTo(descricaoSituacao2);
        descricaoSituacao1.setId(null);
        assertThat(descricaoSituacao1).isNotEqualTo(descricaoSituacao2);
    }
}
