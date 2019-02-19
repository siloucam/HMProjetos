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

import com.domain.Cliente;
import com.domain.*; // for static metamodels
import com.repository.ClienteRepository;
import com.service.dto.ClienteCriteria;


/**
 * Service for executing complex queries for Cliente entities in the database.
 * The main input is a {@link ClienteCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cliente} or a {@link Page} of {@link Cliente} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClienteQueryService extends QueryService<Cliente> {

    private final Logger log = LoggerFactory.getLogger(ClienteQueryService.class);


    private final ClienteRepository clienteRepository;

    public ClienteQueryService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Return a {@link List} of {@link Cliente} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cliente> findByCriteria(ClienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cliente} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cliente> findByCriteria(ClienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification, page);
    }

    /**
     * Function to convert ClienteCriteria to a {@link Specifications}
     */
    private Specifications<Cliente> createSpecification(ClienteCriteria criteria) {
        Specifications<Cliente> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cliente_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Cliente_.nome));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), Cliente_.endereco));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Cliente_.bairro));
            }
            if (criteria.getCidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCidade(), Cliente_.cidade));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Cliente_.estado));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Cliente_.cep));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Cliente_.email));
            }
            if (criteria.getIndicacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndicacao(), Cliente_.indicacao));
            }
            if (criteria.getDocumento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumento(), Cliente_.documento));
            }
            if (criteria.getTelefoneId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTelefoneId(), Cliente_.telefones, Telefone_.id));
            }
            if (criteria.getServicoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getServicoId(), Cliente_.servicos, Servico_.id));
            }
        }
        return specification;
    }

}
