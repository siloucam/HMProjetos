package com.web.rest;

import com.HmProjetosApp;

import com.domain.Situacao;
import com.domain.DescricaoSituacao;
import com.domain.TipoSituacao;
import com.domain.Servico;
import com.domain.ExtendUser;
import com.repository.SituacaoRepository;
import com.service.SituacaoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.SituacaoCriteria;
import com.service.SituacaoQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SituacaoResource REST controller.
 *
 * @see SituacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class SituacaoResourceIntTest {

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_TERCEIRO = "AAAAAAAAAA";
    private static final String UPDATED_TERCEIRO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DTINICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTINICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DTFIM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTFIM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DTESTIPULADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTESTIPULADA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SituacaoRepository situacaoRepository;

    @Autowired
    private SituacaoService situacaoService;

    @Autowired
    private SituacaoQueryService situacaoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSituacaoMockMvc;

    private Situacao situacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SituacaoResource situacaoResource = new SituacaoResource(situacaoService, situacaoQueryService);
        this.restSituacaoMockMvc = MockMvcBuilders.standaloneSetup(situacaoResource)
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
    public static Situacao createEntity(EntityManager em) {
        Situacao situacao = new Situacao()
            .observacao(DEFAULT_OBSERVACAO)
            .terceiro(DEFAULT_TERCEIRO)
            .dtinicio(DEFAULT_DTINICIO)
            .dtfim(DEFAULT_DTFIM)
            .dtestipulada(DEFAULT_DTESTIPULADA);
        return situacao;
    }

    @Before
    public void initTest() {
        situacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createSituacao() throws Exception {
        int databaseSizeBeforeCreate = situacaoRepository.findAll().size();

        // Create the Situacao
        restSituacaoMockMvc.perform(post("/api/situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacao)))
            .andExpect(status().isCreated());

        // Validate the Situacao in the database
        List<Situacao> situacaoList = situacaoRepository.findAll();
        assertThat(situacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Situacao testSituacao = situacaoList.get(situacaoList.size() - 1);
        assertThat(testSituacao.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testSituacao.getTerceiro()).isEqualTo(DEFAULT_TERCEIRO);
        assertThat(testSituacao.getDtinicio()).isEqualTo(DEFAULT_DTINICIO);
        assertThat(testSituacao.getDtfim()).isEqualTo(DEFAULT_DTFIM);
        assertThat(testSituacao.getDtestipulada()).isEqualTo(DEFAULT_DTESTIPULADA);
    }

    @Test
    @Transactional
    public void createSituacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = situacaoRepository.findAll().size();

        // Create the Situacao with an existing ID
        situacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSituacaoMockMvc.perform(post("/api/situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacao)))
            .andExpect(status().isBadRequest());

        // Validate the Situacao in the database
        List<Situacao> situacaoList = situacaoRepository.findAll();
        assertThat(situacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSituacaos() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList
        restSituacaoMockMvc.perform(get("/api/situacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].terceiro").value(hasItem(DEFAULT_TERCEIRO.toString())))
            .andExpect(jsonPath("$.[*].dtinicio").value(hasItem(DEFAULT_DTINICIO.toString())))
            .andExpect(jsonPath("$.[*].dtfim").value(hasItem(DEFAULT_DTFIM.toString())))
            .andExpect(jsonPath("$.[*].dtestipulada").value(hasItem(DEFAULT_DTESTIPULADA.toString())));
    }

    @Test
    @Transactional
    public void getSituacao() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get the situacao
        restSituacaoMockMvc.perform(get("/api/situacaos/{id}", situacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(situacao.getId().intValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.terceiro").value(DEFAULT_TERCEIRO.toString()))
            .andExpect(jsonPath("$.dtinicio").value(DEFAULT_DTINICIO.toString()))
            .andExpect(jsonPath("$.dtfim").value(DEFAULT_DTFIM.toString()))
            .andExpect(jsonPath("$.dtestipulada").value(DEFAULT_DTESTIPULADA.toString()));
    }

    @Test
    @Transactional
    public void getAllSituacaosByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where observacao equals to DEFAULT_OBSERVACAO
        defaultSituacaoShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the situacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultSituacaoShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultSituacaoShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the situacaoList where observacao equals to UPDATED_OBSERVACAO
        defaultSituacaoShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where observacao is not null
        defaultSituacaoShouldBeFound("observacao.specified=true");

        // Get all the situacaoList where observacao is null
        defaultSituacaoShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllSituacaosByTerceiroIsEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where terceiro equals to DEFAULT_TERCEIRO
        defaultSituacaoShouldBeFound("terceiro.equals=" + DEFAULT_TERCEIRO);

        // Get all the situacaoList where terceiro equals to UPDATED_TERCEIRO
        defaultSituacaoShouldNotBeFound("terceiro.equals=" + UPDATED_TERCEIRO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByTerceiroIsInShouldWork() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where terceiro in DEFAULT_TERCEIRO or UPDATED_TERCEIRO
        defaultSituacaoShouldBeFound("terceiro.in=" + DEFAULT_TERCEIRO + "," + UPDATED_TERCEIRO);

        // Get all the situacaoList where terceiro equals to UPDATED_TERCEIRO
        defaultSituacaoShouldNotBeFound("terceiro.in=" + UPDATED_TERCEIRO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByTerceiroIsNullOrNotNull() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where terceiro is not null
        defaultSituacaoShouldBeFound("terceiro.specified=true");

        // Get all the situacaoList where terceiro is null
        defaultSituacaoShouldNotBeFound("terceiro.specified=false");
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtinicioIsEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtinicio equals to DEFAULT_DTINICIO
        defaultSituacaoShouldBeFound("dtinicio.equals=" + DEFAULT_DTINICIO);

        // Get all the situacaoList where dtinicio equals to UPDATED_DTINICIO
        defaultSituacaoShouldNotBeFound("dtinicio.equals=" + UPDATED_DTINICIO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtinicioIsInShouldWork() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtinicio in DEFAULT_DTINICIO or UPDATED_DTINICIO
        defaultSituacaoShouldBeFound("dtinicio.in=" + DEFAULT_DTINICIO + "," + UPDATED_DTINICIO);

        // Get all the situacaoList where dtinicio equals to UPDATED_DTINICIO
        defaultSituacaoShouldNotBeFound("dtinicio.in=" + UPDATED_DTINICIO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtinicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtinicio is not null
        defaultSituacaoShouldBeFound("dtinicio.specified=true");

        // Get all the situacaoList where dtinicio is null
        defaultSituacaoShouldNotBeFound("dtinicio.specified=false");
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtinicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtinicio greater than or equals to DEFAULT_DTINICIO
        defaultSituacaoShouldBeFound("dtinicio.greaterOrEqualThan=" + DEFAULT_DTINICIO);

        // Get all the situacaoList where dtinicio greater than or equals to UPDATED_DTINICIO
        defaultSituacaoShouldNotBeFound("dtinicio.greaterOrEqualThan=" + UPDATED_DTINICIO);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtinicioIsLessThanSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtinicio less than or equals to DEFAULT_DTINICIO
        defaultSituacaoShouldNotBeFound("dtinicio.lessThan=" + DEFAULT_DTINICIO);

        // Get all the situacaoList where dtinicio less than or equals to UPDATED_DTINICIO
        defaultSituacaoShouldBeFound("dtinicio.lessThan=" + UPDATED_DTINICIO);
    }


    @Test
    @Transactional
    public void getAllSituacaosByDtfimIsEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtfim equals to DEFAULT_DTFIM
        defaultSituacaoShouldBeFound("dtfim.equals=" + DEFAULT_DTFIM);

        // Get all the situacaoList where dtfim equals to UPDATED_DTFIM
        defaultSituacaoShouldNotBeFound("dtfim.equals=" + UPDATED_DTFIM);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtfimIsInShouldWork() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtfim in DEFAULT_DTFIM or UPDATED_DTFIM
        defaultSituacaoShouldBeFound("dtfim.in=" + DEFAULT_DTFIM + "," + UPDATED_DTFIM);

        // Get all the situacaoList where dtfim equals to UPDATED_DTFIM
        defaultSituacaoShouldNotBeFound("dtfim.in=" + UPDATED_DTFIM);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtfimIsNullOrNotNull() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtfim is not null
        defaultSituacaoShouldBeFound("dtfim.specified=true");

        // Get all the situacaoList where dtfim is null
        defaultSituacaoShouldNotBeFound("dtfim.specified=false");
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtfimIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtfim greater than or equals to DEFAULT_DTFIM
        defaultSituacaoShouldBeFound("dtfim.greaterOrEqualThan=" + DEFAULT_DTFIM);

        // Get all the situacaoList where dtfim greater than or equals to UPDATED_DTFIM
        defaultSituacaoShouldNotBeFound("dtfim.greaterOrEqualThan=" + UPDATED_DTFIM);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtfimIsLessThanSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtfim less than or equals to DEFAULT_DTFIM
        defaultSituacaoShouldNotBeFound("dtfim.lessThan=" + DEFAULT_DTFIM);

        // Get all the situacaoList where dtfim less than or equals to UPDATED_DTFIM
        defaultSituacaoShouldBeFound("dtfim.lessThan=" + UPDATED_DTFIM);
    }


    @Test
    @Transactional
    public void getAllSituacaosByDtestipuladaIsEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtestipulada equals to DEFAULT_DTESTIPULADA
        defaultSituacaoShouldBeFound("dtestipulada.equals=" + DEFAULT_DTESTIPULADA);

        // Get all the situacaoList where dtestipulada equals to UPDATED_DTESTIPULADA
        defaultSituacaoShouldNotBeFound("dtestipulada.equals=" + UPDATED_DTESTIPULADA);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtestipuladaIsInShouldWork() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtestipulada in DEFAULT_DTESTIPULADA or UPDATED_DTESTIPULADA
        defaultSituacaoShouldBeFound("dtestipulada.in=" + DEFAULT_DTESTIPULADA + "," + UPDATED_DTESTIPULADA);

        // Get all the situacaoList where dtestipulada equals to UPDATED_DTESTIPULADA
        defaultSituacaoShouldNotBeFound("dtestipulada.in=" + UPDATED_DTESTIPULADA);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtestipuladaIsNullOrNotNull() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtestipulada is not null
        defaultSituacaoShouldBeFound("dtestipulada.specified=true");

        // Get all the situacaoList where dtestipulada is null
        defaultSituacaoShouldNotBeFound("dtestipulada.specified=false");
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtestipuladaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtestipulada greater than or equals to DEFAULT_DTESTIPULADA
        defaultSituacaoShouldBeFound("dtestipulada.greaterOrEqualThan=" + DEFAULT_DTESTIPULADA);

        // Get all the situacaoList where dtestipulada greater than or equals to UPDATED_DTESTIPULADA
        defaultSituacaoShouldNotBeFound("dtestipulada.greaterOrEqualThan=" + UPDATED_DTESTIPULADA);
    }

    @Test
    @Transactional
    public void getAllSituacaosByDtestipuladaIsLessThanSomething() throws Exception {
        // Initialize the database
        situacaoRepository.saveAndFlush(situacao);

        // Get all the situacaoList where dtestipulada less than or equals to DEFAULT_DTESTIPULADA
        defaultSituacaoShouldNotBeFound("dtestipulada.lessThan=" + DEFAULT_DTESTIPULADA);

        // Get all the situacaoList where dtestipulada less than or equals to UPDATED_DTESTIPULADA
        defaultSituacaoShouldBeFound("dtestipulada.lessThan=" + UPDATED_DTESTIPULADA);
    }


    @Test
    @Transactional
    public void getAllSituacaosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        DescricaoSituacao descricao = DescricaoSituacaoResourceIntTest.createEntity(em);
        em.persist(descricao);
        em.flush();
        situacao.setDescricao(descricao);
        situacaoRepository.saveAndFlush(situacao);
        Long descricaoId = descricao.getId();

        // Get all the situacaoList where descricao equals to descricaoId
        defaultSituacaoShouldBeFound("descricaoId.equals=" + descricaoId);

        // Get all the situacaoList where descricao equals to descricaoId + 1
        defaultSituacaoShouldNotBeFound("descricaoId.equals=" + (descricaoId + 1));
    }


    @Test
    @Transactional
    public void getAllSituacaosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        TipoSituacao tipo = TipoSituacaoResourceIntTest.createEntity(em);
        em.persist(tipo);
        em.flush();
        situacao.setTipo(tipo);
        situacaoRepository.saveAndFlush(situacao);
        Long tipoId = tipo.getId();

        // Get all the situacaoList where tipo equals to tipoId
        defaultSituacaoShouldBeFound("tipoId.equals=" + tipoId);

        // Get all the situacaoList where tipo equals to tipoId + 1
        defaultSituacaoShouldNotBeFound("tipoId.equals=" + (tipoId + 1));
    }


    @Test
    @Transactional
    public void getAllSituacaosByServicoIsEqualToSomething() throws Exception {
        // Initialize the database
        Servico servico = ServicoResourceIntTest.createEntity(em);
        em.persist(servico);
        em.flush();
        situacao.setServico(servico);
        situacaoRepository.saveAndFlush(situacao);
        Long servicoId = servico.getId();

        // Get all the situacaoList where servico equals to servicoId
        defaultSituacaoShouldBeFound("servicoId.equals=" + servicoId);

        // Get all the situacaoList where servico equals to servicoId + 1
        defaultSituacaoShouldNotBeFound("servicoId.equals=" + (servicoId + 1));
    }


    @Test
    @Transactional
    public void getAllSituacaosByResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        ExtendUser responsavel = ExtendUserResourceIntTest.createEntity(em);
        em.persist(responsavel);
        em.flush();
        situacao.setResponsavel(responsavel);
        situacaoRepository.saveAndFlush(situacao);
        Long responsavelId = responsavel.getId();

        // Get all the situacaoList where responsavel equals to responsavelId
        defaultSituacaoShouldBeFound("responsavelId.equals=" + responsavelId);

        // Get all the situacaoList where responsavel equals to responsavelId + 1
        defaultSituacaoShouldNotBeFound("responsavelId.equals=" + (responsavelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSituacaoShouldBeFound(String filter) throws Exception {
        restSituacaoMockMvc.perform(get("/api/situacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].terceiro").value(hasItem(DEFAULT_TERCEIRO.toString())))
            .andExpect(jsonPath("$.[*].dtinicio").value(hasItem(DEFAULT_DTINICIO.toString())))
            .andExpect(jsonPath("$.[*].dtfim").value(hasItem(DEFAULT_DTFIM.toString())))
            .andExpect(jsonPath("$.[*].dtestipulada").value(hasItem(DEFAULT_DTESTIPULADA.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSituacaoShouldNotBeFound(String filter) throws Exception {
        restSituacaoMockMvc.perform(get("/api/situacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSituacao() throws Exception {
        // Get the situacao
        restSituacaoMockMvc.perform(get("/api/situacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSituacao() throws Exception {
        // Initialize the database
        situacaoService.save(situacao);

        int databaseSizeBeforeUpdate = situacaoRepository.findAll().size();

        // Update the situacao
        Situacao updatedSituacao = situacaoRepository.findOne(situacao.getId());
        // Disconnect from session so that the updates on updatedSituacao are not directly saved in db
        em.detach(updatedSituacao);
        updatedSituacao
            .observacao(UPDATED_OBSERVACAO)
            .terceiro(UPDATED_TERCEIRO)
            .dtinicio(UPDATED_DTINICIO)
            .dtfim(UPDATED_DTFIM)
            .dtestipulada(UPDATED_DTESTIPULADA);

        restSituacaoMockMvc.perform(put("/api/situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSituacao)))
            .andExpect(status().isOk());

        // Validate the Situacao in the database
        List<Situacao> situacaoList = situacaoRepository.findAll();
        assertThat(situacaoList).hasSize(databaseSizeBeforeUpdate);
        Situacao testSituacao = situacaoList.get(situacaoList.size() - 1);
        assertThat(testSituacao.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testSituacao.getTerceiro()).isEqualTo(UPDATED_TERCEIRO);
        assertThat(testSituacao.getDtinicio()).isEqualTo(UPDATED_DTINICIO);
        assertThat(testSituacao.getDtfim()).isEqualTo(UPDATED_DTFIM);
        assertThat(testSituacao.getDtestipulada()).isEqualTo(UPDATED_DTESTIPULADA);
    }

    @Test
    @Transactional
    public void updateNonExistingSituacao() throws Exception {
        int databaseSizeBeforeUpdate = situacaoRepository.findAll().size();

        // Create the Situacao

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSituacaoMockMvc.perform(put("/api/situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacao)))
            .andExpect(status().isCreated());

        // Validate the Situacao in the database
        List<Situacao> situacaoList = situacaoRepository.findAll();
        assertThat(situacaoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSituacao() throws Exception {
        // Initialize the database
        situacaoService.save(situacao);

        int databaseSizeBeforeDelete = situacaoRepository.findAll().size();

        // Get the situacao
        restSituacaoMockMvc.perform(delete("/api/situacaos/{id}", situacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Situacao> situacaoList = situacaoRepository.findAll();
        assertThat(situacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Situacao.class);
        Situacao situacao1 = new Situacao();
        situacao1.setId(1L);
        Situacao situacao2 = new Situacao();
        situacao2.setId(situacao1.getId());
        assertThat(situacao1).isEqualTo(situacao2);
        situacao2.setId(2L);
        assertThat(situacao1).isNotEqualTo(situacao2);
        situacao1.setId(null);
        assertThat(situacao1).isNotEqualTo(situacao2);
    }
}
