package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.DescricaoSituacao;
import com.service.DescricaoSituacaoService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.DescricaoSituacaoCriteria;
import com.service.DescricaoSituacaoQueryService;
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
 * REST controller for managing DescricaoSituacao.
 */
@RestController
@RequestMapping("/api")
public class DescricaoSituacaoResource {

    private final Logger log = LoggerFactory.getLogger(DescricaoSituacaoResource.class);

    private static final String ENTITY_NAME = "descricaoSituacao";

    private final DescricaoSituacaoService descricaoSituacaoService;

    private final DescricaoSituacaoQueryService descricaoSituacaoQueryService;

    public DescricaoSituacaoResource(DescricaoSituacaoService descricaoSituacaoService, DescricaoSituacaoQueryService descricaoSituacaoQueryService) {
        this.descricaoSituacaoService = descricaoSituacaoService;
        this.descricaoSituacaoQueryService = descricaoSituacaoQueryService;
    }

    /**
     * POST  /descricao-situacaos : Create a new descricaoSituacao.
     *
     * @param descricaoSituacao the descricaoSituacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new descricaoSituacao, or with status 400 (Bad Request) if the descricaoSituacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/descricao-situacaos")
    @Timed
    public ResponseEntity<DescricaoSituacao> createDescricaoSituacao(@RequestBody DescricaoSituacao descricaoSituacao) throws URISyntaxException {
        log.debug("REST request to save DescricaoSituacao : {}", descricaoSituacao);
        if (descricaoSituacao.getId() != null) {
            throw new BadRequestAlertException("A new descricaoSituacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescricaoSituacao result = descricaoSituacaoService.save(descricaoSituacao);
        return ResponseEntity.created(new URI("/api/descricao-situacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /descricao-situacaos : Updates an existing descricaoSituacao.
     *
     * @param descricaoSituacao the descricaoSituacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated descricaoSituacao,
     * or with status 400 (Bad Request) if the descricaoSituacao is not valid,
     * or with status 500 (Internal Server Error) if the descricaoSituacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/descricao-situacaos")
    @Timed
    public ResponseEntity<DescricaoSituacao> updateDescricaoSituacao(@RequestBody DescricaoSituacao descricaoSituacao) throws URISyntaxException {
        log.debug("REST request to update DescricaoSituacao : {}", descricaoSituacao);
        if (descricaoSituacao.getId() == null) {
            return createDescricaoSituacao(descricaoSituacao);
        }
        DescricaoSituacao result = descricaoSituacaoService.save(descricaoSituacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descricaoSituacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /descricao-situacaos : get all the descricaoSituacaos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of descricaoSituacaos in body
     */
    @GetMapping("/descricao-situacaos")
    @Timed
    public ResponseEntity<List<DescricaoSituacao>> getAllDescricaoSituacaos(DescricaoSituacaoCriteria criteria) {
        log.debug("REST request to get DescricaoSituacaos by criteria: {}", criteria);
        List<DescricaoSituacao> entityList = descricaoSituacaoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /descricao-situacaos/:id : get the "id" descricaoSituacao.
     *
     * @param id the id of the descricaoSituacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the descricaoSituacao, or with status 404 (Not Found)
     */
    @GetMapping("/descricao-situacaos/{id}")
    @Timed
    public ResponseEntity<DescricaoSituacao> getDescricaoSituacao(@PathVariable Long id) {
        log.debug("REST request to get DescricaoSituacao : {}", id);
        DescricaoSituacao descricaoSituacao = descricaoSituacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(descricaoSituacao));
    }

    /**
     * DELETE  /descricao-situacaos/:id : delete the "id" descricaoSituacao.
     *
     * @param id the id of the descricaoSituacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/descricao-situacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDescricaoSituacao(@PathVariable Long id) {
        log.debug("REST request to delete DescricaoSituacao : {}", id);
        descricaoSituacaoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
