package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.domain.enumeration.TipoCargo;

/**
 * A ExtendUser.
 */
@Entity
@Table(name = "extend_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExtendUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoCargo tipo;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "responsavel")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Situacao> situacaos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCargo getTipo() {
        return tipo;
    }

    public ExtendUser tipo(TipoCargo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoCargo tipo) {
        this.tipo = tipo;
    }

    public User getUser() {
        return user;
    }

    public ExtendUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Situacao> getSituacaos() {
        return situacaos;
    }

    public ExtendUser situacaos(Set<Situacao> situacaos) {
        this.situacaos = situacaos;
        return this;
    }

    public ExtendUser addSituacao(Situacao situacao) {
        this.situacaos.add(situacao);
        situacao.setResponsavel(this);
        return this;
    }

    public ExtendUser removeSituacao(Situacao situacao) {
        this.situacaos.remove(situacao);
        situacao.setResponsavel(null);
        return this;
    }

    public void setSituacaos(Set<Situacao> situacaos) {
        this.situacaos = situacaos;
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
        ExtendUser extendUser = (ExtendUser) o;
        if (extendUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), extendUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExtendUser{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
