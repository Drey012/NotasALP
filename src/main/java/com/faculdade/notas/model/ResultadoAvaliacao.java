//Objeto que guarda o resultado do cálculo e recebe os resultados das provas
package com.faculdade.notas.model;

public class ResultadoAvaliacao {
    private final double notaAtual;
    private final String status;
    private final boolean precisaP3;
    private final boolean precisaExame;

    public ResultadoAvaliacao(double notaAtual, String status, boolean precisaP3, boolean precisaExame) {
        this.notaAtual = notaAtual;
        this.status = status;
        this.precisaP3 = precisaP3;
        this.precisaExame = precisaExame;
    }

    public double getNotaAtual() { return notaAtual; }
    public String getStatus() { return status; }
    public boolean isPrecisaP3() { return precisaP3; }
    public boolean isPrecisaExame() { return precisaExame; }
}