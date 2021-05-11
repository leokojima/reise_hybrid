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
 * A Local.
 */
@Entity
@Table(name = "local")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Local implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nomelocal", nullable = false)
    private String nomelocal;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Column(name = "tipo")
    private String tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "statusl")
    private Status statusl;

    @ManyToMany(mappedBy = "locals")
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

    public Local id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomelocal() {
        return this.nomelocal;
    }

    public Local nomelocal(String nomelocal) {
        this.nomelocal = nomelocal;
        return this;
    }

    public void setNomelocal(String nomelocal) {
        this.nomelocal = nomelocal;
    }

    public String getEmail() {
        return this.email;
    }

    public Local email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Local foto(byte[] foto) {
        this.foto = foto;
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Local fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Local tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Status getStatusl() {
        return this.statusl;
    }

    public Local statusl(Status statusl) {
        this.statusl = statusl;
        return this;
    }

    public void setStatusl(Status statusl) {
        this.statusl = statusl;
    }

    public Set<Viajante> getViajantes() {
        return this.viajantes;
    }

    public Local viajantes(Set<Viajante> viajantes) {
        this.setViajantes(viajantes);
        return this;
    }

    public Local addViajante(Viajante viajante) {
        this.viajantes.add(viajante);
        viajante.getLocals().add(this);
        return this;
    }

    public Local removeViajante(Viajante viajante) {
        this.viajantes.remove(viajante);
        viajante.getLocals().remove(this);
        return this;
    }

    public void setViajantes(Set<Viajante> viajantes) {
        if (this.viajantes != null) {
            this.viajantes.forEach(i -> i.removeLocal(this));
        }
        if (viajantes != null) {
            viajantes.forEach(i -> i.addLocal(this));
        }
        this.viajantes = viajantes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Local)) {
            return false;
        }
        return id != null && id.equals(((Local) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Local{" +
            "id=" + getId() +
            ", nomelocal='" + getNomelocal() + "'" +
            ", email='" + getEmail() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", statusl='" + getStatusl() + "'" +
            "}";
    }
}
