package com.service;

import com.domain.Servico;
import com.repository.ServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Servico.
 */
@Service
@Transactional
public class ServicoService {

    private final Logger log = LoggerFactory.getLogger(ServicoService.class);

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    /**
     * Save a servico.
     *
     * @param servico the entity to save
     * @return the persisted entity
     */
    public Servico save(Servico servico) {
        log.debug("Request to save Servico : {}", servico);
        return servicoRepository.save(servico);
    }

    /**
     * Get all the servicos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Servico> findAll(Pageable pageable) {
        log.debug("Request to get all Servicos");
        return servicoRepository.findAll(pageable);
    }

    /**
     * Get one servico by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Servico findOne(Long id) {
        log.debug("Request to get Servico : {}", id);
        return servicoRepository.findOne(id);
    }

    /**
     * Delete the servico by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Servico : {}", id);
        servicoRepository.delete(id);
    }
}
