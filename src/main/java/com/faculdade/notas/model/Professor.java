package com.faculdade.notas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PROFESSOR")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRO_INT_ID")
    private Long id;

    @Column(name = "PRO_STR_NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "PRO_STR_EMAIL", length = 100)
    private String email;

    public Professor() {}

    public Professor(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}