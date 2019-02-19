package com.web.rest;

import com.HmProjetosApp;

import com.domain.LinkExterno;
import com.domain.Servico;
import com.repository.LinkExternoRepository;
import com.service.LinkExternoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.LinkExternoCriteria;
import com.service.LinkExternoQueryService;

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
 * Test class for the LinkExternoResource REST controller.
 *
 * @see LinkExternoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class LinkExternoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Autowired
    private LinkExternoRepository linkExternoRepository;

    @Autowired
    private LinkExternoService linkExternoService;

    @Autowired
    private LinkExternoQueryService linkExternoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLinkExternoMockMvc;

    private LinkExterno linkExterno;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LinkExternoResource linkExternoResource = new LinkExternoResource(linkExternoService, linkExternoQueryService);
        this.restLinkExternoMockMvc = MockMvcBuilders.standaloneSetup(linkExternoResource)
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
    public static LinkExterno createEntity(EntityManager em) {
        LinkExterno linkExterno = new LinkExterno()
            .nome(DEFAULT_NOME)
            .link(DEFAULT_LINK);
        return linkExterno;
    }

    @Before
    public void initTest() {
        linkExterno = createEntity(em);
    }

    @Test
    @Transactional
    public void createLinkExterno() throws Exception {
        int databaseSizeBeforeCreate = linkExternoRepository.findAll().size();

        // Create the LinkExterno
        restLinkExternoMockMvc.perform(post("/api/link-externos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkExterno)))
            .andExpect(status().isCreated());

        // Validate the LinkExterno in the database
        List<LinkExterno> linkExternoList = linkExternoRepository.findAll();
        assertThat(linkExternoList).hasSize(databaseSizeBeforeCreate + 1);
        LinkExterno testLinkExterno = linkExternoList.get(linkExternoList.size() - 1);
        assertThat(testLinkExterno.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testLinkExterno.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createLinkExternoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = linkExternoRepository.findAll().size();

        // Create the LinkExterno with an existing ID
        linkExterno.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkExternoMockMvc.perform(post("/api/link-externos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkExterno)))
            .andExpect(status().isBadRequest());

        // Validate the LinkExterno in the database
        List<LinkExterno> linkExternoList = linkExternoRepository.findAll();
        assertThat(linkExternoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLinkExternos() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList
        restLinkExternoMockMvc.perform(get("/api/link-externos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkExterno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getLinkExterno() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get the linkExterno
        restLinkExternoMockMvc.perform(get("/api/link-externos/{id}", linkExterno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(linkExterno.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getAllLinkExternosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList where nome equals to DEFAULT_NOME
        defaultLinkExternoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the linkExternoList where nome equals to UPDATED_NOME
        defaultLinkExternoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllLinkExternosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultLinkExternoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the linkExternoList where nome equals to UPDATED_NOME
        defaultLinkExternoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllLinkExternosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList where nome is not null
        defaultLinkExternoShouldBeFound("nome.specified=true");

        // Get all the linkExternoList where nome is null
        defaultLinkExternoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllLinkExternosByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList where link equals to DEFAULT_LINK
        defaultLinkExternoShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the linkExternoList where link equals to UPDATED_LINK
        defaultLinkExternoShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllLinkExternosByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList where link in DEFAULT_LINK or UPDATED_LINK
        defaultLinkExternoShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the linkExternoList where link equals to UPDATED_LINK
        defaultLinkExternoShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllLinkExternosByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        linkExternoRepository.saveAndFlush(linkExterno);

        // Get all the linkExternoList where link is not null
        defaultLinkExternoShouldBeFound("link.specified=true");

        // Get all the linkExternoList where link is null
        defaultLinkExternoShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    public void getAllLinkExternosByServicoIsEqualToSomething() throws Exception {
        // Initialize the database
        Servico servico = ServicoResourceIntTest.createEntity(em);
        em.persist(servico);
        em.flush();
        linkExterno.setServico(servico);
        linkExternoRepository.saveAndFlush(linkExterno);
        Long servicoId = servico.getId();

        // Get all the linkExternoList where servico equals to servicoId
        defaultLinkExternoShouldBeFound("servicoId.equals=" + servicoId);

        // Get all the linkExternoList where servico equals to servicoId + 1
        defaultLinkExternoShouldNotBeFound("servicoId.equals=" + (servicoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLinkExternoShouldBeFound(String filter) throws Exception {
        restLinkExternoMockMvc.perform(get("/api/link-externos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkExterno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLinkExternoShouldNotBeFound(String filter) throws Exception {
        restLinkExternoMockMvc.perform(get("/api/link-externos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingLinkExterno() throws Exception {
        // Get the linkExterno
        restLinkExternoMockMvc.perform(get("/api/link-externos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLinkExterno() throws Exception {
        // Initialize the database
        linkExternoService.save(linkExterno);

        int databaseSizeBeforeUpdate = linkExternoRepository.findAll().size();

        // Update the linkExterno
        LinkExterno updatedLinkExterno = linkExternoRepository.findOne(linkExterno.getId());
        // Disconnect from session so that the updates on updatedLinkExterno are not directly saved in db
        em.detach(updatedLinkExterno);
        updatedLinkExterno
            .nome(UPDATED_NOME)
            .link(UPDATED_LINK);

        restLinkExternoMockMvc.perform(put("/api/link-externos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLinkExterno)))
            .andExpect(status().isOk());

        // Validate the LinkExterno in the database
        List<LinkExterno> linkExternoList = linkExternoRepository.findAll();
        assertThat(linkExternoList).hasSize(databaseSizeBeforeUpdate);
        LinkExterno testLinkExterno = linkExternoList.get(linkExternoList.size() - 1);
        assertThat(testLinkExterno.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testLinkExterno.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void updateNonExistingLinkExterno() throws Exception {
        int databaseSizeBeforeUpdate = linkExternoRepository.findAll().size();

        // Create the LinkExterno

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLinkExternoMockMvc.perform(put("/api/link-externos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkExterno)))
            .andExpect(status().isCreated());

        // Validate the LinkExterno in the database
        List<LinkExterno> linkExternoList = linkExternoRepository.findAll();
        assertThat(linkExternoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLinkExterno() throws Exception {
        // Initialize the database
        linkExternoService.save(linkExterno);

        int databaseSizeBeforeDelete = linkExternoRepository.findAll().size();

        // Get the linkExterno
        restLinkExternoMockMvc.perform(delete("/api/link-externos/{id}", linkExterno.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LinkExterno> linkExternoList = linkExternoRepository.findAll();
        assertThat(linkExternoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkExterno.class);
        LinkExterno linkExterno1 = new LinkExterno();
        linkExterno1.setId(1L);
        LinkExterno linkExterno2 = new LinkExterno();
        linkExterno2.setId(linkExterno1.getId());
        assertThat(linkExterno1).isEqualTo(linkExterno2);
        linkExterno2.setId(2L);
        assertThat(linkExterno1).isNotEqualTo(linkExterno2);
        linkExterno1.setId(null);
        assertThat(linkExterno1).isNotEqualTo(linkExterno2);
    }
}
