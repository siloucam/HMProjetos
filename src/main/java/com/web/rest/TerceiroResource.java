package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.Terceiro;
import com.service.TerceiroService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.TerceiroCriteria;
import com.service.TerceiroQueryService;
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
 * REST controller for managing Terceiro.
 */
@RestController
@RequestMapping("/api")
public class TerceiroResource {

    private final Logger log = LoggerFactory.getLogger(TerceiroResource.class);

    private static final String ENTITY_NAME = "terceiro";

    private final TerceiroService terceiroService;

    private final TerceiroQueryService terceiroQueryService;

    public TerceiroResource(TerceiroService terceiroService, TerceiroQueryService terceiroQueryService) {
        this.terceiroService = terceiroService;
        this.terceiroQueryService = terceiroQueryService;
    }

    /**
     * POST  /terceiros : Create a new terceiro.
     *
     * @param terceiro the terceiro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new terceiro, or with status 400 (Bad Request) if the terceiro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/terceiros")
    @Timed
    public ResponseEntity<Terceiro> createTerceiro(@RequestBody Terceiro terceiro) throws URISyntaxException {
        log.debug("REST request to save Terceiro : {}", terceiro);
        if (terceiro.getId() != null) {
            throw new BadRequestAlertException("A new terceiro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Terceiro result = terceiroService.save(terceiro);
        return ResponseEntity.created(new URI("/api/terceiros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /terceiros : Updates an existing terceiro.
     *
     * @param terceiro the terceiro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated terceiro,
     * or with status 400 (Bad Request) if the terceiro is not valid,
     * or with status 500 (Internal Server Error) if the terceiro couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/terceiros")
    @Timed
    public ResponseEntity<Terceiro> updateTerceiro(@RequestBody Terceiro terceiro) throws URISyntaxException {
        log.debug("REST request to update Terceiro : {}", terceiro);
        if (terceiro.getId() == null) {
            return createTerceiro(terceiro);
        }
        Terceiro result = terceiroService.save(terceiro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, terceiro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /terceiros : get all the terceiros.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of terceiros in body
     */
    @GetMapping("/terceiros")
    @Timed
    public ResponseEntity<List<Terceiro>> getAllTerceiros(TerceiroCriteria criteria) {
        log.debug("REST request to get Terceiros by criteria: {}", criteria);
        List<Terceiro> entityList = terceiroQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /terceiros/:id : get the "id" terceiro.
     *
     * @param id the id of the terceiro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the terceiro, or with status 404 (Not Found)
     */
    @GetMapping("/terceiros/{id}")
    @Timed
    public ResponseEntity<Terceiro> getTerceiro(@PathVariable Long id) {
        log.debug("REST request to get Terceiro : {}", id);
        Terceiro terceiro = terceiroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(terceiro));
    }

    /**
     * DELETE  /terceiros/:id : delete the "id" terceiro.
     *
     * @param id the id of the terceiro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/terceiros/{id}")
    @Timed
    public ResponseEntity<Void> deleteTerceiro(@PathVariable Long id) {
        log.debug("REST request to delete Terceiro : {}", id);
        terceiroService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
