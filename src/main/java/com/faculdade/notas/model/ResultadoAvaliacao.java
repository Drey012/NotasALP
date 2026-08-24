package com.faculdade.notas.model;

public class ResultadoAvaliacao {
    private final double notaAtual;
    private final String status;
    private final boolean precisaP3;
    private final boolean precisaExame;
    private final Double notaNecessariaProximaProva;

    // Construtor Completo (5 parâmetros)
    public ResultadoAvaliacao(double notaAtual, String status, boolean precisaP3, boolean precisaExame, Double notaNecessariaProximaProva) {
        this.notaAtual = notaAtual;
        this.status = status;
        this.precisaP3 = precisaP3;
        this.precisaExame = precisaExame;
        this.notaNecessariaProximaProva = notaNecessariaProximaProva;
    }

    // Construtor Sobrecarregado (4 parâmetros - Mantém retrocompatibilidade)
    public ResultadoAvaliacao(double notaAtual, String status, boolean precisaP3, boolean precisaExame) {
        this(notaAtual, status, precisaP3, precisaExame, null);
    }

    public double getNotaAtual() { return notaAtual; }
    public String getStatus() { return status; }
    public boolean isPrecisaP3() { return precisaP3; }
    public boolean isPrecisaExame() { return precisaExame; }
    public Double getNotaNecessariaProximaProva() { return notaNecessariaProximaProva; }
}