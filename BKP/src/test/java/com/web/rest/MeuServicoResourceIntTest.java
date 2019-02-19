package com.web.rest;

import com.HmProjetosApp;

import com.domain.MeuServico;
import com.repository.MeuServicoRepository;
import com.service.MeuServicoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.MeuServicoCriteria;
import com.service.MeuServicoQueryService;

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
 * Test class for the MeuServicoResource REST controller.
 *
 * @see MeuServicoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class MeuServicoResourceIntTest {

    @Autowired
    private MeuServicoRepository meuServicoRepository;

    @Autowired
    private MeuServicoService meuServicoService;

    @Autowired
    private MeuServicoQueryService meuServicoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMeuServicoMockMvc;

    private MeuServico meuServico;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeuServicoResource meuServicoResource = new MeuServicoResource(meuServicoService, meuServicoQueryService);
        this.restMeuServicoMockMvc = MockMvcBuilders.standaloneSetup(meuServicoResource)
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
    public static MeuServico createEntity(EntityManager em) {
        MeuServico meuServico = new MeuServico();
        return meuServico;
    }

    @Before
    public void initTest() {
        meuServico = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeuServico() throws Exception {
        int databaseSizeBeforeCreate = meuServicoRepository.findAll().size();

        // Create the MeuServico
        restMeuServicoMockMvc.perform(post("/api/meu-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meuServico)))
            .andExpect(status().isCreated());

        // Validate the MeuServico in the database
        List<MeuServico> meuServicoList = meuServicoRepository.findAll();
        assertThat(meuServicoList).hasSize(databaseSizeBeforeCreate + 1);
        MeuServico testMeuServico = meuServicoList.get(meuServicoList.size() - 1);
    }

    @Test
    @Transactional
    public void createMeuServicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = meuServicoRepository.findAll().size();

        // Create the MeuServico with an existing ID
        meuServico.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeuServicoMockMvc.perform(post("/api/meu-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meuServico)))
            .andExpect(status().isBadRequest());

        // Validate the MeuServico in the database
        List<MeuServico> meuServicoList = meuServicoRepository.findAll();
        assertThat(meuServicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeuServicos() throws Exception {
        // Initialize the database
        meuServicoRepository.saveAndFlush(meuServico);

        // Get all the meuServicoList
        restMeuServicoMockMvc.perform(get("/api/meu-servicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meuServico.getId().intValue())));
    }

    @Test
    @Transactional
    public void getMeuServico() throws Exception {
        // Initialize the database
        meuServicoRepository.saveAndFlush(meuServico);

        // Get the meuServico
        restMeuServicoMockMvc.perform(get("/api/meu-servicos/{id}", meuServico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meuServico.getId().intValue()));
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMeuServicoShouldBeFound(String filter) throws Exception {
        restMeuServicoMockMvc.perform(get("/api/meu-servicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meuServico.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMeuServicoShouldNotBeFound(String filter) throws Exception {
        restMeuServicoMockMvc.perform(get("/api/meu-servicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMeuServico() throws Exception {
        // Get the meuServico
        restMeuServicoMockMvc.perform(get("/api/meu-servicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeuServico() throws Exception {
        // Initialize the database
        meuServicoService.save(meuServico);

        int databaseSizeBeforeUpdate = meuServicoRepository.findAll().size();

        // Update the meuServico
        MeuServico updatedMeuServico = meuServicoRepository.findOne(meuServico.getId());
        // Disconnect from session so that the updates on updatedMeuServico are not directly saved in db
        em.detach(updatedMeuServico);

        restMeuServicoMockMvc.perform(put("/api/meu-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeuServico)))
            .andExpect(status().isOk());

        // Validate the MeuServico in the database
        List<MeuServico> meuServicoList = meuServicoRepository.findAll();
        assertThat(meuServicoList).hasSize(databaseSizeBeforeUpdate);
        MeuServico testMeuServico = meuServicoList.get(meuServicoList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingMeuServico() throws Exception {
        int databaseSizeBeforeUpdate = meuServicoRepository.findAll().size();

        // Create the MeuServico

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeuServicoMockMvc.perform(put("/api/meu-servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meuServico)))
            .andExpect(status().isCreated());

        // Validate the MeuServico in the database
        List<MeuServico> meuServicoList = meuServicoRepository.findAll();
        assertThat(meuServicoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeuServico() throws Exception {
        // Initialize the database
        meuServicoService.save(meuServico);

        int databaseSizeBeforeDelete = meuServicoRepository.findAll().size();

        // Get the meuServico
        restMeuServicoMockMvc.perform(delete("/api/meu-servicos/{id}", meuServico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MeuServico> meuServicoList = meuServicoRepository.findAll();
        assertThat(meuServicoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeuServico.class);
        MeuServico meuServico1 = new MeuServico();
        meuServico1.setId(1L);
        MeuServico meuServico2 = new MeuServico();
        meuServico2.setId(meuServico1.getId());
        assertThat(meuServico1).isEqualTo(meuServico2);
        meuServico2.setId(2L);
        assertThat(meuServico1).isNotEqualTo(meuServico2);
        meuServico1.setId(null);
        assertThat(meuServico1).isNotEqualTo(meuServico2);
    }
}
