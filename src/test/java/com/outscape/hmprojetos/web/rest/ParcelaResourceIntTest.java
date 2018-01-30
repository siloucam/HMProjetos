package com.outscape.hmprojetos.web.rest;

import com.outscape.hmprojetos.HmProjetosApp;

import com.outscape.hmprojetos.domain.Parcela;
import com.outscape.hmprojetos.repository.ParcelaRepository;
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

import com.outscape.hmprojetos.domain.enumeration.StatusParcela;
/**
 * Test class for the ParcelaResource REST controller.
 *
 * @see ParcelaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class ParcelaResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final StatusParcela DEFAULT_STATUS = StatusParcela.PENDENTE;
    private static final StatusParcela UPDATED_STATUS = StatusParcela.EFETUADA;

    private static final Float DEFAULT_VALOR = 1F;
    private static final Float UPDATED_VALOR = 2F;

    private static final LocalDate DEFAULT_DTESTIPULADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTESTIPULADA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DTEFETUADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTEFETUADA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ParcelaRepository parcelaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParcelaMockMvc;

    private Parcela parcela;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParcelaResource parcelaResource = new ParcelaResource(parcelaRepository);
        this.restParcelaMockMvc = MockMvcBuilders.standaloneSetup(parcelaResource)
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
    public static Parcela createEntity(EntityManager em) {
        Parcela parcela = new Parcela()
            .descricao(DEFAULT_DESCRICAO)
            .status(DEFAULT_STATUS)
            .valor(DEFAULT_VALOR)
            .dtestipulada(DEFAULT_DTESTIPULADA)
            .dtefetuada(DEFAULT_DTEFETUADA);
        return parcela;
    }

    @Before
    public void initTest() {
        parcela = createEntity(em);
    }

    @Test
    @Transactional
    public void createParcela() throws Exception {
        int databaseSizeBeforeCreate = parcelaRepository.findAll().size();

        // Create the Parcela
        restParcelaMockMvc.perform(post("/api/parcelas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isCreated());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeCreate + 1);
        Parcela testParcela = parcelaList.get(parcelaList.size() - 1);
        assertThat(testParcela.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testParcela.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testParcela.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testParcela.getDtestipulada()).isEqualTo(DEFAULT_DTESTIPULADA);
        assertThat(testParcela.getDtefetuada()).isEqualTo(DEFAULT_DTEFETUADA);
    }

    @Test
    @Transactional
    public void createParcelaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parcelaRepository.findAll().size();

        // Create the Parcela with an existing ID
        parcela.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParcelaMockMvc.perform(post("/api/parcelas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isBadRequest());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcelaRepository.findAll().size();
        // set the field null
        parcela.setValor(null);

        // Create the Parcela, which fails.

        restParcelaMockMvc.perform(post("/api/parcelas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isBadRequest());

        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParcelas() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        // Get all the parcelaList
        restParcelaMockMvc.perform(get("/api/parcelas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parcela.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].dtestipulada").value(hasItem(DEFAULT_DTESTIPULADA.toString())))
            .andExpect(jsonPath("$.[*].dtefetuada").value(hasItem(DEFAULT_DTEFETUADA.toString())));
    }

    @Test
    @Transactional
    public void getParcela() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);

        // Get the parcela
        restParcelaMockMvc.perform(get("/api/parcelas/{id}", parcela.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parcela.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.dtestipulada").value(DEFAULT_DTESTIPULADA.toString()))
            .andExpect(jsonPath("$.dtefetuada").value(DEFAULT_DTEFETUADA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParcela() throws Exception {
        // Get the parcela
        restParcelaMockMvc.perform(get("/api/parcelas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParcela() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();

        // Update the parcela
        Parcela updatedParcela = parcelaRepository.findOne(parcela.getId());
        // Disconnect from session so that the updates on updatedParcela are not directly saved in db
        em.detach(updatedParcela);
        updatedParcela
            .descricao(UPDATED_DESCRICAO)
            .status(UPDATED_STATUS)
            .valor(UPDATED_VALOR)
            .dtestipulada(UPDATED_DTESTIPULADA)
            .dtefetuada(UPDATED_DTEFETUADA);

        restParcelaMockMvc.perform(put("/api/parcelas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParcela)))
            .andExpect(status().isOk());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate);
        Parcela testParcela = parcelaList.get(parcelaList.size() - 1);
        assertThat(testParcela.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testParcela.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParcela.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testParcela.getDtestipulada()).isEqualTo(UPDATED_DTESTIPULADA);
        assertThat(testParcela.getDtefetuada()).isEqualTo(UPDATED_DTEFETUADA);
    }

    @Test
    @Transactional
    public void updateNonExistingParcela() throws Exception {
        int databaseSizeBeforeUpdate = parcelaRepository.findAll().size();

        // Create the Parcela

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParcelaMockMvc.perform(put("/api/parcelas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parcela)))
            .andExpect(status().isCreated());

        // Validate the Parcela in the database
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParcela() throws Exception {
        // Initialize the database
        parcelaRepository.saveAndFlush(parcela);
        int databaseSizeBeforeDelete = parcelaRepository.findAll().size();

        // Get the parcela
        restParcelaMockMvc.perform(delete("/api/parcelas/{id}", parcela.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Parcela> parcelaList = parcelaRepository.findAll();
        assertThat(parcelaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parcela.class);
        Parcela parcela1 = new Parcela();
        parcela1.setId(1L);
        Parcela parcela2 = new Parcela();
        parcela2.setId(parcela1.getId());
        assertThat(parcela1).isEqualTo(parcela2);
        parcela2.setId(2L);
        assertThat(parcela1).isNotEqualTo(parcela2);
        parcela1.setId(null);
        assertThat(parcela1).isNotEqualTo(parcela2);
    }
}
