package com.outscape.hmprojetos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outscape.hmprojetos.domain.Transacao;

import com.outscape.hmprojetos.repository.TransacaoRepository;
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
 * REST controller for managing Transacao.
 */
@RestController
@RequestMapping("/api")
public class TransacaoResource {

    private final Logger log = LoggerFactory.getLogger(TransacaoResource.class);

    private static final String ENTITY_NAME = "transacao";

    private final TransacaoRepository transacaoRepository;

    public TransacaoResource(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * POST  /transacaos : Create a new transacao.
     *
     * @param transacao the transacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transacao, or with status 400 (Bad Request) if the transacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transacaos")
    @Timed
    public ResponseEntity<Transacao> createTransacao(@Valid @RequestBody Transacao transacao) throws URISyntaxException {
        log.debug("REST request to save Transacao : {}", transacao);
        if (transacao.getId() != null) {
            throw new BadRequestAlertException("A new transacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transacao result = transacaoRepository.save(transacao);
        return ResponseEntity.created(new URI("/api/transacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transacaos : Updates an existing transacao.
     *
     * @param transacao the transacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transacao,
     * or with status 400 (Bad Request) if the transacao is not valid,
     * or with status 500 (Internal Server Error) if the transacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transacaos")
    @Timed
    public ResponseEntity<Transacao> updateTransacao(@Valid @RequestBody Transacao transacao) throws URISyntaxException {
        log.debug("REST request to update Transacao : {}", transacao);
        if (transacao.getId() == null) {
            return createTransacao(transacao);
        }
        Transacao result = transacaoRepository.save(transacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transacaos : get all the transacaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transacaos in body
     */
    @GetMapping("/transacaos")
    @Timed
    public List<Transacao> getAllTransacaos() {
        log.debug("REST request to get all Transacaos");
        return transacaoRepository.findAll();
        }

    /**
     * GET  /transacaos/:id : get the "id" transacao.
     *
     * @param id the id of the transacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transacao, or with status 404 (Not Found)
     */
    @GetMapping("/transacaos/{id}")
    @Timed
    public ResponseEntity<Transacao> getTransacao(@PathVariable Long id) {
        log.debug("REST request to get Transacao : {}", id);
        Transacao transacao = transacaoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transacao));
    }

    /**
     * DELETE  /transacaos/:id : delete the "id" transacao.
     *
     * @param id the id of the transacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransacao(@PathVariable Long id) {
        log.debug("REST request to delete Transacao : {}", id);
        transacaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
* GET  /servicos/clientes/{clienteId} : get all the servicos by owner id.
*
* @return the ResponseEntity with status 200 (OK) and the list of actions in body
*/
@GetMapping("/transacaos/servicos/{servicoId}")
@Timed
public List<Transacao> getAllTransacaosForServico(@PathVariable Long servicoId) {
log.debug("REST request to get all transacaos for servico : {}", servicoId);
 
List<Transacao> actions = transacaoRepository.findByServicoId(servicoId);
return actions;
}

}
