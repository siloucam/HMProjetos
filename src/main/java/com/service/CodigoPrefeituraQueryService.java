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

import com.domain.CodigoPrefeitura;
import com.domain.*; // for static metamodels
import com.repository.CodigoPrefeituraRepository;
import com.service.dto.CodigoPrefeituraCriteria;


/**
 * Service for executing complex queries for CodigoPrefeitura entities in the database.
 * The main input is a {@link CodigoPrefeituraCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CodigoPrefeitura} or a {@link Page} of {@link CodigoPrefeitura} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CodigoPrefeituraQueryService extends QueryService<CodigoPrefeitura> {

    private final Logger log = LoggerFactory.getLogger(CodigoPrefeituraQueryService.class);


    private final CodigoPrefeituraRepository codigoPrefeituraRepository;

    public CodigoPrefeituraQueryService(CodigoPrefeituraRepository codigoPrefeituraRepository) {
        this.codigoPrefeituraRepository = codigoPrefeituraRepository;
    }

    /**
     * Return a {@link List} of {@link CodigoPrefeitura} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CodigoPrefeitura> findByCriteria(CodigoPrefeituraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<CodigoPrefeitura> specification = createSpecification(criteria);
        return codigoPrefeituraRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CodigoPrefeitura} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CodigoPrefeitura> findByCriteria(CodigoPrefeituraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<CodigoPrefeitura> specification = createSpecification(criteria);
        return codigoPrefeituraRepository.findAll(specification, page);
    }

    /**
     * Function to convert CodigoPrefeituraCriteria to a {@link Specifications}
     */
    private Specifications<CodigoPrefeitura> createSpecification(CodigoPrefeituraCriteria criteria) {
        Specifications<CodigoPrefeitura> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CodigoPrefeitura_.id));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), CodigoPrefeitura_.numero));
            }
            if (criteria.getAno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAno(), CodigoPrefeitura_.ano));
            }
            if (criteria.getServicoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getServicoId(), CodigoPrefeitura_.servico, Servico_.id));
            }
        }
        return specification;
    }

}
