package com.outscape.hmprojetos.web.rest;

import com.outscape.hmprojetos.HmProjetosApp;

import com.outscape.hmprojetos.domain.Orcamento;
import com.outscape.hmprojetos.repository.OrcamentoRepository;
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
import java.util.List;

import static com.outscape.hmprojetos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrcamentoResource REST controller.
 *
 * @see OrcamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class OrcamentoResourceIntTest {

    private static final Float DEFAULT_VALOR = 1F;
    private static final Float UPDATED_VALOR = 2F;

    private static final Float DEFAULT_ENTRADA = 1F;
    private static final Float UPDATED_ENTRADA = 2F;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrcamentoMockMvc;

    private Orcamento orcamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrcamentoResource orcamentoResource = new OrcamentoResource(orcamentoRepository);
        this.restOrcamentoMockMvc = MockMvcBuilders.standaloneSetup(orcamentoResource)
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
    public static Orcamento createEntity(EntityManager em) {
        Orcamento orcamento = new Orcamento()
            .valor(DEFAULT_VALOR)
            .entrada(DEFAULT_ENTRADA);
        return orcamento;
    }

    @Before
    public void initTest() {
        orcamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrcamento() throws Exception {
        int databaseSizeBeforeCreate = orcamentoRepository.findAll().size();

        // Create the Orcamento
        restOrcamentoMockMvc.perform(post("/api/orcamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orcamento)))
            .andExpect(status().isCreated());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Orcamento testOrcamento = orcamentoList.get(orcamentoList.size() - 1);
        assertThat(testOrcamento.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testOrcamento.getEntrada()).isEqualTo(DEFAULT_ENTRADA);
    }

    @Test
    @Transactional
    public void createOrcamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orcamentoRepository.findAll().size();

        // Create the Orcamento with an existing ID
        orcamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrcamentoMockMvc.perform(post("/api/orcamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orcamento)))
            .andExpect(status().isBadRequest());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = orcamentoRepository.findAll().size();
        // set the field null
        orcamento.setValor(null);

        // Create the Orcamento, which fails.

        restOrcamentoMockMvc.perform(post("/api/orcamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orcamento)))
            .andExpect(status().isBadRequest());

        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrcamentos() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList
        restOrcamentoMockMvc.perform(get("/api/orcamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orcamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].entrada").value(hasItem(DEFAULT_ENTRADA.doubleValue())));
    }

    @Test
    @Transactional
    public void getOrcamento() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get the orcamento
        restOrcamentoMockMvc.perform(get("/api/orcamentos/{id}", orcamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orcamento.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.entrada").value(DEFAULT_ENTRADA.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrcamento() throws Exception {
        // Get the orcamento
        restOrcamentoMockMvc.perform(get("/api/orcamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrcamento() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();

        // Update the orcamento
        Orcamento updatedOrcamento = orcamentoRepository.findOne(orcamento.getId());
        // Disconnect from session so that the updates on updatedOrcamento are not directly saved in db
        em.detach(updatedOrcamento);
        updatedOrcamento
            .valor(UPDATED_VALOR)
            .entrada(UPDATED_ENTRADA);

        restOrcamentoMockMvc.perform(put("/api/orcamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrcamento)))
            .andExpect(status().isOk());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
        Orcamento testOrcamento = orcamentoList.get(orcamentoList.size() - 1);
        assertThat(testOrcamento.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testOrcamento.getEntrada()).isEqualTo(UPDATED_ENTRADA);
    }

    @Test
    @Transactional
    public void updateNonExistingOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();

        // Create the Orcamento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrcamentoMockMvc.perform(put("/api/orcamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orcamento)))
            .andExpect(status().isCreated());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrcamento() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);
        int databaseSizeBeforeDelete = orcamentoRepository.findAll().size();

        // Get the orcamento
        restOrcamentoMockMvc.perform(delete("/api/orcamentos/{id}", orcamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orcamento.class);
        Orcamento orcamento1 = new Orcamento();
        orcamento1.setId(1L);
        Orcamento orcamento2 = new Orcamento();
        orcamento2.setId(orcamento1.getId());
        assertThat(orcamento1).isEqualTo(orcamento2);
        orcamento2.setId(2L);
        assertThat(orcamento1).isNotEqualTo(orcamento2);
        orcamento1.setId(null);
        assertThat(orcamento1).isNotEqualTo(orcamento2);
    }
}
