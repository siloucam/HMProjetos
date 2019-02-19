package com.service;

import com.domain.LinkExterno;
import com.repository.LinkExternoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing LinkExterno.
 */
@Service
@Transactional
public class LinkExternoService {

    private final Logger log = LoggerFactory.getLogger(LinkExternoService.class);

    private final LinkExternoRepository linkExternoRepository;

    public LinkExternoService(LinkExternoRepository linkExternoRepository) {
        this.linkExternoRepository = linkExternoRepository;
    }

    /**
     * Save a linkExterno.
     *
     * @param linkExterno the entity to save
     * @return the persisted entity
     */
    public LinkExterno save(LinkExterno linkExterno) {
        log.debug("Request to save LinkExterno : {}", linkExterno);
        return linkExternoRepository.save(linkExterno);
    }

    /**
     * Get all the linkExternos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<LinkExterno> findAll() {
        log.debug("Request to get all LinkExternos");
        return linkExternoRepository.findAll();
    }

    /**
     * Get one linkExterno by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LinkExterno findOne(Long id) {
        log.debug("Request to get LinkExterno : {}", id);
        return linkExternoRepository.findOne(id);
    }

    /**
     * Delete the linkExterno by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LinkExterno : {}", id);
        linkExternoRepository.delete(id);
    }
}
