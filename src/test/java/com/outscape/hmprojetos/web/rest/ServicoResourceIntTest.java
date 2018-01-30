package com.outscape.hmprojetos.web.rest;

import com.outscape.hmprojetos.HmProjetosApp;

import com.outscape.hmprojetos.domain.Servico;
import com.outscape.hmprojetos.repository.ServicoRepository;
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

import com.outscape.hmprojetos.domain.enumeration.TipoServico;
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

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

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

    private static final String DEFAULT_IPTU = "AAAAAAAAAA";
    private static final String UPDATED_IPTU = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Autowired
    private ServicoRepository servicoRepository;

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
        final ServicoResource servicoResource = new ServicoResource(servicoRepository);
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
            .descricao(DEFAULT_DESCRICAO)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .cep(DEFAULT_CEP)
            .inicio(DEFAULT_INICIO)
            .fim(DEFAULT_FIM)
            .iptu(DEFAULT_IPTU)
            .link(DEFAULT_LINK);
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
        assertThat(testServico.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testServico.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testServico.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testServico.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testServico.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testServico.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testServico.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testServico.getFim()).isEqualTo(DEFAULT_FIM);
        assertThat(testServico.getIptu()).isEqualTo(DEFAULT_IPTU);
        assertThat(testServico.getLink()).isEqualTo(DEFAULT_LINK);
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
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())))
            .andExpect(jsonPath("$.[*].iptu").value(hasItem(DEFAULT_IPTU.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
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
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.fim").value(DEFAULT_FIM.toString()))
            .andExpect(jsonPath("$.iptu").value(DEFAULT_IPTU.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
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
        servicoRepository.saveAndFlush(servico);
        int databaseSizeBeforeUpdate = servicoRepository.findAll().size();

        // Update the servico
        Servico updatedServico = servicoRepository.findOne(servico.getId());
        // Disconnect from session so that the updates on updatedServico are not directly saved in db
        em.detach(updatedServico);
        updatedServico
            .tipo(UPDATED_TIPO)
            .codigo(UPDATED_CODIGO)
            .descricao(UPDATED_DESCRICAO)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .iptu(UPDATED_IPTU)
            .link(UPDATED_LINK);

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
        assertThat(testServico.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testServico.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testServico.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testServico.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testServico.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testServico.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testServico.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testServico.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testServico.getIptu()).isEqualTo(UPDATED_IPTU);
        assertThat(testServico.getLink()).isEqualTo(UPDATED_LINK);
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
        servicoRepository.saveAndFlush(servico);
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
