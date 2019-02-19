package com.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.domain.DescricaoSituacao;
import com.domain.*; // for static metamodels
import com.repository.DescricaoSituacaoRepository;
import com.service.dto.DescricaoSituacaoCriteria;


/**
 * Service for executing complex queries for DescricaoSituacao entities in the database.
 * The main input is a {@link DescricaoSituacaoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DescricaoSituacao} or a {@link Page} of {@link DescricaoSituacao} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DescricaoSituacaoQueryService extends QueryService<DescricaoSituacao> {

    private final Logger log = LoggerFactory.getLogger(DescricaoSituacaoQueryService.class);


    private final DescricaoSituacaoRepository descricaoSituacaoRepository;

    public DescricaoSituacaoQueryService(DescricaoSituacaoRepository descricaoSituacaoRepository) {
        this.descricaoSituacaoRepository = descricaoSituacaoRepository;
    }

    /**
     * Return a {@link List} of {@link DescricaoSituacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DescricaoSituacao> findByCriteria(DescricaoSituacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<DescricaoSituacao> specification = createSpecification(criteria);
        return descricaoSituacaoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DescricaoSituacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DescricaoSituacao> findByCriteria(DescricaoSituacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<DescricaoSituacao> specification = createSpecification(criteria);
        return descricaoSituacaoRepository.findAll(specification, page);
    }

    /**
     * Function to convert DescricaoSituacaoCriteria to a {@link Specifications}
     */
    private Specifications<DescricaoSituacao> createSpecification(DescricaoSituacaoCriteria criteria) {
        Specifications<DescricaoSituacao> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DescricaoSituacao_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), DescricaoSituacao_.nome));
            }
            if (criteria.getSituacaoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSituacaoId(), DescricaoSituacao_.situacao, TipoSituacao_.id));
            }
        }
        return specification;
    }

}
