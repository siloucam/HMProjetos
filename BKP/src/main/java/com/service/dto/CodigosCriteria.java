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
 * Criteria class for the Codigos entity. This class is used in CodigosResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /codigos?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CodigosCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter tipo;

    private IntegerFilter ano;

    private IntegerFilter numero;

    public CodigosCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTipo() {
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public IntegerFilter getAno() {
        return ano;
    }

    public void setAno(IntegerFilter ano) {
        this.ano = ano;
    }

    public IntegerFilter getNumero() {
        return numero;
    }

    public void setNumero(IntegerFilter numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "CodigosCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (ano != null ? "ano=" + ano + ", " : "") +
                (numero != null ? "numero=" + numero + ", " : "") +
            "}";
    }

}
