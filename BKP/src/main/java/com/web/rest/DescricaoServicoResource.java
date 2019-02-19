package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.DescricaoServico;
import com.service.DescricaoServicoService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.DescricaoServicoCriteria;
import com.service.DescricaoServicoQueryService;
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
 * REST controller for managing DescricaoServico.
 */
@RestController
@RequestMapping("/api")
public class DescricaoServicoResource {

    private final Logger log = LoggerFactory.getLogger(DescricaoServicoResource.class);

    private static final String ENTITY_NAME = "descricaoServico";

    private final DescricaoServicoService descricaoServicoService;

    private final DescricaoServicoQueryService descricaoServicoQueryService;

    public DescricaoServicoResource(DescricaoServicoService descricaoServicoService, DescricaoServicoQueryService descricaoServicoQueryService) {
        this.descricaoServicoService = descricaoServicoService;
        this.descricaoServicoQueryService = descricaoServicoQueryService;
    }

    /**
     * POST  /descricao-servicos : Create a new descricaoServico.
     *
     * @param descricaoServico the descricaoServico to create
     * @return the ResponseEntity with status 201 (Created) and with body the new descricaoServico, or with status 400 (Bad Request) if the descricaoServico has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/descricao-servicos")
    @Timed
    public ResponseEntity<DescricaoServico> createDescricaoServico(@Valid @RequestBody DescricaoServico descricaoServico) throws URISyntaxException {
        log.debug("REST request to save DescricaoServico : {}", descricaoServico);
        if (descricaoServico.getId() != null) {
            throw new BadRequestAlertException("A new descricaoServico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescricaoServico result = descricaoServicoService.save(descricaoServico);
        return ResponseEntity.created(new URI("/api/descricao-servicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /descricao-servicos : Updates an existing descricaoServico.
     *
     * @param descricaoServico the descricaoServico to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated descricaoServico,
     * or with status 400 (Bad Request) if the descricaoServico is not valid,
     * or with status 500 (Internal Server Error) if the descricaoServico couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/descricao-servicos")
    @Timed
    public ResponseEntity<DescricaoServico> updateDescricaoServico(@Valid @RequestBody DescricaoServico descricaoServico) throws URISyntaxException {
        log.debug("REST request to update DescricaoServico : {}", descricaoServico);
        if (descricaoServico.getId() == null) {
            return createDescricaoServico(descricaoServico);
        }
        DescricaoServico result = descricaoServicoService.save(descricaoServico);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, descricaoServico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /descricao-servicos : get all the descricaoServicos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of descricaoServicos in body
     */
    @GetMapping("/descricao-servicos")
    @Timed
    public ResponseEntity<List<DescricaoServico>> getAllDescricaoServicos(DescricaoServicoCriteria criteria) {
        log.debug("REST request to get DescricaoServicos by criteria: {}", criteria);
        List<DescricaoServico> entityList = descricaoServicoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /descricao-servicos/:id : get the "id" descricaoServico.
     *
     * @param id the id of the descricaoServico to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the descricaoServico, or with status 404 (Not Found)
     */
    @GetMapping("/descricao-servicos/{id}")
    @Timed
    public ResponseEntity<DescricaoServico> getDescricaoServico(@PathVariable Long id) {
        log.debug("REST request to get DescricaoServico : {}", id);
        DescricaoServico descricaoServico = descricaoServicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(descricaoServico));
    }

    /**
     * DELETE  /descricao-servicos/:id : delete the "id" descricaoServico.
     *
     * @param id the id of the descricaoServico to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/descricao-servicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDescricaoServico(@PathVariable Long id) {
        log.debug("REST request to delete DescricaoServico : {}", id);
        descricaoServicoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
