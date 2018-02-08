package com.outscape.hmprojetos.web.rest;

import com.outscape.hmprojetos.HmProjetosApp;

import com.outscape.hmprojetos.domain.Codigos;
import com.outscape.hmprojetos.repository.CodigosRepository;
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
 * Test class for the CodigosResource REST controller.
 *
 * @see CodigosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class CodigosResourceIntTest {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO = 1;
    private static final Integer UPDATED_ANO = 2;

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    @Autowired
    private CodigosRepository codigosRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCodigosMockMvc;

    private Codigos codigos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CodigosResource codigosResource = new CodigosResource(codigosRepository);
        this.restCodigosMockMvc = MockMvcBuilders.standaloneSetup(codigosResource)
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
    public static Codigos createEntity(EntityManager em) {
        Codigos codigos = new Codigos()
            .tipo(DEFAULT_TIPO)
            .ano(DEFAULT_ANO)
            .numero(DEFAULT_NUMERO);
        return codigos;
    }

    @Before
    public void initTest() {
        codigos = createEntity(em);
    }

    @Test
    @Transactional
    public void createCodigos() throws Exception {
        int databaseSizeBeforeCreate = codigosRepository.findAll().size();

        // Create the Codigos
        restCodigosMockMvc.perform(post("/api/codigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codigos)))
            .andExpect(status().isCreated());

        // Validate the Codigos in the database
        List<Codigos> codigosList = codigosRepository.findAll();
        assertThat(codigosList).hasSize(databaseSizeBeforeCreate + 1);
        Codigos testCodigos = codigosList.get(codigosList.size() - 1);
        assertThat(testCodigos.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCodigos.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testCodigos.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createCodigosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = codigosRepository.findAll().size();

        // Create the Codigos with an existing ID
        codigos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodigosMockMvc.perform(post("/api/codigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codigos)))
            .andExpect(status().isBadRequest());

        // Validate the Codigos in the database
        List<Codigos> codigosList = codigosRepository.findAll();
        assertThat(codigosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCodigos() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList
        restCodigosMockMvc.perform(get("/api/codigos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigos.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getCodigos() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get the codigos
        restCodigosMockMvc.perform(get("/api/codigos/{id}", codigos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(codigos.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingCodigos() throws Exception {
        // Get the codigos
        restCodigosMockMvc.perform(get("/api/codigos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCodigos() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);
        int databaseSizeBeforeUpdate = codigosRepository.findAll().size();

        // Update the codigos
        Codigos updatedCodigos = codigosRepository.findOne(codigos.getId());
        // Disconnect from session so that the updates on updatedCodigos are not directly saved in db
        em.detach(updatedCodigos);
        updatedCodigos
            .tipo(UPDATED_TIPO)
            .ano(UPDATED_ANO)
            .numero(UPDATED_NUMERO);

        restCodigosMockMvc.perform(put("/api/codigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCodigos)))
            .andExpect(status().isOk());

        // Validate the Codigos in the database
        List<Codigos> codigosList = codigosRepository.findAll();
        assertThat(codigosList).hasSize(databaseSizeBeforeUpdate);
        Codigos testCodigos = codigosList.get(codigosList.size() - 1);
        assertThat(testCodigos.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCodigos.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testCodigos.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void updateNonExistingCodigos() throws Exception {
        int databaseSizeBeforeUpdate = codigosRepository.findAll().size();

        // Create the Codigos

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCodigosMockMvc.perform(put("/api/codigos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codigos)))
            .andExpect(status().isCreated());

        // Validate the Codigos in the database
        List<Codigos> codigosList = codigosRepository.findAll();
        assertThat(codigosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCodigos() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);
        int databaseSizeBeforeDelete = codigosRepository.findAll().size();

        // Get the codigos
        restCodigosMockMvc.perform(delete("/api/codigos/{id}", codigos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Codigos> codigosList = codigosRepository.findAll();
        assertThat(codigosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Codigos.class);
        Codigos codigos1 = new Codigos();
        codigos1.setId(1L);
        Codigos codigos2 = new Codigos();
        codigos2.setId(codigos1.getId());
        assertThat(codigos1).isEqualTo(codigos2);
        codigos2.setId(2L);
        assertThat(codigos1).isNotEqualTo(codigos2);
        codigos1.setId(null);
        assertThat(codigos1).isNotEqualTo(codigos2);
    }
}
