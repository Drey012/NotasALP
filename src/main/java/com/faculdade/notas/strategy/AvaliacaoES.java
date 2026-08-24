package com.faculdade.notas.strategy;
import com.faculdade.notas.util.CalculoUtils;

public class AvaliacaoES implements EstrategiaAvaliacao{
    @Override
    public String getNomeProfessor() {
        return "Danilo";
    }

    @Override
    public String getNomeMateria() {
        return "Engenharia de Software";
    }

    @Override
    public String[] getRotulosNotasIniciais() {
        return new String[]{"P1", "P2", "Projeto", "PI"};
    }

    @Override
    public double calcularMedia1(double[] notas) {
        return ((notas[0]+notas[1])/2)*0.5+notas[2]*0.2+notas[3]*0.3;
    }

    @Override
    public double calcularMedia2(double[] notas, double p3) {
        double p1 = notas[0];
        double p2 = notas[1];
        double projeto = notas[2];
        double PI = notas[3];

        if (p1 < p2){
            return ((p3+p2)/2)*0.5+projeto*0.2+PI*0.3;
        } else {
            return ((p1+p3)/2)*0.5+projeto*0.2+PI*0.3;
        }
    }

    @Override
    public double calcularNotaNecessaria(double[] notasAtuais) {
        // Cenário 1: O aluno forneceu P1, Projeto e PI (3 notas) e quer saber quanto precisa tirar na P2 para fechar M1 >= 6.0
        if (notasAtuais.length == 3) {
            double p1 = notasAtuais[0];
            double projeto = notasAtuais[1];
            double PI = notasAtuais[2];

            double p2Necessaria = ((6.0 - (projeto * 0.2) - (PI * 0.3)) / 0.25) - p1;
            return Math.max(0.0, CalculoUtils.arredondar(p2Necessaria));
        }

        // Cenário 2: O aluno forneceu todas as 4 notas iniciais (P1, P2, Projeto, PI), ficou com M1 < 6.0 e precisa calcular quanto tirar na P3
        if (notasAtuais.length == 4) {
            double p1 = notasAtuais[0];
            double p2 = notasAtuais[1];
            double projeto = notasAtuais[2];
            double PI = notasAtuais[3];

            // A P3 substituirá a menor prova. Logo, a prova mantida na equação é a maior entre P1 e P2.
            double maiorProva = Math.max(p1, p2);

            double p3Necessaria = ((6.0 - (projeto * 0.2) - (PI * 0.3)) / 0.25) - maiorProva;
            return Math.max(0.0, CalculoUtils.arredondar(p3Necessaria));
        }

        return 0.0;
    }
}
