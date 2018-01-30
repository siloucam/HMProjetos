package com.outscape.hmprojetos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.outscape.hmprojetos.domain.enumeration.StatusParcela;

/**
 * A Parcela.
 */
@Entity
@Table(name = "parcela")
public class Parcela implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusParcela status;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Float valor;

    @Column(name = "dtestipulada")
    private LocalDate dtestipulada;

    @Column(name = "dtefetuada")
    private LocalDate dtefetuada;

    @OneToOne(mappedBy = "parcela")
    @JsonIgnore
    private Transacao transacao;

    @ManyToOne
    private Orcamento orcamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Parcela descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusParcela getStatus() {
        return status;
    }

    public Parcela status(StatusParcela status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusParcela status) {
        this.status = status;
    }

    public Float getValor() {
        return valor;
    }

    public Parcela valor(Float valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public LocalDate getDtestipulada() {
        return dtestipulada;
    }

    public Parcela dtestipulada(LocalDate dtestipulada) {
        this.dtestipulada = dtestipulada;
        return this;
    }

    public void setDtestipulada(LocalDate dtestipulada) {
        this.dtestipulada = dtestipulada;
    }

    public LocalDate getDtefetuada() {
        return dtefetuada;
    }

    public Parcela dtefetuada(LocalDate dtefetuada) {
        this.dtefetuada = dtefetuada;
        return this;
    }

    public void setDtefetuada(LocalDate dtefetuada) {
        this.dtefetuada = dtefetuada;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public Parcela transacao(Transacao transacao) {
        this.transacao = transacao;
        return this;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public Parcela orcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
        return this;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
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
        Parcela parcela = (Parcela) o;
        if (parcela.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parcela.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Parcela{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", status='" + getStatus() + "'" +
            ", valor=" + getValor() +
            ", dtestipulada='" + getDtestipulada() + "'" +
            ", dtefetuada='" + getDtefetuada() + "'" +
            "}";
    }
}
