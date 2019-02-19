package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.domain.enumeration.TipoServico;

/**
 * A Servico.
 */
@Entity
@Table(name = "servico")
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoServico tipo;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "valor")
    private Float valor;

    @Column(name = "forma")
    private String forma;

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

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "fim")
    private LocalDate fim;

    @OneToMany(mappedBy = "servico")
    @JsonIgnore
    private Set<CodigoPrefeitura> codprefeituras = new HashSet<>();

    @OneToMany(mappedBy = "servico")
    @JsonIgnore
    private Set<LinkExterno> linkexternos = new HashSet<>();

    @OneToMany(mappedBy = "servico")
    @JsonIgnore
    private Set<Situacao> situacaos = new HashSet<>();

    @OneToMany(mappedBy = "servico")
    @JsonIgnore
    private Set<Transacao> transacaos = new HashSet<>();

    @ManyToOne
    private DescricaoServico descricao;

    @ManyToOne
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoServico getTipo() {
        return tipo;
    }

    public Servico tipo(TipoServico tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoServico tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Servico codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getObservacao() {
        return observacao;
    }

    public Servico observacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Float getValor() {
        return valor;
    }

    public Servico valor(Float valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getForma() {
        return forma;
    }

    public Servico forma(String forma) {
        this.forma = forma;
        return this;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getEndereco() {
        return endereco;
    }

    public Servico endereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public Servico bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public Servico cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public Servico estado(String estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public Servico cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public Servico inicio(LocalDate inicio) {
        this.inicio = inicio;
        return this;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public Servico fim(LocalDate fim) {
        this.fim = fim;
        return this;
    }

    public void setFim(LocalDate fim) {
        this.fim = fim;
    }

    public Set<CodigoPrefeitura> getCodprefeituras() {
        return codprefeituras;
    }

    public Servico codprefeituras(Set<CodigoPrefeitura> codigoPrefeituras) {
        this.codprefeituras = codigoPrefeituras;
        return this;
    }

    public Servico addCodprefeitura(CodigoPrefeitura codigoPrefeitura) {
        this.codprefeituras.add(codigoPrefeitura);
        codigoPrefeitura.setServico(this);
        return this;
    }

    public Servico removeCodprefeitura(CodigoPrefeitura codigoPrefeitura) {
        this.codprefeituras.remove(codigoPrefeitura);
        codigoPrefeitura.setServico(null);
        return this;
    }

    public void setCodprefeituras(Set<CodigoPrefeitura> codigoPrefeituras) {
        this.codprefeituras = codigoPrefeituras;
    }

    public Set<LinkExterno> getLinkexternos() {
        return linkexternos;
    }

    public Servico linkexternos(Set<LinkExterno> linkExternos) {
        this.linkexternos = linkExternos;
        return this;
    }

    public Servico addLinkexterno(LinkExterno linkExterno) {
        this.linkexternos.add(linkExterno);
        linkExterno.setServico(this);
        return this;
    }

    public Servico removeLinkexterno(LinkExterno linkExterno) {
        this.linkexternos.remove(linkExterno);
        linkExterno.setServico(null);
        return this;
    }

    public void setLinkexternos(Set<LinkExterno> linkExternos) {
        this.linkexternos = linkExternos;
    }

    public Set<Situacao> getSituacaos() {
        return situacaos;
    }

    public Servico situacaos(Set<Situacao> situacaos) {
        this.situacaos = situacaos;
        return this;
    }

    public Servico addSituacao(Situacao situacao) {
        this.situacaos.add(situacao);
        situacao.setServico(this);
        return this;
    }

    public Servico removeSituacao(Situacao situacao) {
        this.situacaos.remove(situacao);
        situacao.setServico(null);
        return this;
    }

    public void setSituacaos(Set<Situacao> situacaos) {
        this.situacaos = situacaos;
    }

    public Set<Transacao> getTransacaos() {
        return transacaos;
    }

    public Servico transacaos(Set<Transacao> transacaos) {
        this.transacaos = transacaos;
        return this;
    }

    public Servico addTransacao(Transacao transacao) {
        this.transacaos.add(transacao);
        transacao.setServico(this);
        return this;
    }

    public Servico removeTransacao(Transacao transacao) {
        this.transacaos.remove(transacao);
        transacao.setServico(null);
        return this;
    }

    public void setTransacaos(Set<Transacao> transacaos) {
        this.transacaos = transacaos;
    }

    public DescricaoServico getDescricao() {
        return descricao;
    }

    public Servico descricao(DescricaoServico descricaoServico) {
        this.descricao = descricaoServico;
        return this;
    }

    public void setDescricao(DescricaoServico descricaoServico) {
        this.descricao = descricaoServico;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Servico cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        Servico servico = (Servico) o;
        if (servico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Servico{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", valor=" + getValor() +
            ", forma='" + getForma() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cep='" + getCep() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            "}";
    }
}
