package com.web.rest;

import com.HmProjetosApp;

import com.domain.Telefone;
import com.domain.Cliente;
import com.repository.TelefoneRepository;
import com.service.TelefoneService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.TelefoneCriteria;
import com.service.TelefoneQueryService;

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
 * Test class for the TelefoneResource REST controller.
 *
 * @see TelefoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class TelefoneResourceIntTest {

    private static final String DEFAULT_CONTATO = "AAAAAAAAAA";
    private static final String UPDATED_CONTATO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private TelefoneQueryService telefoneQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTelefoneMockMvc;

    private Telefone telefone;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TelefoneResource telefoneResource = new TelefoneResource(telefoneService, telefoneQueryService);
        this.restTelefoneMockMvc = MockMvcBuilders.standaloneSetup(telefoneResource)
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
    public static Telefone createEntity(EntityManager em) {
        Telefone telefone = new Telefone()
            .contato(DEFAULT_CONTATO)
            .numero(DEFAULT_NUMERO);
        return telefone;
    }

    @Before
    public void initTest() {
        telefone = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelefone() throws Exception {
        int databaseSizeBeforeCreate = telefoneRepository.findAll().size();

        // Create the Telefone
        restTelefoneMockMvc.perform(post("/api/telefones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefone)))
            .andExpect(status().isCreated());

        // Validate the Telefone in the database
        List<Telefone> telefoneList = telefoneRepository.findAll();
        assertThat(telefoneList).hasSize(databaseSizeBeforeCreate + 1);
        Telefone testTelefone = telefoneList.get(telefoneList.size() - 1);
        assertThat(testTelefone.getContato()).isEqualTo(DEFAULT_CONTATO);
        assertThat(testTelefone.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createTelefoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telefoneRepository.findAll().size();

        // Create the Telefone with an existing ID
        telefone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelefoneMockMvc.perform(post("/api/telefones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefone)))
            .andExpect(status().isBadRequest());

        // Validate the Telefone in the database
        List<Telefone> telefoneList = telefoneRepository.findAll();
        assertThat(telefoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneRepository.findAll().size();
        // set the field null
        telefone.setNumero(null);

        // Create the Telefone, which fails.

        restTelefoneMockMvc.perform(post("/api/telefones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefone)))
            .andExpect(status().isBadRequest());

        List<Telefone> telefoneList = telefoneRepository.findAll();
        assertThat(telefoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefones() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList
        restTelefoneMockMvc.perform(get("/api/telefones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefone.getId().intValue())))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())));
    }

    @Test
    @Transactional
    public void getTelefone() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get the telefone
        restTelefoneMockMvc.perform(get("/api/telefones/{id}", telefone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(telefone.getId().intValue()))
            .andExpect(jsonPath("$.contato").value(DEFAULT_CONTATO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()));
    }

    @Test
    @Transactional
    public void getAllTelefonesByContatoIsEqualToSomething() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList where contato equals to DEFAULT_CONTATO
        defaultTelefoneShouldBeFound("contato.equals=" + DEFAULT_CONTATO);

        // Get all the telefoneList where contato equals to UPDATED_CONTATO
        defaultTelefoneShouldNotBeFound("contato.equals=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    public void getAllTelefonesByContatoIsInShouldWork() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList where contato in DEFAULT_CONTATO or UPDATED_CONTATO
        defaultTelefoneShouldBeFound("contato.in=" + DEFAULT_CONTATO + "," + UPDATED_CONTATO);

        // Get all the telefoneList where contato equals to UPDATED_CONTATO
        defaultTelefoneShouldNotBeFound("contato.in=" + UPDATED_CONTATO);
    }

    @Test
    @Transactional
    public void getAllTelefonesByContatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList where contato is not null
        defaultTelefoneShouldBeFound("contato.specified=true");

        // Get all the telefoneList where contato is null
        defaultTelefoneShouldNotBeFound("contato.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelefonesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList where numero equals to DEFAULT_NUMERO
        defaultTelefoneShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the telefoneList where numero equals to UPDATED_NUMERO
        defaultTelefoneShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllTelefonesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultTelefoneShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the telefoneList where numero equals to UPDATED_NUMERO
        defaultTelefoneShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllTelefonesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefoneList where numero is not null
        defaultTelefoneShouldBeFound("numero.specified=true");

        // Get all the telefoneList where numero is null
        defaultTelefoneShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllTelefonesByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        telefone.setCliente(cliente);
        telefoneRepository.saveAndFlush(telefone);
        Long clienteId = cliente.getId();

        // Get all the telefoneList where cliente equals to clienteId
        defaultTelefoneShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the telefoneList where cliente equals to clienteId + 1
        defaultTelefoneShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTelefoneShouldBeFound(String filter) throws Exception {
        restTelefoneMockMvc.perform(get("/api/telefones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefone.getId().intValue())))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTelefoneShouldNotBeFound(String filter) throws Exception {
        restTelefoneMockMvc.perform(get("/api/telefones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTelefone() throws Exception {
        // Get the telefone
        restTelefoneMockMvc.perform(get("/api/telefones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefone() throws Exception {
        // Initialize the database
        telefoneService.save(telefone);

        int databaseSizeBeforeUpdate = telefoneRepository.findAll().size();

        // Update the telefone
        Telefone updatedTelefone = telefoneRepository.findOne(telefone.getId());
        // Disconnect from session so that the updates on updatedTelefone are not directly saved in db
        em.detach(updatedTelefone);
        updatedTelefone
            .contato(UPDATED_CONTATO)
            .numero(UPDATED_NUMERO);

        restTelefoneMockMvc.perform(put("/api/telefones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTelefone)))
            .andExpect(status().isOk());

        // Validate the Telefone in the database
        List<Telefone> telefoneList = telefoneRepository.findAll();
        assertThat(telefoneList).hasSize(databaseSizeBeforeUpdate);
        Telefone testTelefone = telefoneList.get(telefoneList.size() - 1);
        assertThat(testTelefone.getContato()).isEqualTo(UPDATED_CONTATO);
        assertThat(testTelefone.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void updateNonExistingTelefone() throws Exception {
        int databaseSizeBeforeUpdate = telefoneRepository.findAll().size();

        // Create the Telefone

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTelefoneMockMvc.perform(put("/api/telefones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefone)))
            .andExpect(status().isCreated());

        // Validate the Telefone in the database
        List<Telefone> telefoneList = telefoneRepository.findAll();
        assertThat(telefoneList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTelefone() throws Exception {
        // Initialize the database
        telefoneService.save(telefone);

        int databaseSizeBeforeDelete = telefoneRepository.findAll().size();

        // Get the telefone
        restTelefoneMockMvc.perform(delete("/api/telefones/{id}", telefone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Telefone> telefoneList = telefoneRepository.findAll();
        assertThat(telefoneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Telefone.class);
        Telefone telefone1 = new Telefone();
        telefone1.setId(1L);
        Telefone telefone2 = new Telefone();
        telefone2.setId(telefone1.getId());
        assertThat(telefone1).isEqualTo(telefone2);
        telefone2.setId(2L);
        assertThat(telefone1).isNotEqualTo(telefone2);
        telefone1.setId(null);
        assertThat(telefone1).isNotEqualTo(telefone2);
    }
}
