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

import com.domain.LinkExterno;
import com.domain.*; // for static metamodels
import com.repository.LinkExternoRepository;
import com.service.dto.LinkExternoCriteria;


/**
 * Service for executing complex queries for LinkExterno entities in the database.
 * The main input is a {@link LinkExternoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LinkExterno} or a {@link Page} of {@link LinkExterno} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LinkExternoQueryService extends QueryService<LinkExterno> {

    private final Logger log = LoggerFactory.getLogger(LinkExternoQueryService.class);


    private final LinkExternoRepository linkExternoRepository;

    public LinkExternoQueryService(LinkExternoRepository linkExternoRepository) {
        this.linkExternoRepository = linkExternoRepository;
    }

    /**
     * Return a {@link List} of {@link LinkExterno} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LinkExterno> findByCriteria(LinkExternoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<LinkExterno> specification = createSpecification(criteria);
        return linkExternoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LinkExterno} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LinkExterno> findByCriteria(LinkExternoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<LinkExterno> specification = createSpecification(criteria);
        return linkExternoRepository.findAll(specification, page);
    }

    /**
     * Function to convert LinkExternoCriteria to a {@link Specifications}
     */
    private Specifications<LinkExterno> createSpecification(LinkExternoCriteria criteria) {
        Specifications<LinkExterno> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LinkExterno_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), LinkExterno_.nome));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), LinkExterno_.link));
            }
            if (criteria.getServicoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getServicoId(), LinkExterno_.servico, Servico_.id));
            }
        }
        return specification;
    }

}
