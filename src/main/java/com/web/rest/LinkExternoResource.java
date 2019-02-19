package com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.domain.LinkExterno;
import com.service.LinkExternoService;
import com.web.rest.errors.BadRequestAlertException;
import com.web.rest.util.HeaderUtil;
import com.service.dto.LinkExternoCriteria;
import com.service.LinkExternoQueryService;
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
 * REST controller for managing LinkExterno.
 */
@RestController
@RequestMapping("/api")
public class LinkExternoResource {

    private final Logger log = LoggerFactory.getLogger(LinkExternoResource.class);

    private static final String ENTITY_NAME = "linkExterno";

    private final LinkExternoService linkExternoService;

    private final LinkExternoQueryService linkExternoQueryService;

    public LinkExternoResource(LinkExternoService linkExternoService, LinkExternoQueryService linkExternoQueryService) {
        this.linkExternoService = linkExternoService;
        this.linkExternoQueryService = linkExternoQueryService;
    }

    /**
     * POST  /link-externos : Create a new linkExterno.
     *
     * @param linkExterno the linkExterno to create
     * @return the ResponseEntity with status 201 (Created) and with body the new linkExterno, or with status 400 (Bad Request) if the linkExterno has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/link-externos")
    @Timed
    public ResponseEntity<LinkExterno> createLinkExterno(@RequestBody LinkExterno linkExterno) throws URISyntaxException {
        log.debug("REST request to save LinkExterno : {}", linkExterno);
        if (linkExterno.getId() != null) {
            throw new BadRequestAlertException("A new linkExterno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LinkExterno result = linkExternoService.save(linkExterno);
        return ResponseEntity.created(new URI("/api/link-externos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /link-externos : Updates an existing linkExterno.
     *
     * @param linkExterno the linkExterno to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated linkExterno,
     * or with status 400 (Bad Request) if the linkExterno is not valid,
     * or with status 500 (Internal Server Error) if the linkExterno couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/link-externos")
    @Timed
    public ResponseEntity<LinkExterno> updateLinkExterno(@RequestBody LinkExterno linkExterno) throws URISyntaxException {
        log.debug("REST request to update LinkExterno : {}", linkExterno);
        if (linkExterno.getId() == null) {
            return createLinkExterno(linkExterno);
        }
        LinkExterno result = linkExternoService.save(linkExterno);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, linkExterno.getId().toString()))
            .body(result);
    }

    /**
     * GET  /link-externos : get all the linkExternos.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of linkExternos in body
     */
    @GetMapping("/link-externos")
    @Timed
    public ResponseEntity<List<LinkExterno>> getAllLinkExternos(LinkExternoCriteria criteria) {
        log.debug("REST request to get LinkExternos by criteria: {}", criteria);
        List<LinkExterno> entityList = linkExternoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /link-externos/:id : get the "id" linkExterno.
     *
     * @param id the id of the linkExterno to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the linkExterno, or with status 404 (Not Found)
     */
    @GetMapping("/link-externos/{id}")
    @Timed
    public ResponseEntity<LinkExterno> getLinkExterno(@PathVariable Long id) {
        log.debug("REST request to get LinkExterno : {}", id);
        LinkExterno linkExterno = linkExternoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(linkExterno));
    }

    /**
     * DELETE  /link-externos/:id : delete the "id" linkExterno.
     *
     * @param id the id of the linkExterno to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/link-externos/{id}")
    @Timed
    public ResponseEntity<Void> deleteLinkExterno(@PathVariable Long id) {
        log.debug("REST request to delete LinkExterno : {}", id);
        linkExternoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
