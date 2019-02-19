package com.web.rest;

import com.HmProjetosApp;

import com.domain.Codigos;
import com.repository.CodigosRepository;
import com.service.CodigosService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.CodigosCriteria;
import com.service.CodigosQueryService;

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
    private CodigosService codigosService;

    @Autowired
    private CodigosQueryService codigosQueryService;

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
        final CodigosResource codigosResource = new CodigosResource(codigosService, codigosQueryService);
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
    public void getAllCodigosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where tipo equals to DEFAULT_TIPO
        defaultCodigosShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the codigosList where tipo equals to UPDATED_TIPO
        defaultCodigosShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllCodigosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultCodigosShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the codigosList where tipo equals to UPDATED_TIPO
        defaultCodigosShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllCodigosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where tipo is not null
        defaultCodigosShouldBeFound("tipo.specified=true");

        // Get all the codigosList where tipo is null
        defaultCodigosShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCodigosByAnoIsEqualToSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where ano equals to DEFAULT_ANO
        defaultCodigosShouldBeFound("ano.equals=" + DEFAULT_ANO);

        // Get all the codigosList where ano equals to UPDATED_ANO
        defaultCodigosShouldNotBeFound("ano.equals=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    public void getAllCodigosByAnoIsInShouldWork() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where ano in DEFAULT_ANO or UPDATED_ANO
        defaultCodigosShouldBeFound("ano.in=" + DEFAULT_ANO + "," + UPDATED_ANO);

        // Get all the codigosList where ano equals to UPDATED_ANO
        defaultCodigosShouldNotBeFound("ano.in=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    public void getAllCodigosByAnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where ano is not null
        defaultCodigosShouldBeFound("ano.specified=true");

        // Get all the codigosList where ano is null
        defaultCodigosShouldNotBeFound("ano.specified=false");
    }

    @Test
    @Transactional
    public void getAllCodigosByAnoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where ano greater than or equals to DEFAULT_ANO
        defaultCodigosShouldBeFound("ano.greaterOrEqualThan=" + DEFAULT_ANO);

        // Get all the codigosList where ano greater than or equals to UPDATED_ANO
        defaultCodigosShouldNotBeFound("ano.greaterOrEqualThan=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    public void getAllCodigosByAnoIsLessThanSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where ano less than or equals to DEFAULT_ANO
        defaultCodigosShouldNotBeFound("ano.lessThan=" + DEFAULT_ANO);

        // Get all the codigosList where ano less than or equals to UPDATED_ANO
        defaultCodigosShouldBeFound("ano.lessThan=" + UPDATED_ANO);
    }


    @Test
    @Transactional
    public void getAllCodigosByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where numero equals to DEFAULT_NUMERO
        defaultCodigosShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the codigosList where numero equals to UPDATED_NUMERO
        defaultCodigosShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCodigosByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultCodigosShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the codigosList where numero equals to UPDATED_NUMERO
        defaultCodigosShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCodigosByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where numero is not null
        defaultCodigosShouldBeFound("numero.specified=true");

        // Get all the codigosList where numero is null
        defaultCodigosShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllCodigosByNumeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where numero greater than or equals to DEFAULT_NUMERO
        defaultCodigosShouldBeFound("numero.greaterOrEqualThan=" + DEFAULT_NUMERO);

        // Get all the codigosList where numero greater than or equals to UPDATED_NUMERO
        defaultCodigosShouldNotBeFound("numero.greaterOrEqualThan=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCodigosByNumeroIsLessThanSomething() throws Exception {
        // Initialize the database
        codigosRepository.saveAndFlush(codigos);

        // Get all the codigosList where numero less than or equals to DEFAULT_NUMERO
        defaultCodigosShouldNotBeFound("numero.lessThan=" + DEFAULT_NUMERO);

        // Get all the codigosList where numero less than or equals to UPDATED_NUMERO
        defaultCodigosShouldBeFound("numero.lessThan=" + UPDATED_NUMERO);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCodigosShouldBeFound(String filter) throws Exception {
        restCodigosMockMvc.perform(get("/api/codigos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigos.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCodigosShouldNotBeFound(String filter) throws Exception {
        restCodigosMockMvc.perform(get("/api/codigos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
        codigosService.save(codigos);

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
        codigosService.save(codigos);

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
