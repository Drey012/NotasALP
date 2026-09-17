package com.faculdade.notas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CURSO")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUR_INT_ID")
    private Long id;

    @Column(name = "CUR_STR_NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "CUR_STR_SIGLA", length = 20, nullable = false)
    private String sigla;

    public Curso() {}

    public Curso(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }
}