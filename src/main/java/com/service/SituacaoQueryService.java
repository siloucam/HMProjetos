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

import com.domain.Situacao;
import com.domain.*; // for static metamodels
import com.repository.SituacaoRepository;
import com.service.dto.SituacaoCriteria;


/**
 * Service for executing complex queries for Situacao entities in the database.
 * The main input is a {@link SituacaoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Situacao} or a {@link Page} of {@link Situacao} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SituacaoQueryService extends QueryService<Situacao> {

    private final Logger log = LoggerFactory.getLogger(SituacaoQueryService.class);


    private final SituacaoRepository situacaoRepository;

    public SituacaoQueryService(SituacaoRepository situacaoRepository) {
        this.situacaoRepository = situacaoRepository;
    }

    /**
     * Return a {@link List} of {@link Situacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Situacao> findByCriteria(SituacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Situacao> specification = createSpecification(criteria);
        return situacaoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Situacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Situacao> findByCriteria(SituacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Situacao> specification = createSpecification(criteria);
        return situacaoRepository.findAll(specification, page);
    }

    /**
     * Function to convert SituacaoCriteria to a {@link Specifications}
     */
    private Specifications<Situacao> createSpecification(SituacaoCriteria criteria) {
        Specifications<Situacao> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Situacao_.id));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Situacao_.observacao));
            }
            if (criteria.getTerceiro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTerceiro(), Situacao_.terceiro));
            }
            if (criteria.getDtinicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDtinicio(), Situacao_.dtinicio));
            }
            if (criteria.getDtfim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDtfim(), Situacao_.dtfim));
            }
            if (criteria.getDtestipulada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDtestipulada(), Situacao_.dtestipulada));
            }
            if (criteria.getDescricaoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDescricaoId(), Situacao_.descricao, DescricaoSituacao_.id));
            }
            if (criteria.getTipoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTipoId(), Situacao_.tipo, TipoSituacao_.id));
            }
            if (criteria.getServicoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getServicoId(), Situacao_.servico, Servico_.id));
            }
            if (criteria.getResponsavelId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResponsavelId(), Situacao_.responsavel, ExtendUser_.id));
            }
        }
        return specification;
    }

}
