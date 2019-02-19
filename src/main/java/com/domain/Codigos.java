package com.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Codigos.
 */
@Entity
@Table(name = "codigos")
public class Codigos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "numero")
    private Integer numero;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public Codigos tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getAno() {
        return ano;
    }

    public Codigos ano(Integer ano) {
        this.ano = ano;
        return this;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public Codigos numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
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
        Codigos codigos = (Codigos) o;
        if (codigos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), codigos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Codigos{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", ano=" + getAno() +
            ", numero=" + getNumero() +
            "}";
    }
}
