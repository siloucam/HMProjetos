package com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.domain.enumeration.OP;

/**
 * A Transacao.
 */
@Entity
@Table(name = "transacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Float valor;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operacao", nullable = false)
    private OP operacao;

    @ManyToOne
    private Servico servico;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValor() {
        return valor;
    }

    public Transacao valor(Float valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public Transacao data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public Transacao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OP getOperacao() {
        return operacao;
    }

    public Transacao operacao(OP operacao) {
        this.operacao = operacao;
        return this;
    }

    public void setOperacao(OP operacao) {
        this.operacao = operacao;
    }

    public Servico getServico() {
        return servico;
    }

    public Transacao servico(Servico servico) {
        this.servico = servico;
        return this;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
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
        Transacao transacao = (Transacao) o;
        if (transacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transacao{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", data='" + getData() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", operacao='" + getOperacao() + "'" +
            "}";
    }
}
