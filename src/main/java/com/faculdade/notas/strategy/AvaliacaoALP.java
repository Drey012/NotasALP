package com.faculdade.notas.strategy;

public class AvaliacaoALP implements EstrategiaAvaliacao {
    @Override
    public String getNomeProfessor() {
        return "Sirley";
    }

    @Override
    public String getNomeMateria() {
        return "Algotitmo e Lógica de Programação";
    }

    @Override
    public String[] getRotulosNotasIniciais() {
        return new String[]{"P1", "Listas", "P2"};
    }

    @Override
    public double calcularMedia1(double[] notas) {
        return (0.35 * notas[0]) + (0.15 * notas[1]) + (0.50 * notas[2]);
    }

    @Override
    public double calcularMedia2(double[] notas, double p3) {
        double p1 = notas[0];
        double list = notas[1];
        double p2 = notas[2];

        if (p1 < p2) {
            return (0.35 * p3) + (0.15 * list) + (0.50 * p2);
        } else {
            return (0.35 * p1) + (0.15 * list) + (0.50 * p3);
        }
    }
}