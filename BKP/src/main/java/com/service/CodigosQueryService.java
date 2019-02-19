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

import com.domain.Codigos;
import com.domain.*; // for static metamodels
import com.repository.CodigosRepository;
import com.service.dto.CodigosCriteria;


/**
 * Service for executing complex queries for Codigos entities in the database.
 * The main input is a {@link CodigosCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Codigos} or a {@link Page} of {@link Codigos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CodigosQueryService extends QueryService<Codigos> {

    private final Logger log = LoggerFactory.getLogger(CodigosQueryService.class);


    private final CodigosRepository codigosRepository;

    public CodigosQueryService(CodigosRepository codigosRepository) {
        this.codigosRepository = codigosRepository;
    }

    /**
     * Return a {@link List} of {@link Codigos} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Codigos> findByCriteria(CodigosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Codigos> specification = createSpecification(criteria);
        return codigosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Codigos} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Codigos> findByCriteria(CodigosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Codigos> specification = createSpecification(criteria);
        return codigosRepository.findAll(specification, page);
    }

    /**
     * Function to convert CodigosCriteria to a {@link Specifications}
     */
    private Specifications<Codigos> createSpecification(CodigosCriteria criteria) {
        Specifications<Codigos> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Codigos_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Codigos_.tipo));
            }
            if (criteria.getAno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAno(), Codigos_.ano));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumero(), Codigos_.numero));
            }
        }
        return specification;
    }

}
