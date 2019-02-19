package com.service;

import com.domain.Codigos;
import com.repository.CodigosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Codigos.
 */
@Service
@Transactional
public class CodigosService {

    private final Logger log = LoggerFactory.getLogger(CodigosService.class);

    private final CodigosRepository codigosRepository;

    public CodigosService(CodigosRepository codigosRepository) {
        this.codigosRepository = codigosRepository;
    }

    /**
     * Save a codigos.
     *
     * @param codigos the entity to save
     * @return the persisted entity
     */
    public Codigos save(Codigos codigos) {
        log.debug("Request to save Codigos : {}", codigos);
        return codigosRepository.save(codigos);
    }

    /**
     * Get all the codigos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Codigos> findAll() {
        log.debug("Request to get all Codigos");
        return codigosRepository.findAll();
    }

    /**
     * Get one codigos by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Codigos findOne(Long id) {
        log.debug("Request to get Codigos : {}", id);
        return codigosRepository.findOne(id);
    }

    /**
     * Delete the codigos by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Codigos : {}", id);
        codigosRepository.delete(id);
    }
}
