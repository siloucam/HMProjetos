package com.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Cliente entity. This class is used in ClienteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /clientes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClienteCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter nome;

    private StringFilter endereco;

    private StringFilter bairro;

    private StringFilter cidade;

    private StringFilter estado;

    private StringFilter cep;

    private StringFilter email;

    private StringFilter indicacao;

    private StringFilter documento;

    private LongFilter telefoneId;

    private LongFilter servicoId;

    public ClienteCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
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

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getIndicacao() {
        return indicacao;
    }

    public void setIndicacao(StringFilter indicacao) {
        this.indicacao = indicacao;
    }

    public StringFilter getDocumento() {
        return documento;
    }

    public void setDocumento(StringFilter documento) {
        this.documento = documento;
    }

    public LongFilter getTelefoneId() {
        return telefoneId;
    }

    public void setTelefoneId(LongFilter telefoneId) {
        this.telefoneId = telefoneId;
    }

    public LongFilter getServicoId() {
        return servicoId;
    }

    public void setServicoId(LongFilter servicoId) {
        this.servicoId = servicoId;
    }

    @Override
    public String toString() {
        return "ClienteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (endereco != null ? "endereco=" + endereco + ", " : "") +
                (bairro != null ? "bairro=" + bairro + ", " : "") +
                (cidade != null ? "cidade=" + cidade + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (cep != null ? "cep=" + cep + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (indicacao != null ? "indicacao=" + indicacao + ", " : "") +
                (documento != null ? "documento=" + documento + ", " : "") +
                (telefoneId != null ? "telefoneId=" + telefoneId + ", " : "") +
                (servicoId != null ? "servicoId=" + servicoId + ", " : "") +
            "}";
    }

}
