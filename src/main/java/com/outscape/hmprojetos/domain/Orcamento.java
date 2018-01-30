package com.outscape.hmprojetos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Orcamento.
 */
@Entity
@Table(name = "orcamento")
public class Orcamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Float valor;

    @Column(name = "entrada")
    private Float entrada;

    @OneToOne
    @JoinColumn(unique = true)
    private Servico servico;

    @OneToMany(mappedBy = "orcamento")
    @JsonIgnore
    private Set<Parcela> parcelas = new HashSet<>();

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

    public Orcamento valor(Float valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Float getEntrada() {
        return entrada;
    }

    public Orcamento entrada(Float entrada) {
        this.entrada = entrada;
        return this;
    }

    public void setEntrada(Float entrada) {
        this.entrada = entrada;
    }

    public Servico getServico() {
        return servico;
    }

    public Orcamento servico(Servico servico) {
        this.servico = servico;
        return this;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Set<Parcela> getParcelas() {
        return parcelas;
    }

    public Orcamento parcelas(Set<Parcela> parcelas) {
        this.parcelas = parcelas;
        return this;
    }

    public Orcamento addParcela(Parcela parcela) {
        this.parcelas.add(parcela);
        parcela.setOrcamento(this);
        return this;
    }

    public Orcamento removeParcela(Parcela parcela) {
        this.parcelas.remove(parcela);
        parcela.setOrcamento(null);
        return this;
    }

    public void setParcelas(Set<Parcela> parcelas) {
        this.parcelas = parcelas;
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
        Orcamento orcamento = (Orcamento) o;
        if (orcamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orcamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orcamento{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", entrada=" + getEntrada() +
            "}";
    }
}
