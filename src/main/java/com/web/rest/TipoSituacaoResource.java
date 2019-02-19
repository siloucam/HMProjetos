package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.TipoSituacao;
import com.service.TipoSituacaoService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.TipoSituacaoCriteria;
import com.service.TipoSituacaoQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TipoSituacao.
 */
@RestController
@RequestMapping("/api")
public class TipoSituacaoResource {

    private final Logger log = LoggerFactory.getLogger(TipoSituacaoResource.class);

    private static final String ENTITY_NAME = "tipoSituacao";

    private final TipoSituacaoService tipoSituacaoService;

    private final TipoSituacaoQueryService tipoSituacaoQueryService;

    public TipoSituacaoResource(TipoSituacaoService tipoSituacaoService, TipoSituacaoQueryService tipoSituacaoQueryService) {
        this.tipoSituacaoService = tipoSituacaoService;
        this.tipoSituacaoQueryService = tipoSituacaoQueryService;
    }

    /**
     * POST  /tipo-situacaos : Create a new tipoSituacao.
     *
     * @param tipoSituacao the tipoSituacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoSituacao, or with status 400 (Bad Request) if the tipoSituacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipo-situacaos")
    @Timed
    public ResponseEntity<TipoSituacao> createTipoSituacao(@RequestBody TipoSituacao tipoSituacao) throws URISyntaxException {
        log.debug("REST request to save TipoSituacao : {}", tipoSituacao);
        if (tipoSituacao.getId() != null) {
            throw new BadRequestAlertException("A new tipoSituacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoSituacao result = tipoSituacaoService.save(tipoSituacao);
        return ResponseEntity.created(new URI("/api/tipo-situacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-situacaos : Updates an existing tipoSituacao.
     *
     * @param tipoSituacao the tipoSituacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoSituacao,
     * or with status 400 (Bad Request) if the tipoSituacao is not valid,
     * or with status 500 (Internal Server Error) if the tipoSituacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipo-situacaos")
    @Timed
    public ResponseEntity<TipoSituacao> updateTipoSituacao(@RequestBody TipoSituacao tipoSituacao) throws URISyntaxException {
        log.debug("REST request to update TipoSituacao : {}", tipoSituacao);
        if (tipoSituacao.getId() == null) {
            return createTipoSituacao(tipoSituacao);
        }
        TipoSituacao result = tipoSituacaoService.save(tipoSituacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipoSituacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-situacaos : get all the tipoSituacaos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tipoSituacaos in body
     */
    @GetMapping("/tipo-situacaos")
    @Timed
    public ResponseEntity<List<TipoSituacao>> getAllTipoSituacaos(TipoSituacaoCriteria criteria) {
        log.debug("REST request to get TipoSituacaos by criteria: {}", criteria);
        List<TipoSituacao> entityList = tipoSituacaoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /tipo-situacaos/:id : get the "id" tipoSituacao.
     *
     * @param id the id of the tipoSituacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoSituacao, or with status 404 (Not Found)
     */
    @GetMapping("/tipo-situacaos/{id}")
    @Timed
    public ResponseEntity<TipoSituacao> getTipoSituacao(@PathVariable Long id) {
        log.debug("REST request to get TipoSituacao : {}", id);
        TipoSituacao tipoSituacao = tipoSituacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipoSituacao));
    }

    /**
     * DELETE  /tipo-situacaos/:id : delete the "id" tipoSituacao.
     *
     * @param id the id of the tipoSituacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipo-situacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipoSituacao(@PathVariable Long id) {
        log.debug("REST request to delete TipoSituacao : {}", id);
        tipoSituacaoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
