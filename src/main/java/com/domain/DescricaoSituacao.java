package com.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DescricaoSituacao.
 */
@Entity
@Table(name = "descricao_situacao")
public class DescricaoSituacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    private TipoSituacao situacao;

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

    public DescricaoSituacao nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoSituacao getSituacao() {
        return situacao;
    }

    public DescricaoSituacao situacao(TipoSituacao tipoSituacao) {
        this.situacao = tipoSituacao;
        return this;
    }

    public void setSituacao(TipoSituacao tipoSituacao) {
        this.situacao = tipoSituacao;
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
        DescricaoSituacao descricaoSituacao = (DescricaoSituacao) o;
        if (descricaoSituacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), descricaoSituacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DescricaoSituacao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
