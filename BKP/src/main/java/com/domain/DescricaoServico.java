package com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.domain.enumeration.TipoServico;

/**
 * A DescricaoServico.
 */
@Entity
@Table(name = "descricao_servico")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DescricaoServico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoServico tipo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public DescricaoServico nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoServico getTipo() {
        return tipo;
    }

    public DescricaoServico tipo(TipoServico tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoServico tipo) {
        this.tipo = tipo;
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
        DescricaoServico descricaoServico = (DescricaoServico) o;
        if (descricaoServico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), descricaoServico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DescricaoServico{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
