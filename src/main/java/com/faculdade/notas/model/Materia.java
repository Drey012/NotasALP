package com.faculdade.notas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MATERIA")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAT_INT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SEM_INT_ID", nullable = false)
    private Semestre semestre;

    @Column(name = "MAT_STR_NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "MAT_STR_SIGLA", length = 10, nullable = false)
    private String sigla;

    public Materia() {}

    public Materia(Semestre semestre, String nome, String sigla) {
        this.semestre = semestre;
        this.nome = nome;
        this.sigla = sigla;
    }

    public Long getId() { return id; }
    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }
}