package com.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A MeuServico.
 */
@Entity
@Table(name = "meu_servico")
public class MeuServico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        MeuServico meuServico = (MeuServico) o;
        if (meuServico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), meuServico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MeuServico{" +
            "id=" + getId() +
            "}";
    }
}
