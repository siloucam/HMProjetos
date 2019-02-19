package com.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A LinkExterno.
 */
@Entity
@Table(name = "link_externo")
public class LinkExterno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "jhi_link")
    private String link;

    @ManyToOne
    private Servico servico;

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

    public LinkExterno nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public LinkExterno link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Servico getServico() {
        return servico;
    }

    public LinkExterno servico(Servico servico) {
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
        LinkExterno linkExterno = (LinkExterno) o;
        if (linkExterno.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), linkExterno.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LinkExterno{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
