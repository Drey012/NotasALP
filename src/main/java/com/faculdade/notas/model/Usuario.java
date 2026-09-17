package com.faculdade.notas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USU_INT_ID")
    private Long id;

    @Column(name = "USU_STR_NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "USU_STR_EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "USU_STR_SENHA", length = 255, nullable = false)
    private String senha;

    @Column(name = "USU_STR_CARGO", length = 20, nullable = false)
    private String cargo; // 'ADMIN', 'COORDENADOR', 'PROFESSOR'

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String cargo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}