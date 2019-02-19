package com.web.rest;

import com.HmProjetosApp;

import com.domain.DescricaoServico;
import com.repository.DescricaoServicoRepository;
import com.service.DescricaoServicoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.DescricaoServicoCriteria;
import com.service.DescricaoServicoQueryService;

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

import com.domain.enumeration.TipoServico;
/**
 * Test class for the DescricaoServicoResource REST controller.
 *
 * @see DescricaoServicoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class DescricaoServicoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final TipoServico DEFAULT_TIPO = TipoServico.PAQ;
    private static final TipoServico UPDATED_TIPO = TipoServico.ASS;

    @Autowired
    private DescricaoServicoRepository descricaoServicoRepository;

    @Autowired
    private DescricaoServicoService descricaoServicoService;

    @Autowired
    private DescricaoServicoQueryService descricaoServicoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDescricaoServicoMockMvc;

    private DescricaoServico descricaoServico;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescricaoServicoResource descricaoServicoResource = new DescricaoServicoResource(descricaoServicoService, descricaoServicoQueryService);
        this.restDescricaoServicoMockMvc = MockMvcBuilders.standaloneSetup(descricaoServicoResource)
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
    public static DescricaoServico createEntity(EntityManager em) {
        DescricaoServico descricaoServico = new DescricaoServico()
            .nome(DEFAULT_NOME)
            .tipo(DEFAULT_TIPO);
        return descricaoServico;
    }

    @Before
    public void initTest() {
        descricaoServico = createEntity(em);
    }

    @Test
    @Transactional
    public void createDescricaoServico() throws Exception {
        int databaseSizeBeforeCreate = descricaoServicoRepository.findAll().size();

        // Create the DescricaoServico
        restDescricaoServicoMockMvc.perform(post("/api/descricao-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoServico)))
            .andExpect(status().isCreated());

        // Validate the DescricaoServico in the database
        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeCreate + 1);
        DescricaoServico testDescricaoServico = descricaoServicoList.get(descricaoServicoList.size() - 1);
        assertThat(testDescricaoServico.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDescricaoServico.getTipo()).isEqualTo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void createDescricaoServicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descricaoServicoRepository.findAll().size();

        // Create the DescricaoServico with an existing ID
        descricaoServico.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescricaoServicoMockMvc.perform(post("/api/descricao-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoServico)))
            .andExpect(status().isBadRequest());

        // Validate the DescricaoServico in the database
        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = descricaoServicoRepository.findAll().size();
        // set the field null
        descricaoServico.setNome(null);

        // Create the DescricaoServico, which fails.

        restDescricaoServicoMockMvc.perform(post("/api/descricao-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoServico)))
            .andExpect(status().isBadRequest());

        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = descricaoServicoRepository.findAll().size();
        // set the field null
        descricaoServico.setTipo(null);

        // Create the DescricaoServico, which fails.

        restDescricaoServicoMockMvc.perform(post("/api/descricao-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoServico)))
            .andExpect(status().isBadRequest());

        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDescricaoServicos() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList
        restDescricaoServicoMockMvc.perform(get("/api/descricao-servicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descricaoServico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getDescricaoServico() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get the descricaoServico
        restDescricaoServicoMockMvc.perform(get("/api/descricao-servicos/{id}", descricaoServico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(descricaoServico.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getAllDescricaoServicosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList where nome equals to DEFAULT_NOME
        defaultDescricaoServicoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the descricaoServicoList where nome equals to UPDATED_NOME
        defaultDescricaoServicoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDescricaoServicosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultDescricaoServicoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the descricaoServicoList where nome equals to UPDATED_NOME
        defaultDescricaoServicoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllDescricaoServicosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList where nome is not null
        defaultDescricaoServicoShouldBeFound("nome.specified=true");

        // Get all the descricaoServicoList where nome is null
        defaultDescricaoServicoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllDescricaoServicosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList where tipo equals to DEFAULT_TIPO
        defaultDescricaoServicoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the descricaoServicoList where tipo equals to UPDATED_TIPO
        defaultDescricaoServicoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllDescricaoServicosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultDescricaoServicoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the descricaoServicoList where tipo equals to UPDATED_TIPO
        defaultDescricaoServicoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllDescricaoServicosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        descricaoServicoRepository.saveAndFlush(descricaoServico);

        // Get all the descricaoServicoList where tipo is not null
        defaultDescricaoServicoShouldBeFound("tipo.specified=true");

        // Get all the descricaoServicoList where tipo is null
        defaultDescricaoServicoShouldNotBeFound("tipo.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDescricaoServicoShouldBeFound(String filter) throws Exception {
        restDescricaoServicoMockMvc.perform(get("/api/descricao-servicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descricaoServico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDescricaoServicoShouldNotBeFound(String filter) throws Exception {
        restDescricaoServicoMockMvc.perform(get("/api/descricao-servicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDescricaoServico() throws Exception {
        // Get the descricaoServico
        restDescricaoServicoMockMvc.perform(get("/api/descricao-servicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDescricaoServico() throws Exception {
        // Initialize the database
        descricaoServicoService.save(descricaoServico);

        int databaseSizeBeforeUpdate = descricaoServicoRepository.findAll().size();

        // Update the descricaoServico
        DescricaoServico updatedDescricaoServico = descricaoServicoRepository.findOne(descricaoServico.getId());
        // Disconnect from session so that the updates on updatedDescricaoServico are not directly saved in db
        em.detach(updatedDescricaoServico);
        updatedDescricaoServico
            .nome(UPDATED_NOME)
            .tipo(UPDATED_TIPO);

        restDescricaoServicoMockMvc.perform(put("/api/descricao-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDescricaoServico)))
            .andExpect(status().isOk());

        // Validate the DescricaoServico in the database
        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeUpdate);
        DescricaoServico testDescricaoServico = descricaoServicoList.get(descricaoServicoList.size() - 1);
        assertThat(testDescricaoServico.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDescricaoServico.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void updateNonExistingDescricaoServico() throws Exception {
        int databaseSizeBeforeUpdate = descricaoServicoRepository.findAll().size();

        // Create the DescricaoServico

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDescricaoServicoMockMvc.perform(put("/api/descricao-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descricaoServico)))
            .andExpect(status().isCreated());

        // Validate the DescricaoServico in the database
        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDescricaoServico() throws Exception {
        // Initialize the database
        descricaoServicoService.save(descricaoServico);

        int databaseSizeBeforeDelete = descricaoServicoRepository.findAll().size();

        // Get the descricaoServico
        restDescricaoServicoMockMvc.perform(delete("/api/descricao-servicos/{id}", descricaoServico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DescricaoServico> descricaoServicoList = descricaoServicoRepository.findAll();
        assertThat(descricaoServicoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescricaoServico.class);
        DescricaoServico descricaoServico1 = new DescricaoServico();
        descricaoServico1.setId(1L);
        DescricaoServico descricaoServico2 = new DescricaoServico();
        descricaoServico2.setId(descricaoServico1.getId());
        assertThat(descricaoServico1).isEqualTo(descricaoServico2);
        descricaoServico2.setId(2L);
        assertThat(descricaoServico1).isNotEqualTo(descricaoServico2);
        descricaoServico1.setId(null);
        assertThat(descricaoServico1).isNotEqualTo(descricaoServico2);
    }
}
