package com.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Situacao entity. This class is used in SituacaoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /situacaos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SituacaoCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter observacao;

    private StringFilter terceiro;

    private LocalDateFilter dtinicio;

    private LocalDateFilter dtfim;

    private LocalDateFilter dtestipulada;

    private LongFilter descricaoId;

    private LongFilter tipoId;

    private LongFilter servicoId;

    private LongFilter responsavelId;

    public SituacaoCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getObservacao() {
        return observacao;
    }

    public void setObservacao(StringFilter observacao) {
        this.observacao = observacao;
    }

    public StringFilter getTerceiro() {
        return terceiro;
    }

    public void setTerceiro(StringFilter terceiro) {
        this.terceiro = terceiro;
    }

    public LocalDateFilter getDtinicio() {
        return dtinicio;
    }

    public void setDtinicio(LocalDateFilter dtinicio) {
        this.dtinicio = dtinicio;
    }

    public LocalDateFilter getDtfim() {
        return dtfim;
    }

    public void setDtfim(LocalDateFilter dtfim) {
        this.dtfim = dtfim;
    }

    public LocalDateFilter getDtestipulada() {
        return dtestipulada;
    }

    public void setDtestipulada(LocalDateFilter dtestipulada) {
        this.dtestipulada = dtestipulada;
    }

    public LongFilter getDescricaoId() {
        return descricaoId;
    }

    public void setDescricaoId(LongFilter descricaoId) {
        this.descricaoId = descricaoId;
    }

    public LongFilter getTipoId() {
        return tipoId;
    }

    public void setTipoId(LongFilter tipoId) {
        this.tipoId = tipoId;
    }

    public LongFilter getServicoId() {
        return servicoId;
    }

    public void setServicoId(LongFilter servicoId) {
        this.servicoId = servicoId;
    }

    public LongFilter getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(LongFilter responsavelId) {
        this.responsavelId = responsavelId;
    }

    @Override
    public String toString() {
        return "SituacaoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (observacao != null ? "observacao=" + observacao + ", " : "") +
                (terceiro != null ? "terceiro=" + terceiro + ", " : "") +
                (dtinicio != null ? "dtinicio=" + dtinicio + ", " : "") +
                (dtfim != null ? "dtfim=" + dtfim + ", " : "") +
                (dtestipulada != null ? "dtestipulada=" + dtestipulada + ", " : "") +
                (descricaoId != null ? "descricaoId=" + descricaoId + ", " : "") +
                (tipoId != null ? "tipoId=" + tipoId + ", " : "") +
                (servicoId != null ? "servicoId=" + servicoId + ", " : "") +
                (responsavelId != null ? "responsavelId=" + responsavelId + ", " : "") +
            "}";
    }

}
