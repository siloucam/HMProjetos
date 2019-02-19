package com.service.dto;

import java.io.Serializable;
import com.domain.enumeration.OP;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Transacao entity. This class is used in TransacaoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /transacaos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransacaoCriteria implements Serializable {
    /**
     * Class for filtering OP
     */
    public static class OPFilter extends Filter<OP> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private FloatFilter valor;

    private LocalDateFilter data;

    private StringFilter descricao;

    private OPFilter operacao;

    private LongFilter servicoId;

    public TransacaoCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getValor() {
        return valor;
    }

    public void setValor(FloatFilter valor) {
        this.valor = valor;
    }

    public LocalDateFilter getData() {
        return data;
    }

    public void setData(LocalDateFilter data) {
        this.data = data;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public OPFilter getOperacao() {
        return operacao;
    }

    public void setOperacao(OPFilter operacao) {
        this.operacao = operacao;
    }

    public LongFilter getServicoId() {
        return servicoId;
    }

    public void setServicoId(LongFilter servicoId) {
        this.servicoId = servicoId;
    }

    @Override
    public String toString() {
        return "TransacaoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (valor != null ? "valor=" + valor + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
                (operacao != null ? "operacao=" + operacao + ", " : "") +
                (servicoId != null ? "servicoId=" + servicoId + ", " : "") +
            "}";
    }

}
