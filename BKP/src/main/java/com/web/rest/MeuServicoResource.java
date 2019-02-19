package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.MeuServico;
import com.service.MeuServicoService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.MeuServicoCriteria;
import com.service.MeuServicoQueryService;
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
 * REST controller for managing MeuServico.
 */
@RestController
@RequestMapping("/api")
public class MeuServicoResource {

    private final Logger log = LoggerFactory.getLogger(MeuServicoResource.class);

    private static final String ENTITY_NAME = "meuServico";

    private final MeuServicoService meuServicoService;

    private final MeuServicoQueryService meuServicoQueryService;

    public MeuServicoResource(MeuServicoService meuServicoService, MeuServicoQueryService meuServicoQueryService) {
        this.meuServicoService = meuServicoService;
        this.meuServicoQueryService = meuServicoQueryService;
    }

    /**
     * POST  /meu-servicos : Create a new meuServico.
     *
     * @param meuServico the meuServico to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meuServico, or with status 400 (Bad Request) if the meuServico has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meu-servicos")
    @Timed
    public ResponseEntity<MeuServico> createMeuServico(@RequestBody MeuServico meuServico) throws URISyntaxException {
        log.debug("REST request to save MeuServico : {}", meuServico);
        if (meuServico.getId() != null) {
            throw new BadRequestAlertException("A new meuServico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeuServico result = meuServicoService.save(meuServico);
        return ResponseEntity.created(new URI("/api/meu-servicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meu-servicos : Updates an existing meuServico.
     *
     * @param meuServico the meuServico to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meuServico,
     * or with status 400 (Bad Request) if the meuServico is not valid,
     * or with status 500 (Internal Server Error) if the meuServico couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meu-servicos")
    @Timed
    public ResponseEntity<MeuServico> updateMeuServico(@RequestBody MeuServico meuServico) throws URISyntaxException {
        log.debug("REST request to update MeuServico : {}", meuServico);
        if (meuServico.getId() == null) {
            return createMeuServico(meuServico);
        }
        MeuServico result = meuServicoService.save(meuServico);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meuServico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meu-servicos : get all the meuServicos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of meuServicos in body
     */
    @GetMapping("/meu-servicos")
    @Timed
    public ResponseEntity<List<MeuServico>> getAllMeuServicos(MeuServicoCriteria criteria) {
        log.debug("REST request to get MeuServicos by criteria: {}", criteria);
        List<MeuServico> entityList = meuServicoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /meu-servicos/:id : get the "id" meuServico.
     *
     * @param id the id of the meuServico to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meuServico, or with status 404 (Not Found)
     */
    @GetMapping("/meu-servicos/{id}")
    @Timed
    public ResponseEntity<MeuServico> getMeuServico(@PathVariable Long id) {
        log.debug("REST request to get MeuServico : {}", id);
        MeuServico meuServico = meuServicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(meuServico));
    }

    /**
     * DELETE  /meu-servicos/:id : delete the "id" meuServico.
     *
     * @param id the id of the meuServico to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meu-servicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeuServico(@PathVariable Long id) {
        log.debug("REST request to delete MeuServico : {}", id);
        meuServicoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
