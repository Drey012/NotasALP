package com.faculdade.notas.model.dto;

public class RequisicaoNotaDTO {

    private Integer indiceProfessor;
    private double[] notasIniciais;
    private Double p3;
    private Double exame;

    // Getters and Setters

    public Integer getIndiceProfessor() {
        return indiceProfessor;
    }

    public void setIndiceProfessor(Integer indiceProfessor) {
        this.indiceProfessor = indiceProfessor;
    }

    public double[] getNotasIniciais() {
        return notasIniciais;
    }

    public void setNotasIniciais(double[] notasIniciais) {
        this.notasIniciais = notasIniciais;
    }

    public Double getP3() {
        return p3;
    }

    public void setP3(Double p3) {
        this.p3 = p3;
    }

    public Double getExame() {
        return exame;
    }

    public void setExame(Double exame) {
        this.exame = exame;
    }
}
