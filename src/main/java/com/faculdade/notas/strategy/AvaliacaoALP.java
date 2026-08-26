package com.faculdade.notas.strategy;
import com.faculdade.notas.util.CalculoUtils;

public class AvaliacaoALP implements EstrategiaAvaliacao {
    @Override
    public String getNomeProfessor() {
        return "Sirley";
    }

    @Override
    public String getNomeMateria() {
        return "Algoritmo e Lógica de Programação";
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

        // Regra 3: Em caso de ter uma nota menor na P3 do que em ambas, P1 e P2, nenhuma delas deve ser substituída
        if (p3 <= p1 && p3 <= p2) {
            return calcularMedia1(notas);
        }

        if (p1 < p2) {
            return (0.35 * p3) + (0.15 * list) + (0.50 * p2);
        } else {
            return (0.35 * p1) + (0.15 * list) + (0.50 * p3);
        }
    }

    @Override
    public double calcularNotaNecessaria(double[] notasAtuais) {
        // Regra 2: Se informou P1 e Listas (2 notas), calcula a P2 necessária para M1 >= 6
        if (notasAtuais.length == 2) {
            double p1 = notasAtuais[0];
            double list = notasAtuais[1];

            // P2 = (6.0 - 0.35*P1 - 0.15*List) / 0.50
            double p2Necessaria = (6.0 - (0.35 * p1) - (0.15 * list)) / 0.50;

            return Math.max(0.0, CalculoUtils.arredondar(p2Necessaria));
        }

        // Regra 4: Se informou P1, Listas e P2 (3 notas completas) e ficou com M1 < 6, calcula P3 necessária mantendo a maior entre P1 e P2
        if (notasAtuais.length == 3) {
            double p1 = notasAtuais[0];
            double list = notasAtuais[1];
            double p2 = notasAtuais[2];

            double maiorProva = Math.max(p1, p2);

            // (0.35 * maiorProva + 0.15 * list + 0.50 * p3) = 6.0 se P1 for menor ou vice versa
            // Se p1 < p2, a prova substituída é p1 por p3: 0.35 * p3 + 0.15 * list + 0.50 * p2 = 6 => p3 = (6 - 0.15*list - 0.50*p2)/0.35
            // Se p2 <= p1, a prova substituída é p2 por p3: 0.35 * p1 + 0.15 * list + 0.50 * p3 = 6 => p3 = (6 - 0.35*p1 - 0.15*list)/0.50
            double p3Necessaria;
            if (p1 < p2) {
                p3Necessaria = (6.0 - (0.15 * list) - (0.50 * p2)) / 0.35;
            } else {
                p3Necessaria = (6.0 - (0.35 * p1) - (0.15 * list)) / 0.50;
            }

            return Math.max(0.0, CalculoUtils.arredondar(p3Necessaria));
        }

        return 0.0;
    }
}