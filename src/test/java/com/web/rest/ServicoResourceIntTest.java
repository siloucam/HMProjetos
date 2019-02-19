package com.web.rest;

import com.HmProjetosApp;

import com.domain.Servico;
import com.domain.CodigoPrefeitura;
import com.domain.LinkExterno;
import com.domain.Situacao;
import com.domain.Transacao;
import com.domain.DescricaoServico;
import com.domain.Cliente;
import com.repository.ServicoRepository;
import com.service.ServicoService;
import com.web.rest.errors.ExceptionTranslator;
import com.service.dto.ServicoCriteria;
import com.service.ServicoQueryService;

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

import static com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.domain.enumeration.TipoServico;
/**
 * Test class for the ServicoResource REST controller.
 *
 * @see ServicoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HmProjetosApp.class)
public class ServicoResourceIntTest {

    private static final TipoServico DEFAULT_TIPO = TipoServico.PAQ;
    private static final TipoServico UPDATED_TIPO = TipoServico.ASS;

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Float DEFAULT_VALOR = 1F;
    private static final Float UPDATED_VALOR = 2F;

    private static final String DEFAULT_FORMA = "AAAAAAAAAA";
    private static final String UPDATED_FORMA = "BBBBBBBBBB";

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

    private static final LocalDate DEFAULT_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FIM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIM = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private ServicoQueryService servicoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServicoMockMvc;

    private Servico servico;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServicoResource servicoResource = new ServicoResource(servicoService, servicoQueryService);
        this.restServicoMockMvc = MockMvcBuilders.standaloneSetup(servicoResource)
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
    public static Servico createEntity(EntityManager em) {
        Servico servico = new Servico()
            .tipo(DEFAULT_TIPO)
            .codigo(DEFAULT_CODIGO)
            .observacao(DEFAULT_OBSERVACAO)
            .valor(DEFAULT_VALOR)
            .forma(DEFAULT_FORMA)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .cep(DEFAULT_CEP)
            .inicio(DEFAULT_INICIO)
            .fim(DEFAULT_FIM);
        return servico;
    }

    @Before
    public void initTest() {
        servico = createEntity(em);
    }

    @Test
    @Transactional
    public void createServico() throws Exception {
        int databaseSizeBeforeCreate = servicoRepository.findAll().size();

        // Create the Servico
        restServicoMockMvc.perform(post("/api/servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servico)))
            .andExpect(status().isCreated());

        // Validate the Servico in the database
        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeCreate + 1);
        Servico testServico = servicoList.get(servicoList.size() - 1);
        assertThat(testServico.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testServico.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testServico.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testServico.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testServico.getForma()).isEqualTo(DEFAULT_FORMA);
        assertThat(testServico.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testServico.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testServico.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testServico.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testServico.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testServico.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testServico.getFim()).isEqualTo(DEFAULT_FIM);
    }

    @Test
    @Transactional
    public void createServicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = servicoRepository.findAll().size();

        // Create the Servico with an existing ID
        servico.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicoMockMvc.perform(post("/api/servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servico)))
            .andExpect(status().isBadRequest());

        // Validate the Servico in the database
        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicoRepository.findAll().size();
        // set the field null
        servico.setTipo(null);

        // Create the Servico, which fails.

        restServicoMockMvc.perform(post("/api/servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servico)))
            .andExpect(status().isBadRequest());

        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicoRepository.findAll().size();
        // set the field null
        servico.setCodigo(null);

        // Create the Servico, which fails.

        restServicoMockMvc.perform(post("/api/servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servico)))
            .andExpect(status().isBadRequest());

        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServicos() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList
        restServicoMockMvc.perform(get("/api/servicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servico.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].forma").value(hasItem(DEFAULT_FORMA.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())));
    }

    @Test
    @Transactional
    public void getServico() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get the servico
        restServicoMockMvc.perform(get("/api/servicos/{id}", servico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(servico.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.forma").value(DEFAULT_FORMA.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.fim").value(DEFAULT_FIM.toString()));
    }

    @Test
    @Transactional
    public void getAllServicosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where tipo equals to DEFAULT_TIPO
        defaultServicoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the servicoList where tipo equals to UPDATED_TIPO
        defaultServicoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllServicosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultServicoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the servicoList where tipo equals to UPDATED_TIPO
        defaultServicoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void getAllServicosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where tipo is not null
        defaultServicoShouldBeFound("tipo.specified=true");

        // Get all the servicoList where tipo is null
        defaultServicoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where codigo equals to DEFAULT_CODIGO
        defaultServicoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the servicoList where codigo equals to UPDATED_CODIGO
        defaultServicoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllServicosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultServicoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the servicoList where codigo equals to UPDATED_CODIGO
        defaultServicoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllServicosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where codigo is not null
        defaultServicoShouldBeFound("codigo.specified=true");

        // Get all the servicoList where codigo is null
        defaultServicoShouldNotBeFound("codigo.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByObservacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where observacao equals to DEFAULT_OBSERVACAO
        defaultServicoShouldBeFound("observacao.equals=" + DEFAULT_OBSERVACAO);

        // Get all the servicoList where observacao equals to UPDATED_OBSERVACAO
        defaultServicoShouldNotBeFound("observacao.equals=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllServicosByObservacaoIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where observacao in DEFAULT_OBSERVACAO or UPDATED_OBSERVACAO
        defaultServicoShouldBeFound("observacao.in=" + DEFAULT_OBSERVACAO + "," + UPDATED_OBSERVACAO);

        // Get all the servicoList where observacao equals to UPDATED_OBSERVACAO
        defaultServicoShouldNotBeFound("observacao.in=" + UPDATED_OBSERVACAO);
    }

    @Test
    @Transactional
    public void getAllServicosByObservacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where observacao is not null
        defaultServicoShouldBeFound("observacao.specified=true");

        // Get all the servicoList where observacao is null
        defaultServicoShouldNotBeFound("observacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where valor equals to DEFAULT_VALOR
        defaultServicoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the servicoList where valor equals to UPDATED_VALOR
        defaultServicoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllServicosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultServicoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the servicoList where valor equals to UPDATED_VALOR
        defaultServicoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllServicosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where valor is not null
        defaultServicoShouldBeFound("valor.specified=true");

        // Get all the servicoList where valor is null
        defaultServicoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByFormaIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where forma equals to DEFAULT_FORMA
        defaultServicoShouldBeFound("forma.equals=" + DEFAULT_FORMA);

        // Get all the servicoList where forma equals to UPDATED_FORMA
        defaultServicoShouldNotBeFound("forma.equals=" + UPDATED_FORMA);
    }

    @Test
    @Transactional
    public void getAllServicosByFormaIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where forma in DEFAULT_FORMA or UPDATED_FORMA
        defaultServicoShouldBeFound("forma.in=" + DEFAULT_FORMA + "," + UPDATED_FORMA);

        // Get all the servicoList where forma equals to UPDATED_FORMA
        defaultServicoShouldNotBeFound("forma.in=" + UPDATED_FORMA);
    }

    @Test
    @Transactional
    public void getAllServicosByFormaIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where forma is not null
        defaultServicoShouldBeFound("forma.specified=true");

        // Get all the servicoList where forma is null
        defaultServicoShouldNotBeFound("forma.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where endereco equals to DEFAULT_ENDERECO
        defaultServicoShouldBeFound("endereco.equals=" + DEFAULT_ENDERECO);

        // Get all the servicoList where endereco equals to UPDATED_ENDERECO
        defaultServicoShouldNotBeFound("endereco.equals=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    public void getAllServicosByEnderecoIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where endereco in DEFAULT_ENDERECO or UPDATED_ENDERECO
        defaultServicoShouldBeFound("endereco.in=" + DEFAULT_ENDERECO + "," + UPDATED_ENDERECO);

        // Get all the servicoList where endereco equals to UPDATED_ENDERECO
        defaultServicoShouldNotBeFound("endereco.in=" + UPDATED_ENDERECO);
    }

    @Test
    @Transactional
    public void getAllServicosByEnderecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where endereco is not null
        defaultServicoShouldBeFound("endereco.specified=true");

        // Get all the servicoList where endereco is null
        defaultServicoShouldNotBeFound("endereco.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where bairro equals to DEFAULT_BAIRRO
        defaultServicoShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the servicoList where bairro equals to UPDATED_BAIRRO
        defaultServicoShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllServicosByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultServicoShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the servicoList where bairro equals to UPDATED_BAIRRO
        defaultServicoShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    public void getAllServicosByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where bairro is not null
        defaultServicoShouldBeFound("bairro.specified=true");

        // Get all the servicoList where bairro is null
        defaultServicoShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where cidade equals to DEFAULT_CIDADE
        defaultServicoShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the servicoList where cidade equals to UPDATED_CIDADE
        defaultServicoShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllServicosByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultServicoShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the servicoList where cidade equals to UPDATED_CIDADE
        defaultServicoShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void getAllServicosByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where cidade is not null
        defaultServicoShouldBeFound("cidade.specified=true");

        // Get all the servicoList where cidade is null
        defaultServicoShouldNotBeFound("cidade.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where estado equals to DEFAULT_ESTADO
        defaultServicoShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the servicoList where estado equals to UPDATED_ESTADO
        defaultServicoShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllServicosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultServicoShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the servicoList where estado equals to UPDATED_ESTADO
        defaultServicoShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllServicosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where estado is not null
        defaultServicoShouldBeFound("estado.specified=true");

        // Get all the servicoList where estado is null
        defaultServicoShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where cep equals to DEFAULT_CEP
        defaultServicoShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the servicoList where cep equals to UPDATED_CEP
        defaultServicoShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllServicosByCepIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultServicoShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the servicoList where cep equals to UPDATED_CEP
        defaultServicoShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    public void getAllServicosByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where cep is not null
        defaultServicoShouldBeFound("cep.specified=true");

        // Get all the servicoList where cep is null
        defaultServicoShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where inicio equals to DEFAULT_INICIO
        defaultServicoShouldBeFound("inicio.equals=" + DEFAULT_INICIO);

        // Get all the servicoList where inicio equals to UPDATED_INICIO
        defaultServicoShouldNotBeFound("inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllServicosByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where inicio in DEFAULT_INICIO or UPDATED_INICIO
        defaultServicoShouldBeFound("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO);

        // Get all the servicoList where inicio equals to UPDATED_INICIO
        defaultServicoShouldNotBeFound("inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllServicosByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where inicio is not null
        defaultServicoShouldBeFound("inicio.specified=true");

        // Get all the servicoList where inicio is null
        defaultServicoShouldNotBeFound("inicio.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where inicio greater than or equals to DEFAULT_INICIO
        defaultServicoShouldBeFound("inicio.greaterOrEqualThan=" + DEFAULT_INICIO);

        // Get all the servicoList where inicio greater than or equals to UPDATED_INICIO
        defaultServicoShouldNotBeFound("inicio.greaterOrEqualThan=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllServicosByInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where inicio less than or equals to DEFAULT_INICIO
        defaultServicoShouldNotBeFound("inicio.lessThan=" + DEFAULT_INICIO);

        // Get all the servicoList where inicio less than or equals to UPDATED_INICIO
        defaultServicoShouldBeFound("inicio.lessThan=" + UPDATED_INICIO);
    }


    @Test
    @Transactional
    public void getAllServicosByFimIsEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where fim equals to DEFAULT_FIM
        defaultServicoShouldBeFound("fim.equals=" + DEFAULT_FIM);

        // Get all the servicoList where fim equals to UPDATED_FIM
        defaultServicoShouldNotBeFound("fim.equals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllServicosByFimIsInShouldWork() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where fim in DEFAULT_FIM or UPDATED_FIM
        defaultServicoShouldBeFound("fim.in=" + DEFAULT_FIM + "," + UPDATED_FIM);

        // Get all the servicoList where fim equals to UPDATED_FIM
        defaultServicoShouldNotBeFound("fim.in=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllServicosByFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where fim is not null
        defaultServicoShouldBeFound("fim.specified=true");

        // Get all the servicoList where fim is null
        defaultServicoShouldNotBeFound("fim.specified=false");
    }

    @Test
    @Transactional
    public void getAllServicosByFimIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where fim greater than or equals to DEFAULT_FIM
        defaultServicoShouldBeFound("fim.greaterOrEqualThan=" + DEFAULT_FIM);

        // Get all the servicoList where fim greater than or equals to UPDATED_FIM
        defaultServicoShouldNotBeFound("fim.greaterOrEqualThan=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllServicosByFimIsLessThanSomething() throws Exception {
        // Initialize the database
        servicoRepository.saveAndFlush(servico);

        // Get all the servicoList where fim less than or equals to DEFAULT_FIM
        defaultServicoShouldNotBeFound("fim.lessThan=" + DEFAULT_FIM);

        // Get all the servicoList where fim less than or equals to UPDATED_FIM
        defaultServicoShouldBeFound("fim.lessThan=" + UPDATED_FIM);
    }


    @Test
    @Transactional
    public void getAllServicosByCodprefeituraIsEqualToSomething() throws Exception {
        // Initialize the database
        CodigoPrefeitura codprefeitura = CodigoPrefeituraResourceIntTest.createEntity(em);
        em.persist(codprefeitura);
        em.flush();
        servico.addCodprefeitura(codprefeitura);
        servicoRepository.saveAndFlush(servico);
        Long codprefeituraId = codprefeitura.getId();

        // Get all the servicoList where codprefeitura equals to codprefeituraId
        defaultServicoShouldBeFound("codprefeituraId.equals=" + codprefeituraId);

        // Get all the servicoList where codprefeitura equals to codprefeituraId + 1
        defaultServicoShouldNotBeFound("codprefeituraId.equals=" + (codprefeituraId + 1));
    }


    @Test
    @Transactional
    public void getAllServicosByLinkexternoIsEqualToSomething() throws Exception {
        // Initialize the database
        LinkExterno linkexterno = LinkExternoResourceIntTest.createEntity(em);
        em.persist(linkexterno);
        em.flush();
        servico.addLinkexterno(linkexterno);
        servicoRepository.saveAndFlush(servico);
        Long linkexternoId = linkexterno.getId();

        // Get all the servicoList where linkexterno equals to linkexternoId
        defaultServicoShouldBeFound("linkexternoId.equals=" + linkexternoId);

        // Get all the servicoList where linkexterno equals to linkexternoId + 1
        defaultServicoShouldNotBeFound("linkexternoId.equals=" + (linkexternoId + 1));
    }


    @Test
    @Transactional
    public void getAllServicosBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        Situacao situacao = SituacaoResourceIntTest.createEntity(em);
        em.persist(situacao);
        em.flush();
        servico.addSituacao(situacao);
        servicoRepository.saveAndFlush(servico);
        Long situacaoId = situacao.getId();

        // Get all the servicoList where situacao equals to situacaoId
        defaultServicoShouldBeFound("situacaoId.equals=" + situacaoId);

        // Get all the servicoList where situacao equals to situacaoId + 1
        defaultServicoShouldNotBeFound("situacaoId.equals=" + (situacaoId + 1));
    }


    @Test
    @Transactional
    public void getAllServicosByTransacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        Transacao transacao = TransacaoResourceIntTest.createEntity(em);
        em.persist(transacao);
        em.flush();
        servico.addTransacao(transacao);
        servicoRepository.saveAndFlush(servico);
        Long transacaoId = transacao.getId();

        // Get all the servicoList where transacao equals to transacaoId
        defaultServicoShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the servicoList where transacao equals to transacaoId + 1
        defaultServicoShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }


    @Test
    @Transactional
    public void getAllServicosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        DescricaoServico descricao = DescricaoServicoResourceIntTest.createEntity(em);
        em.persist(descricao);
        em.flush();
        servico.setDescricao(descricao);
        servicoRepository.saveAndFlush(servico);
        Long descricaoId = descricao.getId();

        // Get all the servicoList where descricao equals to descricaoId
        defaultServicoShouldBeFound("descricaoId.equals=" + descricaoId);

        // Get all the servicoList where descricao equals to descricaoId + 1
        defaultServicoShouldNotBeFound("descricaoId.equals=" + (descricaoId + 1));
    }


    @Test
    @Transactional
    public void getAllServicosByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        servico.setCliente(cliente);
        servicoRepository.saveAndFlush(servico);
        Long clienteId = cliente.getId();

        // Get all the servicoList where cliente equals to clienteId
        defaultServicoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the servicoList where cliente equals to clienteId + 1
        defaultServicoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultServicoShouldBeFound(String filter) throws Exception {
        restServicoMockMvc.perform(get("/api/servicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servico.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].forma").value(hasItem(DEFAULT_FORMA.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultServicoShouldNotBeFound(String filter) throws Exception {
        restServicoMockMvc.perform(get("/api/servicos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingServico() throws Exception {
        // Get the servico
        restServicoMockMvc.perform(get("/api/servicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServico() throws Exception {
        // Initialize the database
        servicoService.save(servico);

        int databaseSizeBeforeUpdate = servicoRepository.findAll().size();

        // Update the servico
        Servico updatedServico = servicoRepository.findOne(servico.getId());
        // Disconnect from session so that the updates on updatedServico are not directly saved in db
        em.detach(updatedServico);
        updatedServico
            .tipo(UPDATED_TIPO)
            .codigo(UPDATED_CODIGO)
            .observacao(UPDATED_OBSERVACAO)
            .valor(UPDATED_VALOR)
            .forma(UPDATED_FORMA)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM);

        restServicoMockMvc.perform(put("/api/servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServico)))
            .andExpect(status().isOk());

        // Validate the Servico in the database
        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeUpdate);
        Servico testServico = servicoList.get(servicoList.size() - 1);
        assertThat(testServico.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testServico.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testServico.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testServico.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testServico.getForma()).isEqualTo(UPDATED_FORMA);
        assertThat(testServico.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testServico.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testServico.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testServico.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testServico.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testServico.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testServico.getFim()).isEqualTo(UPDATED_FIM);
    }

    @Test
    @Transactional
    public void updateNonExistingServico() throws Exception {
        int databaseSizeBeforeUpdate = servicoRepository.findAll().size();

        // Create the Servico

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServicoMockMvc.perform(put("/api/servicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servico)))
            .andExpect(status().isCreated());

        // Validate the Servico in the database
        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServico() throws Exception {
        // Initialize the database
        servicoService.save(servico);

        int databaseSizeBeforeDelete = servicoRepository.findAll().size();

        // Get the servico
        restServicoMockMvc.perform(delete("/api/servicos/{id}", servico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Servico> servicoList = servicoRepository.findAll();
        assertThat(servicoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servico.class);
        Servico servico1 = new Servico();
        servico1.setId(1L);
        Servico servico2 = new Servico();
        servico2.setId(servico1.getId());
        assertThat(servico1).isEqualTo(servico2);
        servico2.setId(2L);
        assertThat(servico1).isNotEqualTo(servico2);
        servico1.setId(null);
        assertThat(servico1).isNotEqualTo(servico2);
    }
}
