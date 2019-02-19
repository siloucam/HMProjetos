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

import com.domain.Telefone;
import com.domain.*; // for static metamodels
import com.repository.TelefoneRepository;
import com.service.dto.TelefoneCriteria;


/**
 * Service for executing complex queries for Telefone entities in the database.
 * The main input is a {@link TelefoneCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Telefone} or a {@link Page} of {@link Telefone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TelefoneQueryService extends QueryService<Telefone> {

    private final Logger log = LoggerFactory.getLogger(TelefoneQueryService.class);


    private final TelefoneRepository telefoneRepository;

    public TelefoneQueryService(TelefoneRepository telefoneRepository) {
        this.telefoneRepository = telefoneRepository;
    }

    /**
     * Return a {@link List} of {@link Telefone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Telefone> findByCriteria(TelefoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Telefone> specification = createSpecification(criteria);
        return telefoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Telefone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Telefone> findByCriteria(TelefoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Telefone> specification = createSpecification(criteria);
        return telefoneRepository.findAll(specification, page);
    }

    /**
     * Function to convert TelefoneCriteria to a {@link Specifications}
     */
    private Specifications<Telefone> createSpecification(TelefoneCriteria criteria) {
        Specifications<Telefone> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Telefone_.id));
            }
            if (criteria.getContato() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContato(), Telefone_.contato));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Telefone_.numero));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClienteId(), Telefone_.cliente, Cliente_.id));
            }
        }
        return specification;
    }

}
