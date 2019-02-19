package com.service;

import com.domain.MeuServico;
import com.repository.MeuServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing MeuServico.
 */
@Service
@Transactional
public class MeuServicoService {

    private final Logger log = LoggerFactory.getLogger(MeuServicoService.class);

    private final MeuServicoRepository meuServicoRepository;

    public MeuServicoService(MeuServicoRepository meuServicoRepository) {
        this.meuServicoRepository = meuServicoRepository;
    }

    /**
     * Save a meuServico.
     *
     * @param meuServico the entity to save
     * @return the persisted entity
     */
    public MeuServico save(MeuServico meuServico) {
        log.debug("Request to save MeuServico : {}", meuServico);
        return meuServicoRepository.save(meuServico);
    }

    /**
     * Get all the meuServicos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MeuServico> findAll() {
        log.debug("Request to get all MeuServicos");
        return meuServicoRepository.findAll();
    }

    /**
     * Get one meuServico by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MeuServico findOne(Long id) {
        log.debug("Request to get MeuServico : {}", id);
        return meuServicoRepository.findOne(id);
    }

    /**
     * Delete the meuServico by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeuServico : {}", id);
        meuServicoRepository.delete(id);
    }
}
