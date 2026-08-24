package com.faculdade.notas.strategy;
import com.faculdade.notas.util.CalculoUtils;

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

    @Override
    public double calcularNotaNecessaria(double[] notasAtuais) {
        // Se o aluno informou apenas a P1 (1 nota), calculamos a P2
        if (notasAtuais.length == 1) {
            double p1 = notasAtuais[0];

            // P2 = 12.0 - P1
            double p2Necessaria = 12.0 - p1;

            return Math.max(0.0, CalculoUtils.arredondar(p2Necessaria));
        }
        return 0.0;
    }
}
