package com.service;

import com.domain.TipoSituacao;
import com.repository.TipoSituacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing TipoSituacao.
 */
@Service
@Transactional
public class TipoSituacaoService {

    private final Logger log = LoggerFactory.getLogger(TipoSituacaoService.class);

    private final TipoSituacaoRepository tipoSituacaoRepository;

    public TipoSituacaoService(TipoSituacaoRepository tipoSituacaoRepository) {
        this.tipoSituacaoRepository = tipoSituacaoRepository;
    }

    /**
     * Save a tipoSituacao.
     *
     * @param tipoSituacao the entity to save
     * @return the persisted entity
     */
    public TipoSituacao save(TipoSituacao tipoSituacao) {
        log.debug("Request to save TipoSituacao : {}", tipoSituacao);
        return tipoSituacaoRepository.save(tipoSituacao);
    }

    /**
     * Get all the tipoSituacaos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TipoSituacao> findAll() {
        log.debug("Request to get all TipoSituacaos");
        return tipoSituacaoRepository.findAll();
    }

    /**
     * Get one tipoSituacao by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TipoSituacao findOne(Long id) {
        log.debug("Request to get TipoSituacao : {}", id);
        return tipoSituacaoRepository.findOne(id);
    }

    /**
     * Delete the tipoSituacao by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TipoSituacao : {}", id);
        tipoSituacaoRepository.delete(id);
    }
}
