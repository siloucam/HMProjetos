package com.web.rest;

import com.HmProjetosApp;

import com.domain.CodigoPrefeitura;
import com.domain.Servico;
import com.repository.CodigoPrefeituraRepository;
import com.service.CodigoPrefeituraService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.CodigoPrefeituraCriteria;
import com.service.CodigoPrefeituraQueryService;

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
 * Test class for the CodigoPrefeituraResource REST controller.
 *
 * @see CodigoPrefeituraResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class CodigoPrefeituraResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_ANO = "AAAAAAAAAA";
    private static final String UPDATED_ANO = "BBBBBBBBBB";

    @Autowired
    private CodigoPrefeituraRepository codigoPrefeituraRepository;

    @Autowired
    private CodigoPrefeituraService codigoPrefeituraService;

    @Autowired
    private CodigoPrefeituraQueryService codigoPrefeituraQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCodigoPrefeituraMockMvc;

    private CodigoPrefeitura codigoPrefeitura;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CodigoPrefeituraResource codigoPrefeituraResource = new CodigoPrefeituraResource(codigoPrefeituraService, codigoPrefeituraQueryService);
        this.restCodigoPrefeituraMockMvc = MockMvcBuilders.standaloneSetup(codigoPrefeituraResource)
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
    public static CodigoPrefeitura createEntity(EntityManager em) {
        CodigoPrefeitura codigoPrefeitura = new CodigoPrefeitura()
            .numero(DEFAULT_NUMERO)
            .ano(DEFAULT_ANO);
        return codigoPrefeitura;
    }

    @Before
    public void initTest() {
        codigoPrefeitura = createEntity(em);
    }

    @Test
    @Transactional
    public void createCodigoPrefeitura() throws Exception {
        int databaseSizeBeforeCreate = codigoPrefeituraRepository.findAll().size();

        // Create the CodigoPrefeitura
        restCodigoPrefeituraMockMvc.perform(post("/api/codigo-prefeituras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codigoPrefeitura)))
            .andExpect(status().isCreated());

        // Validate the CodigoPrefeitura in the database
        List<CodigoPrefeitura> codigoPrefeituraList = codigoPrefeituraRepository.findAll();
        assertThat(codigoPrefeituraList).hasSize(databaseSizeBeforeCreate + 1);
        CodigoPrefeitura testCodigoPrefeitura = codigoPrefeituraList.get(codigoPrefeituraList.size() - 1);
        assertThat(testCodigoPrefeitura.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCodigoPrefeitura.getAno()).isEqualTo(DEFAULT_ANO);
    }

    @Test
    @Transactional
    public void createCodigoPrefeituraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = codigoPrefeituraRepository.findAll().size();

        // Create the CodigoPrefeitura with an existing ID
        codigoPrefeitura.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodigoPrefeituraMockMvc.perform(post("/api/codigo-prefeituras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codigoPrefeitura)))
            .andExpect(status().isBadRequest());

        // Validate the CodigoPrefeitura in the database
        List<CodigoPrefeitura> codigoPrefeituraList = codigoPrefeituraRepository.findAll();
        assertThat(codigoPrefeituraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeituras() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList
        restCodigoPrefeituraMockMvc.perform(get("/api/codigo-prefeituras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigoPrefeitura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO.toString())));
    }

    @Test
    @Transactional
    public void getCodigoPrefeitura() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get the codigoPrefeitura
        restCodigoPrefeituraMockMvc.perform(get("/api/codigo-prefeituras/{id}", codigoPrefeitura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(codigoPrefeitura.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO.toString()));
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList where numero equals to DEFAULT_NUMERO
        defaultCodigoPrefeituraShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the codigoPrefeituraList where numero equals to UPDATED_NUMERO
        defaultCodigoPrefeituraShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultCodigoPrefeituraShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the codigoPrefeituraList where numero equals to UPDATED_NUMERO
        defaultCodigoPrefeituraShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList where numero is not null
        defaultCodigoPrefeituraShouldBeFound("numero.specified=true");

        // Get all the codigoPrefeituraList where numero is null
        defaultCodigoPrefeituraShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByAnoIsEqualToSomething() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList where ano equals to DEFAULT_ANO
        defaultCodigoPrefeituraShouldBeFound("ano.equals=" + DEFAULT_ANO);

        // Get all the codigoPrefeituraList where ano equals to UPDATED_ANO
        defaultCodigoPrefeituraShouldNotBeFound("ano.equals=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByAnoIsInShouldWork() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList where ano in DEFAULT_ANO or UPDATED_ANO
        defaultCodigoPrefeituraShouldBeFound("ano.in=" + DEFAULT_ANO + "," + UPDATED_ANO);

        // Get all the codigoPrefeituraList where ano equals to UPDATED_ANO
        defaultCodigoPrefeituraShouldNotBeFound("ano.in=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByAnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);

        // Get all the codigoPrefeituraList where ano is not null
        defaultCodigoPrefeituraShouldBeFound("ano.specified=true");

        // Get all the codigoPrefeituraList where ano is null
        defaultCodigoPrefeituraShouldNotBeFound("ano.specified=false");
    }

    @Test
    @Transactional
    public void getAllCodigoPrefeiturasByServicoIsEqualToSomething() throws Exception {
        // Initialize the database
        Servico servico = ServicoResourceIntTest.createEntity(em);
        em.persist(servico);
        em.flush();
        codigoPrefeitura.setServico(servico);
        codigoPrefeituraRepository.saveAndFlush(codigoPrefeitura);
        Long servicoId = servico.getId();

        // Get all the codigoPrefeituraList where servico equals to servicoId
        defaultCodigoPrefeituraShouldBeFound("servicoId.equals=" + servicoId);

        // Get all the codigoPrefeituraList where servico equals to servicoId + 1
        defaultCodigoPrefeituraShouldNotBeFound("servicoId.equals=" + (servicoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCodigoPrefeituraShouldBeFound(String filter) throws Exception {
        restCodigoPrefeituraMockMvc.perform(get("/api/codigo-prefeituras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codigoPrefeitura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCodigoPrefeituraShouldNotBeFound(String filter) throws Exception {
        restCodigoPrefeituraMockMvc.perform(get("/api/codigo-prefeituras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCodigoPrefeitura() throws Exception {
        // Get the codigoPrefeitura
        restCodigoPrefeituraMockMvc.perform(get("/api/codigo-prefeituras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCodigoPrefeitura() throws Exception {
        // Initialize the database
        codigoPrefeituraService.save(codigoPrefeitura);

        int databaseSizeBeforeUpdate = codigoPrefeituraRepository.findAll().size();

        // Update the codigoPrefeitura
        CodigoPrefeitura updatedCodigoPrefeitura = codigoPrefeituraRepository.findOne(codigoPrefeitura.getId());
        // Disconnect from session so that the updates on updatedCodigoPrefeitura are not directly saved in db
        em.detach(updatedCodigoPrefeitura);
        updatedCodigoPrefeitura
            .numero(UPDATED_NUMERO)
            .ano(UPDATED_ANO);

        restCodigoPrefeituraMockMvc.perform(put("/api/codigo-prefeituras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCodigoPrefeitura)))
            .andExpect(status().isOk());

        // Validate the CodigoPrefeitura in the database
        List<CodigoPrefeitura> codigoPrefeituraList = codigoPrefeituraRepository.findAll();
        assertThat(codigoPrefeituraList).hasSize(databaseSizeBeforeUpdate);
        CodigoPrefeitura testCodigoPrefeitura = codigoPrefeituraList.get(codigoPrefeituraList.size() - 1);
        assertThat(testCodigoPrefeitura.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCodigoPrefeitura.getAno()).isEqualTo(UPDATED_ANO);
    }

    @Test
    @Transactional
    public void updateNonExistingCodigoPrefeitura() throws Exception {
        int databaseSizeBeforeUpdate = codigoPrefeituraRepository.findAll().size();

        // Create the CodigoPrefeitura

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCodigoPrefeituraMockMvc.perform(put("/api/codigo-prefeituras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(codigoPrefeitura)))
            .andExpect(status().isCreated());

        // Validate the CodigoPrefeitura in the database
        List<CodigoPrefeitura> codigoPrefeituraList = codigoPrefeituraRepository.findAll();
        assertThat(codigoPrefeituraList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCodigoPrefeitura() throws Exception {
        // Initialize the database
        codigoPrefeituraService.save(codigoPrefeitura);

        int databaseSizeBeforeDelete = codigoPrefeituraRepository.findAll().size();

        // Get the codigoPrefeitura
        restCodigoPrefeituraMockMvc.perform(delete("/api/codigo-prefeituras/{id}", codigoPrefeitura.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CodigoPrefeitura> codigoPrefeituraList = codigoPrefeituraRepository.findAll();
        assertThat(codigoPrefeituraList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodigoPrefeitura.class);
        CodigoPrefeitura codigoPrefeitura1 = new CodigoPrefeitura();
        codigoPrefeitura1.setId(1L);
        CodigoPrefeitura codigoPrefeitura2 = new CodigoPrefeitura();
        codigoPrefeitura2.setId(codigoPrefeitura1.getId());
        assertThat(codigoPrefeitura1).isEqualTo(codigoPrefeitura2);
        codigoPrefeitura2.setId(2L);
        assertThat(codigoPrefeitura1).isNotEqualTo(codigoPrefeitura2);
        codigoPrefeitura1.setId(null);
        assertThat(codigoPrefeitura1).isNotEqualTo(codigoPrefeitura2);
    }
}
