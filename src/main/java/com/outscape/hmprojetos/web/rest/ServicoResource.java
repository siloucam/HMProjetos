package com.outscape.hmprojetos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outscape.hmprojetos.domain.Servico;

import com.outscape.hmprojetos.repository.ServicoRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Servico.
 */
@RestController
@RequestMapping("/api")
public class ServicoResource {

    private final Logger log = LoggerFactory.getLogger(ServicoResource.class);

    private static final String ENTITY_NAME = "servico";

    private final ServicoRepository servicoRepository;

    public ServicoResource(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    /**
     * POST  /servicos : Create a new servico.
     *
     * @param servico the servico to create
     * @return the ResponseEntity with status 201 (Created) and with body the new servico, or with status 400 (Bad Request) if the servico has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/servicos")
    @Timed
    public ResponseEntity<Servico> createServico(@Valid @RequestBody Servico servico) throws URISyntaxException {
        log.debug("REST request to save Servico : {}", servico);
        if (servico.getId() != null) {
            throw new BadRequestAlertException("A new servico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Servico result = servicoRepository.save(servico);
        return ResponseEntity.created(new URI("/api/servicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /servicos : Updates an existing servico.
     *
     * @param servico the servico to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated servico,
     * or with status 400 (Bad Request) if the servico is not valid,
     * or with status 500 (Internal Server Error) if the servico couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/servicos")
    @Timed
    public ResponseEntity<Servico> updateServico(@Valid @RequestBody Servico servico) throws URISyntaxException {
        log.debug("REST request to update Servico : {}", servico);
        if (servico.getId() == null) {
            return createServico(servico);
        }
        Servico result = servicoRepository.save(servico);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, servico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /servicos : get all the servicos.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of servicos in body
     */
    @GetMapping("/servicos")
    @Timed
    public ResponseEntity<List<Servico>> getAllServicos(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("orcamento-is-null".equals(filter)) {
            log.debug("REST request to get all Servicos where orcamento is null");
            return new ResponseEntity<>(StreamSupport
                .stream(servicoRepository.findAll().spliterator(), false)
                .filter(servico -> servico.getOrcamento() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Servicos");
        Page<Servico> page = servicoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/servicos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /servicos/:id : get the "id" servico.
     *
     * @param id the id of the servico to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the servico, or with status 404 (Not Found)
     */
    @GetMapping("/servicos/{id}")
    @Timed
    public ResponseEntity<Servico> getServico(@PathVariable Long id) {
        log.debug("REST request to get Servico : {}", id);
        Servico servico = servicoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(servico));
    }

    /**
     * DELETE  /servicos/:id : delete the "id" servico.
     *
     * @param id the id of the servico to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/servicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteServico(@PathVariable Long id) {
        log.debug("REST request to delete Servico : {}", id);
        servicoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

/**
* GET  /servicos/clientes/{clienteId} : get all the servicos by owner id.
*
* @return the ResponseEntity with status 200 (OK) and the list of actions in body
*/
@GetMapping("/servicos/clientes/{clienteId}")
@Timed
public List<Servico> getAllServicosForCliente(@PathVariable Long clienteId) {
log.debug("REST request to get all servicos for cliente : {}", clienteId);
 
List<Servico> actions = servicoRepository.findByClienteId(clienteId);
return actions;
}




    
}
