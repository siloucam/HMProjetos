package com.service;

import com.domain.Situacao;
import com.repository.SituacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Situacao.
 */
@Service
@Transactional
public class SituacaoService {

    private final Logger log = LoggerFactory.getLogger(SituacaoService.class);

    private final SituacaoRepository situacaoRepository;

    public SituacaoService(SituacaoRepository situacaoRepository) {
        this.situacaoRepository = situacaoRepository;
    }

    /**
     * Save a situacao.
     *
     * @param situacao the entity to save
     * @return the persisted entity
     */
    public Situacao save(Situacao situacao) {
        log.debug("Request to save Situacao : {}", situacao);
        return situacaoRepository.save(situacao);
    }

    /**
     * Get all the situacaos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Situacao> findAll(Pageable pageable) {
        log.debug("Request to get all Situacaos");
        return situacaoRepository.findAll(pageable);
    }

    /**
     * Get one situacao by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Situacao findOne(Long id) {
        log.debug("Request to get Situacao : {}", id);
        return situacaoRepository.findOne(id);
    }

    /**
     * Delete the situacao by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Situacao : {}", id);
        situacaoRepository.delete(id);
    }
}
