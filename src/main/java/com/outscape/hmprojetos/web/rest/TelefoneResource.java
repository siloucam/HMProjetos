package com.outscape.hmprojetos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outscape.hmprojetos.domain.Telefone;

import com.outscape.hmprojetos.repository.TelefoneRepository;
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
 * REST controller for managing Telefone.
 */
@RestController
@RequestMapping("/api")
public class TelefoneResource {

    private final Logger log = LoggerFactory.getLogger(TelefoneResource.class);

    private static final String ENTITY_NAME = "telefone";

    private final TelefoneRepository telefoneRepository;

    public TelefoneResource(TelefoneRepository telefoneRepository) {
        this.telefoneRepository = telefoneRepository;
    }

    /**
     * POST  /telefones : Create a new telefone.
     *
     * @param telefone the telefone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telefone, or with status 400 (Bad Request) if the telefone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telefones")
    @Timed
    public ResponseEntity<Telefone> createTelefone(@Valid @RequestBody Telefone telefone) throws URISyntaxException {
        log.debug("REST request to save Telefone : {}", telefone);
        if (telefone.getId() != null) {
            throw new BadRequestAlertException("A new telefone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Telefone result = telefoneRepository.save(telefone);
        return ResponseEntity.created(new URI("/api/telefones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telefones : Updates an existing telefone.
     *
     * @param telefone the telefone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telefone,
     * or with status 400 (Bad Request) if the telefone is not valid,
     * or with status 500 (Internal Server Error) if the telefone couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/telefones")
    @Timed
    public ResponseEntity<Telefone> updateTelefone(@Valid @RequestBody Telefone telefone) throws URISyntaxException {
        log.debug("REST request to update Telefone : {}", telefone);
        if (telefone.getId() == null) {
            return createTelefone(telefone);
        }
        Telefone result = telefoneRepository.save(telefone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telefones : get all the telefones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of telefones in body
     */
    @GetMapping("/telefones")
    @Timed
    public List<Telefone> getAllTelefones() {
        log.debug("REST request to get all Telefones");
        return telefoneRepository.findAll();
        }

    /**
     * GET  /telefones/:id : get the "id" telefone.
     *
     * @param id the id of the telefone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telefone, or with status 404 (Not Found)
     */
    @GetMapping("/telefones/{id}")
    @Timed
    public ResponseEntity<Telefone> getTelefone(@PathVariable Long id) {
        log.debug("REST request to get Telefone : {}", id);
        Telefone telefone = telefoneRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(telefone));
    }

    /**
     * DELETE  /telefones/:id : delete the "id" telefone.
     *
     * @param id the id of the telefone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telefones/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelefone(@PathVariable Long id) {
        log.debug("REST request to delete Telefone : {}", id);
        telefoneRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

/**
* GET  /telefones/clientes/{clienteId} : get all the telefones by owner id.
*
* @return the ResponseEntity with status 200 (OK) and the list of actions in body
*/
@GetMapping("/telefones/clientes/{clienteId}")
@Timed
public List<Telefone> getAllTelefonesForCliente(@PathVariable Long clienteId) {
log.debug("REST request to get all telefones for cliente : {}", clienteId);
 
List<Telefone> actions = telefoneRepository.findByClienteId(clienteId);
return actions;
}

}
