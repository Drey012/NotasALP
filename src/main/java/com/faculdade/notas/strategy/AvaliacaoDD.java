package com.faculdade.notas.strategy;

public class AvaliacaoDD implements EstrategiaAvaliacao{
    @Override
    public String getNomeProfessor() {
        return "Ivo Branquinho";
    }

    @Override
    public String getNomeMateria() {
        return "Design Digital";
    }

    @Override
    public String[] getRotulosNotasIniciais() {
        return new String[]{"P1", "P2"};
    }

    @Override
    public double calcularMedia1(double[] notas) {
        return (notas[0] + notas[1])/2;
    }

    @Override
    public double calcularMedia2(double[] notas, double p3) {
        double p1 = notas[0];
        double p2 = notas[1];

        if (p1 < p2) {
            return (p3 + p2)/2;
        } else {
            return (p1 + p3)/2;
        }
    }
}
