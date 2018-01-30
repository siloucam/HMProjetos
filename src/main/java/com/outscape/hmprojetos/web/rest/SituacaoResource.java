package com.outscape.hmprojetos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outscape.hmprojetos.domain.Situacao;

import com.outscape.hmprojetos.repository.SituacaoRepository;
import com.outscape.hmprojetos.web.rest.errors.BadRequestAlertException;
import com.outscape.hmprojetos.web.rest.util.HeaderUtil;
import com.outscape.hmprojetos.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Situacao.
 */
@RestController
@RequestMapping("/api")
public class SituacaoResource {

    private final Logger log = LoggerFactory.getLogger(SituacaoResource.class);

    private static final String ENTITY_NAME = "situacao";

    private final SituacaoRepository situacaoRepository;

    public SituacaoResource(SituacaoRepository situacaoRepository) {
        this.situacaoRepository = situacaoRepository;
    }

    /**
     * POST  /situacaos : Create a new situacao.
     *
     * @param situacao the situacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new situacao, or with status 400 (Bad Request) if the situacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/situacaos")
    @Timed
    public ResponseEntity<Situacao> createSituacao(@Valid @RequestBody Situacao situacao) throws URISyntaxException {
        log.debug("REST request to save Situacao : {}", situacao);
        if (situacao.getId() != null) {
            throw new BadRequestAlertException("A new situacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Situacao result = situacaoRepository.save(situacao);
        return ResponseEntity.created(new URI("/api/situacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /situacaos : Updates an existing situacao.
     *
     * @param situacao the situacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated situacao,
     * or with status 400 (Bad Request) if the situacao is not valid,
     * or with status 500 (Internal Server Error) if the situacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/situacaos")
    @Timed
    public ResponseEntity<Situacao> updateSituacao(@Valid @RequestBody Situacao situacao) throws URISyntaxException {
        log.debug("REST request to update Situacao : {}", situacao);
        if (situacao.getId() == null) {
            return createSituacao(situacao);
        }
        Situacao result = situacaoRepository.save(situacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, situacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /situacaos : get all the situacaos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of situacaos in body
     */
    @GetMapping("/situacaos")
    @Timed
    public ResponseEntity<List<Situacao>> getAllSituacaos(Pageable pageable) {
        log.debug("REST request to get a page of Situacaos");
        Page<Situacao> page = situacaoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/situacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /situacaos/:id : get the "id" situacao.
     *
     * @param id the id of the situacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the situacao, or with status 404 (Not Found)
     */
    @GetMapping("/situacaos/{id}")
    @Timed
    public ResponseEntity<Situacao> getSituacao(@PathVariable Long id) {
        log.debug("REST request to get Situacao : {}", id);
        Situacao situacao = situacaoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(situacao));
    }

    /**
     * DELETE  /situacaos/:id : delete the "id" situacao.
     *
     * @param id the id of the situacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/situacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSituacao(@PathVariable Long id) {
        log.debug("REST request to delete Situacao : {}", id);
        situacaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }



/**
* GET  /servicos/clientes/{clienteId} : get all the servicos by owner id.
*
* @return the ResponseEntity with status 200 (OK) and the list of actions in body
*/
@GetMapping("/situacaos/servicos/{servicoId}")
@Timed
public List<Situacao> getAllSituacaosForServico(@PathVariable Long servicoId) {
log.debug("REST request to get all situacaos for servico : {}", servicoId);
 
List<Situacao> actions = situacaoRepository.findByServicoId(servicoId);
return actions;
}



}
