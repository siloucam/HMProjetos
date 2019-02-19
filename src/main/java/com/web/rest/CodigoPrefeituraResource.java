package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.CodigoPrefeitura;
import com.service.CodigoPrefeituraService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.CodigoPrefeituraCriteria;
import com.service.CodigoPrefeituraQueryService;
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
 * REST controller for managing CodigoPrefeitura.
 */
@RestController
@RequestMapping("/api")
public class CodigoPrefeituraResource {

    private final Logger log = LoggerFactory.getLogger(CodigoPrefeituraResource.class);

    private static final String ENTITY_NAME = "codigoPrefeitura";

    private final CodigoPrefeituraService codigoPrefeituraService;

    private final CodigoPrefeituraQueryService codigoPrefeituraQueryService;

    public CodigoPrefeituraResource(CodigoPrefeituraService codigoPrefeituraService, CodigoPrefeituraQueryService codigoPrefeituraQueryService) {
        this.codigoPrefeituraService = codigoPrefeituraService;
        this.codigoPrefeituraQueryService = codigoPrefeituraQueryService;
    }

    /**
     * POST  /codigo-prefeituras : Create a new codigoPrefeitura.
     *
     * @param codigoPrefeitura the codigoPrefeitura to create
     * @return the ResponseEntity with status 201 (Created) and with body the new codigoPrefeitura, or with status 400 (Bad Request) if the codigoPrefeitura has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/codigo-prefeituras")
    @Timed
    public ResponseEntity<CodigoPrefeitura> createCodigoPrefeitura(@RequestBody CodigoPrefeitura codigoPrefeitura) throws URISyntaxException {
        log.debug("REST request to save CodigoPrefeitura : {}", codigoPrefeitura);
        if (codigoPrefeitura.getId() != null) {
            throw new BadRequestAlertException("A new codigoPrefeitura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CodigoPrefeitura result = codigoPrefeituraService.save(codigoPrefeitura);
        return ResponseEntity.created(new URI("/api/codigo-prefeituras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /codigo-prefeituras : Updates an existing codigoPrefeitura.
     *
     * @param codigoPrefeitura the codigoPrefeitura to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated codigoPrefeitura,
     * or with status 400 (Bad Request) if the codigoPrefeitura is not valid,
     * or with status 500 (Internal Server Error) if the codigoPrefeitura couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/codigo-prefeituras")
    @Timed
    public ResponseEntity<CodigoPrefeitura> updateCodigoPrefeitura(@RequestBody CodigoPrefeitura codigoPrefeitura) throws URISyntaxException {
        log.debug("REST request to update CodigoPrefeitura : {}", codigoPrefeitura);
        if (codigoPrefeitura.getId() == null) {
            return createCodigoPrefeitura(codigoPrefeitura);
        }
        CodigoPrefeitura result = codigoPrefeituraService.save(codigoPrefeitura);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, codigoPrefeitura.getId().toString()))
            .body(result);
    }

    /**
     * GET  /codigo-prefeituras : get all the codigoPrefeituras.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of codigoPrefeituras in body
     */
    @GetMapping("/codigo-prefeituras")
    @Timed
    public ResponseEntity<List<CodigoPrefeitura>> getAllCodigoPrefeituras(CodigoPrefeituraCriteria criteria) {
        log.debug("REST request to get CodigoPrefeituras by criteria: {}", criteria);
        List<CodigoPrefeitura> entityList = codigoPrefeituraQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /codigo-prefeituras/:id : get the "id" codigoPrefeitura.
     *
     * @param id the id of the codigoPrefeitura to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the codigoPrefeitura, or with status 404 (Not Found)
     */
    @GetMapping("/codigo-prefeituras/{id}")
    @Timed
    public ResponseEntity<CodigoPrefeitura> getCodigoPrefeitura(@PathVariable Long id) {
        log.debug("REST request to get CodigoPrefeitura : {}", id);
        CodigoPrefeitura codigoPrefeitura = codigoPrefeituraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(codigoPrefeitura));
    }

    /**
     * DELETE  /codigo-prefeituras/:id : delete the "id" codigoPrefeitura.
     *
     * @param id the id of the codigoPrefeitura to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/codigo-prefeituras/{id}")
    @Timed
    public ResponseEntity<Void> deleteCodigoPrefeitura(@PathVariable Long id) {
        log.debug("REST request to delete CodigoPrefeitura : {}", id);
        codigoPrefeituraService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
