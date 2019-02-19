package com.service;

import com.domain.DescricaoServico;
import com.repository.DescricaoServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing DescricaoServico.
 */
@Service
@Transactional
public class DescricaoServicoService {

    private final Logger log = LoggerFactory.getLogger(DescricaoServicoService.class);

    private final DescricaoServicoRepository descricaoServicoRepository;

    public DescricaoServicoService(DescricaoServicoRepository descricaoServicoRepository) {
        this.descricaoServicoRepository = descricaoServicoRepository;
    }

    /**
     * Save a descricaoServico.
     *
     * @param descricaoServico the entity to save
     * @return the persisted entity
     */
    public DescricaoServico save(DescricaoServico descricaoServico) {
        log.debug("Request to save DescricaoServico : {}", descricaoServico);
        return descricaoServicoRepository.save(descricaoServico);
    }

    /**
     * Get all the descricaoServicos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DescricaoServico> findAll() {
        log.debug("Request to get all DescricaoServicos");
        return descricaoServicoRepository.findAll();
    }

    /**
     * Get one descricaoServico by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DescricaoServico findOne(Long id) {
        log.debug("Request to get DescricaoServico : {}", id);
        return descricaoServicoRepository.findOne(id);
    }

    /**
     * Delete the descricaoServico by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DescricaoServico : {}", id);
        descricaoServicoRepository.delete(id);
    }
}
