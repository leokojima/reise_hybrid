package com.reise.reise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reise.reise.domain.enumeration.Status;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Roteiro.
 */
@Entity
@Table(name = "roteiro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Roteiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nomeroteiro", nullable = false)
    private String nomeroteiro;

    @NotNull
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "statusr")
    private Status statusr;

    @ManyToMany(mappedBy = "roteiros")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locals", "roteiros" }, allowSetters = true)
    private Set<Viajante> viajantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Roteiro id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomeroteiro() {
        return this.nomeroteiro;
    }

    public Roteiro nomeroteiro(String nomeroteiro) {
        this.nomeroteiro = nomeroteiro;
        return this;
    }

    public void setNomeroteiro(String nomeroteiro) {
        this.nomeroteiro = nomeroteiro;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Roteiro tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Status getStatusr() {
        return this.statusr;
    }

    public Roteiro statusr(Status statusr) {
        this.statusr = statusr;
        return this;
    }

    public void setStatusr(Status statusr) {
        this.statusr = statusr;
    }

    public Set<Viajante> getViajantes() {
        return this.viajantes;
    }

    public Roteiro viajantes(Set<Viajante> viajantes) {
        this.setViajantes(viajantes);
        return this;
    }

    public Roteiro addViajante(Viajante viajante) {
        this.viajantes.add(viajante);
        viajante.getRoteiros().add(this);
        return this;
    }

    public Roteiro removeViajante(Viajante viajante) {
        this.viajantes.remove(viajante);
        viajante.getRoteiros().remove(this);
        return this;
    }

    public void setViajantes(Set<Viajante> viajantes) {
        if (this.viajantes != null) {
            this.viajantes.forEach(i -> i.removeRoteiro(this));
        }
        if (viajantes != null) {
            viajantes.forEach(i -> i.addRoteiro(this));
        }
        this.viajantes = viajantes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roteiro)) {
            return false;
        }
        return id != null && id.equals(((Roteiro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Roteiro{" +
            "id=" + getId() +
            ", nomeroteiro='" + getNomeroteiro() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", statusr='" + getStatusr() + "'" +
            "}";
    }
}
