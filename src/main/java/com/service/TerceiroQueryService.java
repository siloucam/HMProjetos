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

import com.domain.Terceiro;
import com.domain.*; // for static metamodels
import com.repository.TerceiroRepository;
import com.service.dto.TerceiroCriteria;


/**
 * Service for executing complex queries for Terceiro entities in the database.
 * The main input is a {@link TerceiroCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Terceiro} or a {@link Page} of {@link Terceiro} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TerceiroQueryService extends QueryService<Terceiro> {

    private final Logger log = LoggerFactory.getLogger(TerceiroQueryService.class);


    private final TerceiroRepository terceiroRepository;

    public TerceiroQueryService(TerceiroRepository terceiroRepository) {
        this.terceiroRepository = terceiroRepository;
    }

    /**
     * Return a {@link List} of {@link Terceiro} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Terceiro> findByCriteria(TerceiroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Terceiro> specification = createSpecification(criteria);
        return terceiroRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Terceiro} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Terceiro> findByCriteria(TerceiroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Terceiro> specification = createSpecification(criteria);
        return terceiroRepository.findAll(specification, page);
    }

    /**
     * Function to convert TerceiroCriteria to a {@link Specifications}
     */
    private Specifications<Terceiro> createSpecification(TerceiroCriteria criteria) {
        Specifications<Terceiro> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Terceiro_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Terceiro_.nome));
            }
        }
        return specification;
    }

}
