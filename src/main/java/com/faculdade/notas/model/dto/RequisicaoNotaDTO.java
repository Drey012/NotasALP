package com.faculdade.notas.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RequisicaoNotaDTO {
    @NotNull(message = "O índice do professor é obrigatório.")
    @Min(value = 1, message = "O índice do professor deve ser positivo.")
    private Integer indiceProfessor;

    @NotNull(message = "As notas iniciais são obrigatórias.")
    @Size(min = 1, max = 10, message = "Informe entre 1 e 10 notas iniciais.")
    private double[] notasIniciais;

    @DecimalMin(value = "0.0", message = "A nota P3 não pode ser menor que zero.")
    @DecimalMax(value = "10.0", message = "A nota P3 não pode ser maior que dez.")
    private Double p3;

    @DecimalMin(value = "0.0", message = "A nota de exame não pode ser menor que zero.")
    @DecimalMax(value = "10.0", message = "A nota de exame não pode ser maior que dez.")
    private Double exame;

    public Integer getIndiceProfessor() { return indiceProfessor; }
    public void setIndiceProfessor(Integer indiceProfessor) { this.indiceProfessor = indiceProfessor; }
    public double[] getNotasIniciais() { return notasIniciais; }
    public void setNotasIniciais(double[] notasIniciais) { this.notasIniciais = notasIniciais; }
    public Double getP3() { return p3; }
    public void setP3(Double p3) { this.p3 = p3; }
    public Double getExame() { return exame; }
    public void setExame(Double exame) { this.exame = exame; }
}
