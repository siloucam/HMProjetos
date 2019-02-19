package com.service.dto;

import java.io.Serializable;
import com.domain.enumeration.TipoCargo;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the ExtendUser entity. This class is used in ExtendUserResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /extend-users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtendUserCriteria implements Serializable {
    /**
     * Class for filtering TipoCargo
     */
    public static class TipoCargoFilter extends Filter<TipoCargo> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private TipoCargoFilter tipo;

    private LongFilter userId;

    private LongFilter situacaoId;

    public ExtendUserCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TipoCargoFilter getTipo() {
        return tipo;
    }

    public void setTipo(TipoCargoFilter tipo) {
        this.tipo = tipo;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getSituacaoId() {
        return situacaoId;
    }

    public void setSituacaoId(LongFilter situacaoId) {
        this.situacaoId = situacaoId;
    }

    @Override
    public String toString() {
        return "ExtendUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (situacaoId != null ? "situacaoId=" + situacaoId + ", " : "") +
            "}";
    }

}
