package com.outscape.hmprojetos.web.rest;

import com.outscape.hmprojetos.HmProjetosApp;

import com.outscape.hmprojetos.domain.Situacao;
import com.outscape.hmprojetos.repository.SituacaoRepository;
import com.outscape.hmprojetos.web.rest.errors.ExceptionTranslator;

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

import static com.outscape.hmprojetos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.outscape.hmprojetos.domain.enumeration.TipoSituacao;
/**
 * Test class for the SituacaoResource REST controller.
 *
 * @see SituacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class SituacaoResourceIntTest {

    private static final TipoSituacao DEFAULT_TIPO = TipoSituacao.FM;
    private static final TipoSituacao UPDATED_TIPO = TipoSituacao.FD_LIVRE;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

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
        final SituacaoResource situacaoResource = new SituacaoResource(situacaoRepository);
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
            .tipo(DEFAULT_TIPO)
            .descricao(DEFAULT_DESCRICAO)
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
        assertThat(testSituacao.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testSituacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
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
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = situacaoRepository.findAll().size();
        // set the field null
        situacao.setTipo(null);

        // Create the Situacao, which fails.

        restSituacaoMockMvc.perform(post("/api/situacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(situacao)))
            .andExpect(status().isBadRequest());

        List<Situacao> situacaoList = situacaoRepository.findAll();
        assertThat(situacaoList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
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
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.terceiro").value(DEFAULT_TERCEIRO.toString()))
            .andExpect(jsonPath("$.dtinicio").value(DEFAULT_DTINICIO.toString()))
            .andExpect(jsonPath("$.dtfim").value(DEFAULT_DTFIM.toString()))
            .andExpect(jsonPath("$.dtestipulada").value(DEFAULT_DTESTIPULADA.toString()));
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
        situacaoRepository.saveAndFlush(situacao);
        int databaseSizeBeforeUpdate = situacaoRepository.findAll().size();

        // Update the situacao
        Situacao updatedSituacao = situacaoRepository.findOne(situacao.getId());
        // Disconnect from session so that the updates on updatedSituacao are not directly saved in db
        em.detach(updatedSituacao);
        updatedSituacao
            .tipo(UPDATED_TIPO)
            .descricao(UPDATED_DESCRICAO)
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
        assertThat(testSituacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testSituacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
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
        situacaoRepository.saveAndFlush(situacao);
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
