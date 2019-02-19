package com.service.dto;

import java.io.Serializable;
import com.domain.enumeration.TipoServico;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Servico entity. This class is used in ServicoResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /servicos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServicoCriteria implements Serializable {
    /**
     * Class for filtering TipoServico
     */
    public static class TipoServicoFilter extends Filter<TipoServico> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private TipoServicoFilter tipo;

    private StringFilter codigo;

    private StringFilter observacao;

    private FloatFilter valor;

    private StringFilter forma;

    private StringFilter endereco;

    private StringFilter bairro;

    private StringFilter cidade;

    private StringFilter estado;

    private StringFilter cep;

    private LocalDateFilter inicio;

    private LocalDateFilter fim;

    private StringFilter iptu;

    private StringFilter link;

    private LongFilter situacaoId;

    private LongFilter transacaoId;

    private LongFilter descricaoId;

    private LongFilter clienteId;

    public ServicoCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TipoServicoFilter getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicoFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getCodigo() {
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public StringFilter getObservacao() {
        return observacao;
    }

    public void setObservacao(StringFilter observacao) {
        this.observacao = observacao;
    }

    public FloatFilter getValor() {
        return valor;
    }

    public void setValor(FloatFilter valor) {
        this.valor = valor;
    }

    public StringFilter getForma() {
        return forma;
    }

    public void setForma(StringFilter forma) {
        this.forma = forma;
    }

    public StringFilter getEndereco() {
        return endereco;
    }

    public void setEndereco(StringFilter endereco) {
        this.endereco = endereco;
    }

    public StringFilter getBairro() {
        return bairro;
    }

    public void setBairro(StringFilter bairro) {
        this.bairro = bairro;
    }

    public StringFilter getCidade() {
        return cidade;
    }

    public void setCidade(StringFilter cidade) {
        this.cidade = cidade;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public StringFilter getCep() {
        return cep;
    }

    public void setCep(StringFilter cep) {
        this.cep = cep;
    }

    public LocalDateFilter getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateFilter inicio) {
        this.inicio = inicio;
    }

    public LocalDateFilter getFim() {
        return fim;
    }

    public void setFim(LocalDateFilter fim) {
        this.fim = fim;
    }

    public StringFilter getIptu() {
        return iptu;
    }

    public void setIptu(StringFilter iptu) {
        this.iptu = iptu;
    }

    public StringFilter getLink() {
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public LongFilter getSituacaoId() {
        return situacaoId;
    }

    public void setSituacaoId(LongFilter situacaoId) {
        this.situacaoId = situacaoId;
    }

    public LongFilter getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(LongFilter transacaoId) {
        this.transacaoId = transacaoId;
    }

    public LongFilter getDescricaoId() {
        return descricaoId;
    }

    public void setDescricaoId(LongFilter descricaoId) {
        this.descricaoId = descricaoId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public String toString() {
        return "ServicoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (observacao != null ? "observacao=" + observacao + ", " : "") +
                (valor != null ? "valor=" + valor + ", " : "") +
                (forma != null ? "forma=" + forma + ", " : "") +
                (endereco != null ? "endereco=" + endereco + ", " : "") +
                (bairro != null ? "bairro=" + bairro + ", " : "") +
                (cidade != null ? "cidade=" + cidade + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (cep != null ? "cep=" + cep + ", " : "") +
                (inicio != null ? "inicio=" + inicio + ", " : "") +
                (fim != null ? "fim=" + fim + ", " : "") +
                (iptu != null ? "iptu=" + iptu + ", " : "") +
                (link != null ? "link=" + link + ", " : "") +
                (situacaoId != null ? "situacaoId=" + situacaoId + ", " : "") +
                (transacaoId != null ? "transacaoId=" + transacaoId + ", " : "") +
                (descricaoId != null ? "descricaoId=" + descricaoId + ", " : "") +
                (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
            "}";
    }

}
