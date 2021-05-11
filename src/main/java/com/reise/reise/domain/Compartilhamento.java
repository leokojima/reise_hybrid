package com.reise.reise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Compartilhamento.
 */
@Entity
@Table(name = "compartilhamento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compartilhamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome_comp", nullable = false)
    private String nomeComp;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao_comp")
    private String descricaoComp;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locals", "roteiros" }, allowSetters = true)
    private Viajante viajante;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Compartilhamento id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomeComp() {
        return this.nomeComp;
    }

    public Compartilhamento nomeComp(String nomeComp) {
        this.nomeComp = nomeComp;
        return this;
    }

    public void setNomeComp(String nomeComp) {
        this.nomeComp = nomeComp;
    }

    public String getDescricaoComp() {
        return this.descricaoComp;
    }

    public Compartilhamento descricaoComp(String descricaoComp) {
        this.descricaoComp = descricaoComp;
        return this;
    }

    public void setDescricaoComp(String descricaoComp) {
        this.descricaoComp = descricaoComp;
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public Compartilhamento dataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
        return this;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Viajante getViajante() {
        return this.viajante;
    }

    public Compartilhamento viajante(Viajante viajante) {
        this.setViajante(viajante);
        return this;
    }

    public void setViajante(Viajante viajante) {
        this.viajante = viajante;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compartilhamento)) {
            return false;
        }
        return id != null && id.equals(((Compartilhamento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compartilhamento{" +
            "id=" + getId() +
            ", nomeComp='" + getNomeComp() + "'" +
            ", descricaoComp='" + getDescricaoComp() + "'" +
            ", dataCriacao='" + getDataCriacao() + "'" +
            "}";
    }
}
