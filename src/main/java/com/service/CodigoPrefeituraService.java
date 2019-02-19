package com.service;

import com.domain.CodigoPrefeitura;
import com.repository.CodigoPrefeituraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing CodigoPrefeitura.
 */
@Service
@Transactional
public class CodigoPrefeituraService {

    private final Logger log = LoggerFactory.getLogger(CodigoPrefeituraService.class);

    private final CodigoPrefeituraRepository codigoPrefeituraRepository;

    public CodigoPrefeituraService(CodigoPrefeituraRepository codigoPrefeituraRepository) {
        this.codigoPrefeituraRepository = codigoPrefeituraRepository;
    }

    /**
     * Save a codigoPrefeitura.
     *
     * @param codigoPrefeitura the entity to save
     * @return the persisted entity
     */
    public CodigoPrefeitura save(CodigoPrefeitura codigoPrefeitura) {
        log.debug("Request to save CodigoPrefeitura : {}", codigoPrefeitura);
        return codigoPrefeituraRepository.save(codigoPrefeitura);
    }

    /**
     * Get all the codigoPrefeituras.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CodigoPrefeitura> findAll() {
        log.debug("Request to get all CodigoPrefeituras");
        return codigoPrefeituraRepository.findAll();
    }

    /**
     * Get one codigoPrefeitura by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CodigoPrefeitura findOne(Long id) {
        log.debug("Request to get CodigoPrefeitura : {}", id);
        return codigoPrefeituraRepository.findOne(id);
    }

    /**
     * Delete the codigoPrefeitura by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CodigoPrefeitura : {}", id);
        codigoPrefeituraRepository.delete(id);
    }
}
