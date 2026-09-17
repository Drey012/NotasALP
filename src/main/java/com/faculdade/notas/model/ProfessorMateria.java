package com.faculdade.notas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PROFESSOR_MATERIA")
public class ProfessorMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PMA_INT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PROFESSOR_PRO_INT_ID", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "MATERIA_MAT_INT_ID", nullable = false)
    private Materia materia;

    @Column(name = "PMA_STR_TURNO", length = 10)
    private String turno; // 'MANHA', 'NOITE'

    // Armazena o JSON da regra de cálculo do professor nesta disciplina
    @Column(name = "MET_JSON_FORMULA", columnDefinition = "TEXT", nullable = false)
    private String jsonFormula;

    public ProfessorMateria() {}

    public ProfessorMateria(Professor professor, Materia materia, String turno, String jsonFormula) {
        this.professor = professor;
        this.materia = materia;
        this.turno = turno;
        this.jsonFormula = jsonFormula;
    }

    public Long getId() { return id; }
    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public String getJsonFormula() { return jsonFormula; }
    public void setJsonFormula(String jsonFormula) { this.jsonFormula = jsonFormula; }
}