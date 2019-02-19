package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.Codigos;
import com.service.CodigosService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.CodigosCriteria;
import com.service.CodigosQueryService;
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
 * REST controller for managing Codigos.
 */
@RestController
@RequestMapping("/api")
public class CodigosResource {

    private final Logger log = LoggerFactory.getLogger(CodigosResource.class);

    private static final String ENTITY_NAME = "codigos";

    private final CodigosService codigosService;

    private final CodigosQueryService codigosQueryService;

    public CodigosResource(CodigosService codigosService, CodigosQueryService codigosQueryService) {
        this.codigosService = codigosService;
        this.codigosQueryService = codigosQueryService;
    }

    /**
     * POST  /codigos : Create a new codigos.
     *
     * @param codigos the codigos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codigos, or with status 400 (Bad Request) if the codigos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/codigos")
    @Timed
    public ResponseEntity<Codigos> createCodigos(@RequestBody Codigos codigos) throws URISyntaxException {
        log.debug("REST request to save Codigos : {}", codigos);
        if (codigos.getId() != null) {
            throw new BadRequestAlertException("A new codigos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Codigos result = codigosService.save(codigos);
        return ResponseEntity.created(new URI("/api/codigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /codigos : Updates an existing codigos.
     *
     * @param codigos the codigos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated codigos,
     * or with status 400 (Bad Request) if the codigos is not valid,
     * or with status 500 (Internal Server Error) if the codigos couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/codigos")
    @Timed
    public ResponseEntity<Codigos> updateCodigos(@RequestBody Codigos codigos) throws URISyntaxException {
        log.debug("REST request to update Codigos : {}", codigos);
        if (codigos.getId() == null) {
            return createCodigos(codigos);
        }
        Codigos result = codigosService.save(codigos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, codigos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /codigos : get all the codigos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of codigos in body
     */
    @GetMapping("/codigos")
    @Timed
    public ResponseEntity<List<Codigos>> getAllCodigos(CodigosCriteria criteria) {
        log.debug("REST request to get Codigos by criteria: {}", criteria);
        List<Codigos> entityList = codigosQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /codigos/:id : get the "id" codigos.
     *
     * @param id the id of the codigos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the codigos, or with status 404 (Not Found)
     */
    @GetMapping("/codigos/{id}")
    @Timed
    public ResponseEntity<Codigos> getCodigos(@PathVariable Long id) {
        log.debug("REST request to get Codigos : {}", id);
        Codigos codigos = codigosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codigos));
    }

    /**
     * DELETE  /codigos/:id : delete the "id" codigos.
     *
     * @param id the id of the codigos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/codigos/{id}")
    @Timed
    public ResponseEntity<Void> deleteCodigos(@PathVariable Long id) {
        log.debug("REST request to delete Codigos : {}", id);
        codigosService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
