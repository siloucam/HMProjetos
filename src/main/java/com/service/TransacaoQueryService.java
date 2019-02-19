package com.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.domain.Transacao;
import com.domain.*; // for static metamodels
import com.repository.TransacaoRepository;
import com.service.dto.TransacaoCriteria;

import com.domain.enumeration.OP;

/**
 * Service for executing complex queries for Transacao entities in the database.
 * The main input is a {@link TransacaoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Transacao} or a {@link Page} of {@link Transacao} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransacaoQueryService extends QueryService<Transacao> {

    private final Logger log = LoggerFactory.getLogger(TransacaoQueryService.class);


    private final TransacaoRepository transacaoRepository;

    public TransacaoQueryService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    /**
     * Return a {@link List} of {@link Transacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Transacao> findByCriteria(TransacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Transacao> specification = createSpecification(criteria);
        return transacaoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Transacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Transacao> findByCriteria(TransacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Transacao> specification = createSpecification(criteria);
        return transacaoRepository.findAll(specification, page);
    }

    /**
     * Function to convert TransacaoCriteria to a {@link Specifications}
     */
    private Specifications<Transacao> createSpecification(TransacaoCriteria criteria) {
        Specifications<Transacao> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Transacao_.id));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Transacao_.valor));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Transacao_.data));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Transacao_.descricao));
            }
            if (criteria.getOperacao() != null) {
                specification = specification.and(buildSpecification(criteria.getOperacao(), Transacao_.operacao));
            }
            if (criteria.getServicoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getServicoId(), Transacao_.servico, Servico_.id));
            }
        }
        return specification;
    }

}
