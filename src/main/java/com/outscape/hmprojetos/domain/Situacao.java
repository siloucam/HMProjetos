package com.outscape.hmprojetos.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.outscape.hmprojetos.domain.enumeration.TipoSituacao;

/**
 * A Situacao.
 */
@Entity
@Table(name = "situacao")
public class Situacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoSituacao tipo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "terceiro")
    private String terceiro;

    @Column(name = "dtinicio")
    private LocalDate dtinicio;

    @Column(name = "dtfim")
    private LocalDate dtfim;

    @Column(name = "dtestipulada")
    private LocalDate dtestipulada;

    @ManyToOne
    private Servico servico;

    @ManyToOne
    private ExtendUser responsavel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoSituacao getTipo() {
        return tipo;
    }

    public Situacao tipo(TipoSituacao tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoSituacao tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Situacao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTerceiro() {
        return terceiro;
    }

    public Situacao terceiro(String terceiro) {
        this.terceiro = terceiro;
        return this;
    }

    public void setTerceiro(String terceiro) {
        this.terceiro = terceiro;
    }

    public LocalDate getDtinicio() {
        return dtinicio;
    }

    public Situacao dtinicio(LocalDate dtinicio) {
        this.dtinicio = dtinicio;
        return this;
    }

    public void setDtinicio(LocalDate dtinicio) {
        this.dtinicio = dtinicio;
    }

    public LocalDate getDtfim() {
        return dtfim;
    }

    public Situacao dtfim(LocalDate dtfim) {
        this.dtfim = dtfim;
        return this;
    }

    public void setDtfim(LocalDate dtfim) {
        this.dtfim = dtfim;
    }

    public LocalDate getDtestipulada() {
        return dtestipulada;
    }

    public Situacao dtestipulada(LocalDate dtestipulada) {
        this.dtestipulada = dtestipulada;
        return this;
    }

    public void setDtestipulada(LocalDate dtestipulada) {
        this.dtestipulada = dtestipulada;
    }

    public Servico getServico() {
        return servico;
    }

    public Situacao servico(Servico servico) {
        this.servico = servico;
        return this;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public ExtendUser getResponsavel() {
        return responsavel;
    }

    public Situacao responsavel(ExtendUser extendUser) {
        this.responsavel = extendUser;
        return this;
    }

    public void setResponsavel(ExtendUser extendUser) {
        this.responsavel = extendUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Situacao situacao = (Situacao) o;
        if (situacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), situacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Situacao{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", terceiro='" + getTerceiro() + "'" +
            ", dtinicio='" + getDtinicio() + "'" +
            ", dtfim='" + getDtfim() + "'" +
            ", dtestipulada='" + getDtestipulada() + "'" +
            "}";
    }
}
