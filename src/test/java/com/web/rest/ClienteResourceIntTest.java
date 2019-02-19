package com.web.rest;

import com.HmProjetosApp;

import com.domain.Cliente;
import com.domain.Telefone;
import com.domain.Servico;
import com.repository.ClienteRepository;
import com.service.ClienteService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.ClienteCriteria;
import com.service.ClienteQueryService;

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
 * Test class for the ClienteResource REST controller.
 *
 * @see ClienteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class ClienteResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AA";
    private static final String UPDATED_ESTADO = "BB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_INDICACAO = "AAAAAAAAAA";
    private static final String UPDATED_INDICACAO = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO = "BBBBBBBBBB";

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteQueryService clienteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClienteResource clienteResource = new ClienteResource(clienteService, clienteQueryService);
        this.restClienteMockMvc = MockMvcBuilders.standaloneSetup(clienteResource)
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
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .nome(DEFAULT_NOME)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .cep(DEFAULT_CEP)
            .email(DEFAULT_EMAIL)
            .indicacao(DEFAULT_INDICACAO)
            .documento(DEFAULT_DOCUMENTO);
        return cliente;
    }

    @Before
    public void initTest() {
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    public void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente
        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cliente)))
            .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCliente.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testCliente.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testCliente.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testCliente.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCliente.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testCliente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCliente.getIndicacao()).isEqualTo(DEFAULT_INDICACAO);
        assertThat(testCliente.getDocumento()).isEqualTo(DEFAULT_DOCUMENTO);
    }

    @Test
    @Transactional
    public void createClienteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente with an existing ID
        cliente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cliente)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setNome(null);

        // Create the Cliente, which fails.

        restClienteMockMvc.perform(post("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cliente)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].indicacao").value(hasItem(DEFAULT_INDICACAO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO.toString())));
    }

    @Test
    @Transactional
    public void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.indicacao").value(DEFAULT_INDICACAO.toString()))
            .andExpect(jsonPath("$.documento").value(DEFAULT_DOCUMENTO.toString()));
    }

    @Test
    @Transactional
    public void getAllClientesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome equals to DEFAULT_NOME
        defaultClienteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the clienteList where nome equals to UPDATED_NOME
        defaultClienteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllClientesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultClienteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the clienteList where nome equals to UPDATED_NOME
        defaultClienteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllClientesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome is not null
        defaultClienteShouldBeFound("nome.specified=true");

        // Get all the clienteList where nome is null
        defaultClienteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where endereco equals to DEFAULT_ENDERECO
        defaultClienteShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the clienteList where endereco equals to UPDATED_ENDERECO
        defaultClienteShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    public void getAllClientesByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultClienteShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the clienteList where endereco equals to UPDATED_ENDERECO
        defaultClienteShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    public void getAllClientesByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where endereco is not null
        defaultClienteShouldBeFound("endereco.specified=true");

        // Get all the clienteList where endereco is null
        defaultClienteShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where bairro equals to DEFAULT_BAIRRO
        defaultClienteShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the clienteList where bairro equals to UPDATED_BAIRRO
        defaultClienteShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllClientesByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultClienteShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the clienteList where bairro equals to UPDATED_BAIRRO
        defaultClienteShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllClientesByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where bairro is not null
        defaultClienteShouldBeFound("bairro.specified=true");

        // Get all the clienteList where bairro is null
        defaultClienteShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cidade equals to DEFAULT_CIDADE
        defaultClienteShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the clienteList where cidade equals to UPDATED_CIDADE
        defaultClienteShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllClientesByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultClienteShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the clienteList where cidade equals to UPDATED_CIDADE
        defaultClienteShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllClientesByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cidade is not null
        defaultClienteShouldBeFound("cidade.specified=true");

        // Get all the clienteList where cidade is null
        defaultClienteShouldNotBeFound("cidade.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estado equals to DEFAULT_ESTADO
        defaultClienteShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the clienteList where estado equals to UPDATED_ESTADO
        defaultClienteShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllClientesByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultClienteShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the clienteList where estado equals to UPDATED_ESTADO
        defaultClienteShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllClientesByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where estado is not null
        defaultClienteShouldBeFound("estado.specified=true");

        // Get all the clienteList where estado is null
        defaultClienteShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cep equals to DEFAULT_CEP
        defaultClienteShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the clienteList where cep equals to UPDATED_CEP
        defaultClienteShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllClientesByCepIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultClienteShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the clienteList where cep equals to UPDATED_CEP
        defaultClienteShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllClientesByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cep is not null
        defaultClienteShouldBeFound("cep.specified=true");

        // Get all the clienteList where cep is null
        defaultClienteShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email equals to DEFAULT_EMAIL
        defaultClienteShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the clienteList where email equals to UPDATED_EMAIL
        defaultClienteShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClienteShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the clienteList where email equals to UPDATED_EMAIL
        defaultClienteShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where email is not null
        defaultClienteShouldBeFound("email.specified=true");

        // Get all the clienteList where email is null
        defaultClienteShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByIndicacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where indicacao equals to DEFAULT_INDICACAO
        defaultClienteShouldBeFound("indicacao.equals=" + DEFAULT_INDICACAO);

        // Get all the clienteList where indicacao equals to UPDATED_INDICACAO
        defaultClienteShouldNotBeFound("indicacao.equals=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    public void getAllClientesByIndicacaoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where indicacao in DEFAULT_INDICACAO or UPDATED_INDICACAO
        defaultClienteShouldBeFound("indicacao.in=" + DEFAULT_INDICACAO + "," + UPDATED_INDICACAO);

        // Get all the clienteList where indicacao equals to UPDATED_INDICACAO
        defaultClienteShouldNotBeFound("indicacao.in=" + UPDATED_INDICACAO);
    }

    @Test
    @Transactional
    public void getAllClientesByIndicacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where indicacao is not null
        defaultClienteShouldBeFound("indicacao.specified=true");

        // Get all the clienteList where indicacao is null
        defaultClienteShouldNotBeFound("indicacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where documento equals to DEFAULT_DOCUMENTO
        defaultClienteShouldBeFound("documento.equals=" + DEFAULT_DOCUMENTO);

        // Get all the clienteList where documento equals to UPDATED_DOCUMENTO
        defaultClienteShouldNotBeFound("documento.equals=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllClientesByDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where documento in DEFAULT_DOCUMENTO or UPDATED_DOCUMENTO
        defaultClienteShouldBeFound("documento.in=" + DEFAULT_DOCUMENTO + "," + UPDATED_DOCUMENTO);

        // Get all the clienteList where documento equals to UPDATED_DOCUMENTO
        defaultClienteShouldNotBeFound("documento.in=" + UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllClientesByDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where documento is not null
        defaultClienteShouldBeFound("documento.specified=true");

        // Get all the clienteList where documento is null
        defaultClienteShouldNotBeFound("documento.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientesByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        Telefone telefone = TelefoneResourceIntTest.createEntity(em);
        em.persist(telefone);
        em.flush();
        cliente.addTelefone(telefone);
        clienteRepository.saveAndFlush(cliente);
        Long telefoneId = telefone.getId();

        // Get all the clienteList where telefone equals to telefoneId
        defaultClienteShouldBeFound("telefoneId.equals=" + telefoneId);

        // Get all the clienteList where telefone equals to telefoneId + 1
        defaultClienteShouldNotBeFound("telefoneId.equals=" + (telefoneId + 1));
    }


    @Test
    @Transactional
    public void getAllClientesByServicoIsEqualToSomething() throws Exception {
        // Initialize the database
        Servico servico = ServicoResourceIntTest.createEntity(em);
        em.persist(servico);
        em.flush();
        cliente.addServico(servico);
        clienteRepository.saveAndFlush(cliente);
        Long servicoId = servico.getId();

        // Get all the clienteList where servico equals to servicoId
        defaultClienteShouldBeFound("servicoId.equals=" + servicoId);

        // Get all the clienteList where servico equals to servicoId + 1
        defaultClienteShouldNotBeFound("servicoId.equals=" + (servicoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].indicacao").value(hasItem(DEFAULT_INDICACAO.toString())))
            .andExpect(jsonPath("$.[*].documento").value(hasItem(DEFAULT_DOCUMENTO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCliente() throws Exception {
        // Initialize the database
        clienteService.save(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findOne(cliente.getId());
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .nome(UPDATED_NOME)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .email(UPDATED_EMAIL)
            .indicacao(UPDATED_INDICACAO)
            .documento(UPDATED_DOCUMENTO);

        restClienteMockMvc.perform(put("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCliente)))
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCliente.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testCliente.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testCliente.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testCliente.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCliente.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testCliente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCliente.getIndicacao()).isEqualTo(UPDATED_INDICACAO);
        assertThat(testCliente.getDocumento()).isEqualTo(UPDATED_DOCUMENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Create the Cliente

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClienteMockMvc.perform(put("/api/clientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cliente)))
            .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCliente() throws Exception {
        // Initialize the database
        clienteService.save(cliente);

        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Get the cliente
        restClienteMockMvc.perform(delete("/api/clientes/{id}", cliente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        Cliente cliente2 = new Cliente();
        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);
        cliente2.setId(2L);
        assertThat(cliente1).isNotEqualTo(cliente2);
        cliente1.setId(null);
        assertThat(cliente1).isNotEqualTo(cliente2);
    }
}
