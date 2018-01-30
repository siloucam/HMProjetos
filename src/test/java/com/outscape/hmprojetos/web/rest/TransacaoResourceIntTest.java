package com.outscape.hmprojetos.web.rest;

import com.outscape.hmprojetos.HmProjetosApp;

import com.outscape.hmprojetos.domain.Transacao;
import com.outscape.hmprojetos.repository.TransacaoRepository;
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

import com.outscape.hmprojetos.domain.enumeration.OP;
/**
 * Test class for the TransacaoResource REST controller.
 *
 * @see TransacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class TransacaoResourceIntTest {

    private static final Float DEFAULT_VALOR = 1F;
    private static final Float UPDATED_VALOR = 2F;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final OP DEFAULT_OPERACAO = OP.CREDITO;
    private static final OP UPDATED_OPERACAO = OP.DEBITO;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransacaoMockMvc;

    private Transacao transacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransacaoResource transacaoResource = new TransacaoResource(transacaoRepository);
        this.restTransacaoMockMvc = MockMvcBuilders.standaloneSetup(transacaoResource)
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
    public static Transacao createEntity(EntityManager em) {
        Transacao transacao = new Transacao()
            .valor(DEFAULT_VALOR)
            .data(DEFAULT_DATA)
            .descricao(DEFAULT_DESCRICAO)
            .operacao(DEFAULT_OPERACAO);
        return transacao;
    }

    @Before
    public void initTest() {
        transacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransacao() throws Exception {
        int databaseSizeBeforeCreate = transacaoRepository.findAll().size();

        // Create the Transacao
        restTransacaoMockMvc.perform(post("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transacao)))
            .andExpect(status().isCreated());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Transacao testTransacao = transacaoList.get(transacaoList.size() - 1);
        assertThat(testTransacao.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testTransacao.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testTransacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTransacao.getOperacao()).isEqualTo(DEFAULT_OPERACAO);
    }

    @Test
    @Transactional
    public void createTransacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transacaoRepository.findAll().size();

        // Create the Transacao with an existing ID
        transacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransacaoMockMvc.perform(post("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transacao)))
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = transacaoRepository.findAll().size();
        // set the field null
        transacao.setValor(null);

        // Create the Transacao, which fails.

        restTransacaoMockMvc.perform(post("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transacao)))
            .andExpect(status().isBadRequest());

        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = transacaoRepository.findAll().size();
        // set the field null
        transacao.setData(null);

        // Create the Transacao, which fails.

        restTransacaoMockMvc.perform(post("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transacao)))
            .andExpect(status().isBadRequest());

        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transacaoRepository.findAll().size();
        // set the field null
        transacao.setOperacao(null);

        // Create the Transacao, which fails.

        restTransacaoMockMvc.perform(post("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transacao)))
            .andExpect(status().isBadRequest());

        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransacaos() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList
        restTransacaoMockMvc.perform(get("/api/transacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].operacao").value(hasItem(DEFAULT_OPERACAO.toString())));
    }

    @Test
    @Transactional
    public void getTransacao() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get the transacao
        restTransacaoMockMvc.perform(get("/api/transacaos/{id}", transacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transacao.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.operacao").value(DEFAULT_OPERACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransacao() throws Exception {
        // Get the transacao
        restTransacaoMockMvc.perform(get("/api/transacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransacao() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();

        // Update the transacao
        Transacao updatedTransacao = transacaoRepository.findOne(transacao.getId());
        // Disconnect from session so that the updates on updatedTransacao are not directly saved in db
        em.detach(updatedTransacao);
        updatedTransacao
            .valor(UPDATED_VALOR)
            .data(UPDATED_DATA)
            .descricao(UPDATED_DESCRICAO)
            .operacao(UPDATED_OPERACAO);

        restTransacaoMockMvc.perform(put("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransacao)))
            .andExpect(status().isOk());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
        Transacao testTransacao = transacaoList.get(transacaoList.size() - 1);
        assertThat(testTransacao.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testTransacao.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testTransacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTransacao.getOperacao()).isEqualTo(UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();

        // Create the Transacao

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransacaoMockMvc.perform(put("/api/transacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transacao)))
            .andExpect(status().isCreated());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransacao() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);
        int databaseSizeBeforeDelete = transacaoRepository.findAll().size();

        // Get the transacao
        restTransacaoMockMvc.perform(delete("/api/transacaos/{id}", transacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transacao.class);
        Transacao transacao1 = new Transacao();
        transacao1.setId(1L);
        Transacao transacao2 = new Transacao();
        transacao2.setId(transacao1.getId());
        assertThat(transacao1).isEqualTo(transacao2);
        transacao2.setId(2L);
        assertThat(transacao1).isNotEqualTo(transacao2);
        transacao1.setId(null);
        assertThat(transacao1).isNotEqualTo(transacao2);
    }
}
