package com.service;

import com.domain.Transacao;
import com.repository.TransacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Transacao.
 */
@Service
@Transactional
public class TransacaoService {

    private final Logger log = LoggerFactory.getLogger(TransacaoService.class);

    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * Save a transacao.
     *
     * @param transacao the entity to save
     * @return the persisted entity
     */
    public Transacao save(Transacao transacao) {
        log.debug("Request to save Transacao : {}", transacao);
        return transacaoRepository.save(transacao);
    }

    /**
     * Get all the transacaos.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Transacao> findAll() {
        log.debug("Request to get all Transacaos");
        return transacaoRepository.findAll();
    }

    /**
     * Get one transacao by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Transacao findOne(Long id) {
        log.debug("Request to get Transacao : {}", id);
        return transacaoRepository.findOne(id);
    }

    /**
     * Delete the transacao by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Transacao : {}", id);
        transacaoRepository.delete(id);
    }
}
