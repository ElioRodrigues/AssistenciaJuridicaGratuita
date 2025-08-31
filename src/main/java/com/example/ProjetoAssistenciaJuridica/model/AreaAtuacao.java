package com.example.ProjetoAssistenciaJuridica.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "area_atuacao")
public class AreaAtuacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;


    @ManyToMany(mappedBy = "areasAtuacao")
    private Set<Advogado> advogados;


    public AreaAtuacao() {
    }

    public AreaAtuacao(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Advogado> getAdvogados() {
        return advogados;
    }

    public void setAdvogados(Set<Advogado> advogados) {
        this.advogados = advogados;
    }


    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaAtuacao that = (AreaAtuacao) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }
}
