package com.outscape.hmprojetos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Size(max = 2)
    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "email")
    private String email;

    @Column(name = "indicacao")
    private String indicacao;

    @Column(name = "documento")
    private String documento;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Telefone> telefones = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Servico> servicos = new HashSet<>();

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

    public Cliente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public Cliente endereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public Cliente bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public Cliente cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public Cliente estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public Cliente cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public Cliente email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndicacao() {
        return indicacao;
    }

    public Cliente indicacao(String indicacao) {
        this.indicacao = indicacao;
        return this;
    }

    public void setIndicacao(String indicacao) {
        this.indicacao = indicacao;
    }

    public String getDocumento() {
        return documento;
    }

    public Cliente documento(String documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public Cliente telefones(Set<Telefone> telefones) {
        this.telefones = telefones;
        return this;
    }

    public Cliente addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setCliente(this);
        return this;
    }

    public Cliente removeTelefone(Telefone telefone) {
        this.telefones.remove(telefone);
        telefone.setCliente(null);
        return this;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Set<Servico> getServicos() {
        return servicos;
    }

    public Cliente servicos(Set<Servico> servicos) {
        this.servicos = servicos;
        return this;
    }

    public Cliente addServico(Servico servico) {
        this.servicos.add(servico);
        servico.setCliente(this);
        return this;
    }

    public Cliente removeServico(Servico servico) {
        this.servicos.remove(servico);
        servico.setCliente(null);
        return this;
    }

    public void setServicos(Set<Servico> servicos) {
        this.servicos = servicos;
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
        Cliente cliente = (Cliente) o;
        if (cliente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cep='" + getCep() + "'" +
            ", email='" + getEmail() + "'" +
            ", indicacao='" + getIndicacao() + "'" +
            ", documento='" + getDocumento() + "'" +
            "}";
    }
}
