package com.faculdade.notas.strategy;
import com.faculdade.notas.util.CalculoUtils;

public class AvaliacaoMBD implements EstrategiaAvaliacao{

    @Override
    public String getNomeProfessor() {
        return "Fernando";
    }

    @Override
    public String getNomeMateria() {
        return "Modelagem de Banco de Dados";
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

        // Regra 3: Em caso de ter uma nota menor na P3 do que em ambas, P1 e P2, nenhuma delas deve ser substituída
        if (p3 <= p1 && p3 <= p2) {
            return calcularMedia1(notas);
        }

        if (p1 < p2) {
            return (p3 + p2)/2;
        } else {
            return (p1 + p3)/2;
        }
    }

    @Override
    public double calcularNotaNecessaria(double[] notasAtuais) {
        // Se informou apenas P1 (1 nota), calculamos a P2 necessária para M1 >= 6
        if (notasAtuais.length == 1) {
            double p1 = notasAtuais[0];
            double p2Necessaria = 12.0 - p1;
            return Math.max(0.0, CalculoUtils.arredondar(p2Necessaria));
        }

        // Regra 4: Se informou P1 e P2 (2 notas) e M1 < 6, calcula P3 necessária mantendo a maior entre P1 e P2
        if (notasAtuais.length == 2) {
            double p1 = notasAtuais[0];
            double p2 = notasAtuais[1];
            double maiorProva = Math.max(p1, p2);
            double p3Necessaria = 12.0 - maiorProva;
            return Math.max(0.0, CalculoUtils.arredondar(p3Necessaria));
        }

        return 0.0;
    }
}

