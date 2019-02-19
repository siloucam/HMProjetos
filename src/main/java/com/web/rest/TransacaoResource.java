package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.Transacao;
import com.service.TransacaoService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.TransacaoCriteria;
import com.service.TransacaoQueryService;
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

    private final TransacaoService transacaoService;

    private final TransacaoQueryService transacaoQueryService;

    public TransacaoResource(TransacaoService transacaoService, TransacaoQueryService transacaoQueryService) {
        this.transacaoService = transacaoService;
        this.transacaoQueryService = transacaoQueryService;
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
        Transacao result = transacaoService.save(transacao);
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
        Transacao result = transacaoService.save(transacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transacaos : get all the transacaos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of transacaos in body
     */
    @GetMapping("/transacaos")
    @Timed
    public ResponseEntity<List<Transacao>> getAllTransacaos(TransacaoCriteria criteria) {
        log.debug("REST request to get Transacaos by criteria: {}", criteria);
        List<Transacao> entityList = transacaoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
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
        Transacao transacao = transacaoService.findOne(id);
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
        transacaoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
