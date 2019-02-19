package com.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CodigoPrefeitura.
 */
@Entity
@Table(name = "codigo_prefeitura")
public class CodigoPrefeitura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "ano")
    private String ano;

    @ManyToOne
    private Servico servico;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public CodigoPrefeitura numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAno() {
        return ano;
    }

    public CodigoPrefeitura ano(String ano) {
        this.ano = ano;
        return this;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Servico getServico() {
        return servico;
    }

    public CodigoPrefeitura servico(Servico servico) {
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
        CodigoPrefeitura codigoPrefeitura = (CodigoPrefeitura) o;
        if (codigoPrefeitura.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), codigoPrefeitura.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CodigoPrefeitura{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", ano='" + getAno() + "'" +
            "}";
    }
}
