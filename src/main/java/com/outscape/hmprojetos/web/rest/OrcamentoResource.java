package com.outscape.hmprojetos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outscape.hmprojetos.domain.Orcamento;

import com.outscape.hmprojetos.repository.OrcamentoRepository;
import com.outscape.hmprojetos.web.rest.errors.BadRequestAlertException;
import com.outscape.hmprojetos.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Orcamento.
 */
@RestController
@RequestMapping("/api")
public class OrcamentoResource {

    private final Logger log = LoggerFactory.getLogger(OrcamentoResource.class);

    private static final String ENTITY_NAME = "orcamento";

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoResource(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    /**
     * POST  /orcamentos : Create a new orcamento.
     *
     * @param orcamento the orcamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orcamento, or with status 400 (Bad Request) if the orcamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orcamentos")
    @Timed
    public ResponseEntity<Orcamento> createOrcamento(@Valid @RequestBody Orcamento orcamento) throws URISyntaxException {
        log.debug("REST request to save Orcamento : {}", orcamento);
        if (orcamento.getId() != null) {
            throw new BadRequestAlertException("A new orcamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Orcamento result = orcamentoRepository.save(orcamento);
        return ResponseEntity.created(new URI("/api/orcamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orcamentos : Updates an existing orcamento.
     *
     * @param orcamento the orcamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orcamento,
     * or with status 400 (Bad Request) if the orcamento is not valid,
     * or with status 500 (Internal Server Error) if the orcamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orcamentos")
    @Timed
    public ResponseEntity<Orcamento> updateOrcamento(@Valid @RequestBody Orcamento orcamento) throws URISyntaxException {
        log.debug("REST request to update Orcamento : {}", orcamento);
        if (orcamento.getId() == null) {
            return createOrcamento(orcamento);
        }
        Orcamento result = orcamentoRepository.save(orcamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orcamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orcamentos : get all the orcamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orcamentos in body
     */
    @GetMapping("/orcamentos")
    @Timed
    public List<Orcamento> getAllOrcamentos() {
        log.debug("REST request to get all Orcamentos");
        return orcamentoRepository.findAll();
        }

    /**
     * GET  /orcamentos/:id : get the "id" orcamento.
     *
     * @param id the id of the orcamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orcamento, or with status 404 (Not Found)
     */
    @GetMapping("/orcamentos/{id}")
    @Timed
    public ResponseEntity<Orcamento> getOrcamento(@PathVariable Long id) {
        log.debug("REST request to get Orcamento : {}", id);
        Orcamento orcamento = orcamentoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(orcamento));
    }

    /**
     * DELETE  /orcamentos/:id : delete the "id" orcamento.
     *
     * @param id the id of the orcamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orcamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrcamento(@PathVariable Long id) {
        log.debug("REST request to delete Orcamento : {}", id);
        orcamentoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

/**
* GET  /servicos/clientes/{clienteId} : get all the servicos by owner id.
*
* @return the ResponseEntity with status 200 (OK) and the list of actions in body
*/
@GetMapping("/orcamentos/servicos/{servicoId}")
@Timed
public List<Orcamento> getAllOrcamentosForServico(@PathVariable Long servicoId) {
log.debug("REST request to get all orcamentos for servico : {}", servicoId);
 
List<Orcamento> actions = orcamentoRepository.findByServicoId(servicoId);
return actions;
}

}
