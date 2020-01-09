package com.service;

import com.domain.Terceiro;
import com.repository.TerceiroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Terceiro.
 */
@Service
@Transactional
public class TerceiroService {

    private final Logger log = LoggerFactory.getLogger(TerceiroService.class);

    private final TerceiroRepository terceiroRepository;

    public TerceiroService(TerceiroRepository terceiroRepository) {
        this.terceiroRepository = terceiroRepository;
    }

    /**
     * Save a terceiro.
     *
     * @param terceiro the entity to save
     * @return the persisted entity
     */
    public Terceiro save(Terceiro terceiro) {
        log.debug("Request to save Terceiro : {}", terceiro);
        return terceiroRepository.save(terceiro);
    }

    /**
     * Get all the terceiros.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Terceiro> findAll() {
        log.debug("Request to get all Terceiros");
        return terceiroRepository.findAll();
    }

    /**
     * Get one terceiro by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Terceiro findOne(Long id) {
        log.debug("Request to get Terceiro : {}", id);
        return terceiroRepository.findOne(id);
    }

    /**
     * Delete the terceiro by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Terceiro : {}", id);
        terceiroRepository.delete(id);
    }
}
