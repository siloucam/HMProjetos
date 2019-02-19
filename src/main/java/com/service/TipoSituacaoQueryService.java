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

import com.domain.TipoSituacao;
import com.domain.*; // for static metamodels
import com.repository.TipoSituacaoRepository;
import com.service.dto.TipoSituacaoCriteria;


/**
 * Service for executing complex queries for TipoSituacao entities in the database.
 * The main input is a {@link TipoSituacaoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoSituacao} or a {@link Page} of {@link TipoSituacao} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoSituacaoQueryService extends QueryService<TipoSituacao> {

    private final Logger log = LoggerFactory.getLogger(TipoSituacaoQueryService.class);


    private final TipoSituacaoRepository tipoSituacaoRepository;

    public TipoSituacaoQueryService(TipoSituacaoRepository tipoSituacaoRepository) {
        this.tipoSituacaoRepository = tipoSituacaoRepository;
    }

    /**
     * Return a {@link List} of {@link TipoSituacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoSituacao> findByCriteria(TipoSituacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TipoSituacao> specification = createSpecification(criteria);
        return tipoSituacaoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TipoSituacao} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoSituacao> findByCriteria(TipoSituacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TipoSituacao> specification = createSpecification(criteria);
        return tipoSituacaoRepository.findAll(specification, page);
    }

    /**
     * Function to convert TipoSituacaoCriteria to a {@link Specifications}
     */
    private Specifications<TipoSituacao> createSpecification(TipoSituacaoCriteria criteria) {
        Specifications<TipoSituacao> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TipoSituacao_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), TipoSituacao_.nome));
            }
            if (criteria.getSigla() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSigla(), TipoSituacao_.sigla));
            }
        }
        return specification;
    }

}
