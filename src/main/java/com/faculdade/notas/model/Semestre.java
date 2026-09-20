package com.faculdade.notas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SEMESTRE")
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEM_INT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CUR_INT_ID", nullable = false)
    private Curso curso;

    @Column(name = "SEM_INT_ORDEM", nullable = false)
    private Integer ordem; // 1, 2, 3...

    public Semestre() {}

    public Semestre(Curso curso, Integer ordem) {
        this.curso = curso;
        this.ordem = ordem;
    }

    public Long getId() { return id; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
}