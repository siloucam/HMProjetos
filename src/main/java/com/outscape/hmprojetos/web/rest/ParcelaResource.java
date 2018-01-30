package com.outscape.hmprojetos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outscape.hmprojetos.domain.Parcela;

import com.outscape.hmprojetos.repository.ParcelaRepository;
import com.outscape.hmprojetos.web.rest.errors.BadRequestAlertException;
import com.outscape.hmprojetos.web.rest.util.HeaderUtil;
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
 * REST controller for managing Parcela.
 */
@RestController
@RequestMapping("/api")
public class ParcelaResource {

    private final Logger log = LoggerFactory.getLogger(ParcelaResource.class);

    private static final String ENTITY_NAME = "parcela";

    private final ParcelaRepository parcelaRepository;

    public ParcelaResource(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    /**
     * POST  /parcelas : Create a new parcela.
     *
     * @param parcela the parcela to create
     * @return the ResponseEntity with status 201 (Created) and with body the new parcela, or with status 400 (Bad Request) if the parcela has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parcelas")
    @Timed
    public ResponseEntity<Parcela> createParcela(@Valid @RequestBody Parcela parcela) throws URISyntaxException {
        log.debug("REST request to save Parcela : {}", parcela);
        if (parcela.getId() != null) {
            throw new BadRequestAlertException("A new parcela cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parcela result = parcelaRepository.save(parcela);
        return ResponseEntity.created(new URI("/api/parcelas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parcelas : Updates an existing parcela.
     *
     * @param parcela the parcela to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated parcela,
     * or with status 400 (Bad Request) if the parcela is not valid,
     * or with status 500 (Internal Server Error) if the parcela couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parcelas")
    @Timed
    public ResponseEntity<Parcela> updateParcela(@Valid @RequestBody Parcela parcela) throws URISyntaxException {
        log.debug("REST request to update Parcela : {}", parcela);
        if (parcela.getId() == null) {
            return createParcela(parcela);
        }
        Parcela result = parcelaRepository.save(parcela);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, parcela.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parcelas : get all the parcelas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of parcelas in body
     */
    @GetMapping("/parcelas")
    @Timed
    public List<Parcela> getAllParcelas() {
        log.debug("REST request to get all Parcelas");
        return parcelaRepository.findAll();
        }

    /**
     * GET  /parcelas/:id : get the "id" parcela.
     *
     * @param id the id of the parcela to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the parcela, or with status 404 (Not Found)
     */
    @GetMapping("/parcelas/{id}")
    @Timed
    public ResponseEntity<Parcela> getParcela(@PathVariable Long id) {
        log.debug("REST request to get Parcela : {}", id);
        Parcela parcela = parcelaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(parcela));
    }

    /**
     * DELETE  /parcelas/:id : delete the "id" parcela.
     *
     * @param id the id of the parcela to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parcelas/{id}")
    @Timed
    public ResponseEntity<Void> deleteParcela(@PathVariable Long id) {
        log.debug("REST request to delete Parcela : {}", id);
        parcelaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }



/**
* GET  /telefones/clientes/{clienteId} : get all the telefones by owner id.
*
* @return the ResponseEntity with status 200 (OK) and the list of actions in body
*/
@GetMapping("/parcelas/orcamentos/{orcamentoId}")
@Timed
public List<Parcela> getAllParcelasForOrcamento(@PathVariable Long orcamentoId) {
log.debug("REST request to get all parcelas for orcamento : {}", orcamentoId);
 
List<Parcela> actions = parcelaRepository.findByOrcamentoId(orcamentoId);
return actions;
}


    
}
