package com.faculdade.notas.strategy;

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
}
