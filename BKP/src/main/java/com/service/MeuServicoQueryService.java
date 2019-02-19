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

import com.domain.MeuServico;
import com.domain.*; // for static metamodels
import com.repository.MeuServicoRepository;
import com.service.dto.MeuServicoCriteria;


/**
 * Service for executing complex queries for MeuServico entities in the database.
 * The main input is a {@link MeuServicoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MeuServico} or a {@link Page} of {@link MeuServico} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeuServicoQueryService extends QueryService<MeuServico> {

    private final Logger log = LoggerFactory.getLogger(MeuServicoQueryService.class);


    private final MeuServicoRepository meuServicoRepository;

    public MeuServicoQueryService(MeuServicoRepository meuServicoRepository) {
        this.meuServicoRepository = meuServicoRepository;
    }

    /**
     * Return a {@link List} of {@link MeuServico} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MeuServico> findByCriteria(MeuServicoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MeuServico> specification = createSpecification(criteria);
        return meuServicoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MeuServico} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MeuServico> findByCriteria(MeuServicoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MeuServico> specification = createSpecification(criteria);
        return meuServicoRepository.findAll(specification, page);
    }

    /**
     * Function to convert MeuServicoCriteria to a {@link Specifications}
     */
    private Specifications<MeuServico> createSpecification(MeuServicoCriteria criteria) {
        Specifications<MeuServico> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MeuServico_.id));
            }
        }
        return specification;
    }

}
