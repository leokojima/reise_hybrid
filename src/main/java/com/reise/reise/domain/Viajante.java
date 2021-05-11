package com.reise.reise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reise.reise.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Viajante.
 */
@Entity
@Table(name = "viajante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Viajante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "statusv")
    private Status statusv;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_viajante__local",
        joinColumns = @JoinColumn(name = "viajante_id"),
        inverseJoinColumns = @JoinColumn(name = "local_id")
    )
    @JsonIgnoreProperties(value = { "viajantes" }, allowSetters = true)
    private Set<Local> locals = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_viajante__roteiro",
        joinColumns = @JoinColumn(name = "viajante_id"),
        inverseJoinColumns = @JoinColumn(name = "roteiro_id")
    )
    @JsonIgnoreProperties(value = { "viajantes" }, allowSetters = true)
    private Set<Roteiro> roteiros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Viajante id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Viajante nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public Viajante email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Viajante foto(byte[] foto) {
        this.foto = foto;
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Viajante fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Viajante dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Status getStatusv() {
        return this.statusv;
    }

    public Viajante statusv(Status statusv) {
        this.statusv = statusv;
        return this;
    }

    public void setStatusv(Status statusv) {
        this.statusv = statusv;
    }

    public Set<Local> getLocals() {
        return this.locals;
    }

    public Viajante locals(Set<Local> locals) {
        this.setLocals(locals);
        return this;
    }

    public Viajante addLocal(Local local) {
        this.locals.add(local);
        local.getViajantes().add(this);
        return this;
    }

    public Viajante removeLocal(Local local) {
        this.locals.remove(local);
        local.getViajantes().remove(this);
        return this;
    }

    public void setLocals(Set<Local> locals) {
        this.locals = locals;
    }

    public Set<Roteiro> getRoteiros() {
        return this.roteiros;
    }

    public Viajante roteiros(Set<Roteiro> roteiros) {
        this.setRoteiros(roteiros);
        return this;
    }

    public Viajante addRoteiro(Roteiro roteiro) {
        this.roteiros.add(roteiro);
        roteiro.getViajantes().add(this);
        return this;
    }

    public Viajante removeRoteiro(Roteiro roteiro) {
        this.roteiros.remove(roteiro);
        roteiro.getViajantes().remove(this);
        return this;
    }

    public void setRoteiros(Set<Roteiro> roteiros) {
        this.roteiros = roteiros;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Viajante)) {
            return false;
        }
        return id != null && id.equals(((Viajante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Viajante{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", statusv='" + getStatusv() + "'" +
            "}";
    }
}
