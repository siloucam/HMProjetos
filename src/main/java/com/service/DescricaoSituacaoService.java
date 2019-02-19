package com.service;

import com.domain.DescricaoSituacao;
import com.repository.DescricaoSituacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing DescricaoSituacao.
 */
@Service
@Transactional
public class DescricaoSituacaoService {

    private final Logger log = LoggerFactory.getLogger(DescricaoSituacaoService.class);

    private final DescricaoSituacaoRepository descricaoSituacaoRepository;

    public DescricaoSituacaoService(DescricaoSituacaoRepository descricaoSituacaoRepository) {
        this.descricaoSituacaoRepository = descricaoSituacaoRepository;
    }

    /**
     * Save a descricaoSituacao.
     *
     * @param descricaoSituacao the entity to save
     * @return the persisted entity
     */
    public DescricaoSituacao save(DescricaoSituacao descricaoSituacao) {
        log.debug("Request to save DescricaoSituacao : {}", descricaoSituacao);
        return descricaoSituacaoRepository.save(descricaoSituacao);
    }

    /**
     * Get all the descricaoSituacaos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<DescricaoSituacao> findAll() {
        log.debug("Request to get all DescricaoSituacaos");
        return descricaoSituacaoRepository.findAll();
    }

    /**
     * Get one descricaoSituacao by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DescricaoSituacao findOne(Long id) {
        log.debug("Request to get DescricaoSituacao : {}", id);
        return descricaoSituacaoRepository.findOne(id);
    }

    /**
     * Delete the descricaoSituacao by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DescricaoSituacao : {}", id);
        descricaoSituacaoRepository.delete(id);
    }
}
