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

import com.domain.Servico;
import com.domain.*; // for static metamodels
import com.repository.ServicoRepository;
import com.service.dto.ServicoCriteria;

import com.domain.enumeration.TipoServico;

/**
 * Service for executing complex queries for Servico entities in the database.
 * The main input is a {@link ServicoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Servico} or a {@link Page} of {@link Servico} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicoQueryService extends QueryService<Servico> {

    private final Logger log = LoggerFactory.getLogger(ServicoQueryService.class);


    private final ServicoRepository servicoRepository;

    public ServicoQueryService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    /**
     * Return a {@link List} of {@link Servico} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Servico> findByCriteria(ServicoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Servico> specification = createSpecification(criteria);
        return servicoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Servico} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Servico> findByCriteria(ServicoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Servico> specification = createSpecification(criteria);
        return servicoRepository.findAll(specification, page);
    }

    /**
     * Function to convert ServicoCriteria to a {@link Specifications}
     */
    private Specifications<Servico> createSpecification(ServicoCriteria criteria) {
        Specifications<Servico> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Servico_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Servico_.tipo));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Servico_.codigo));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Servico_.observacao));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Servico_.valor));
            }
            if (criteria.getForma() != null) {
                specification = specification.and(buildStringSpecification(criteria.getForma(), Servico_.forma));
            }
            if (criteria.getEndereco() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndereco(), Servico_.endereco));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Servico_.bairro));
            }
            if (criteria.getCidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCidade(), Servico_.cidade));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Servico_.estado));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Servico_.cep));
            }
            if (criteria.getInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInicio(), Servico_.inicio));
            }
            if (criteria.getFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFim(), Servico_.fim));
            }
            if (criteria.getCodprefeituraId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCodprefeituraId(), Servico_.codprefeituras, CodigoPrefeitura_.id));
            }
            if (criteria.getLinkexternoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkexternoId(), Servico_.linkexternos, LinkExterno_.id));
            }
            if (criteria.getSituacaoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSituacaoId(), Servico_.situacaos, Situacao_.id));
            }
            if (criteria.getTransacaoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTransacaoId(), Servico_.transacaos, Transacao_.id));
            }
            if (criteria.getDescricaoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDescricaoId(), Servico_.descricao, DescricaoServico_.id));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClienteId(), Servico_.cliente, Cliente_.id));
            }
        }
        return specification;
    }

}
