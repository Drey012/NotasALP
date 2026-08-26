//Abstrai o padrão para a lógica de todas as avaliações
package com.faculdade.notas.strategy;

public interface EstrategiaAvaliacao {
    String getNomeProfessor();
    String getNomeMateria();
    String[] getRotulosNotasIniciais();

    double calcularMedia1(double[] notas);
    double calcularMedia2(double[] notas, double p3);

    double calcularNotaNecessaria(double[] notasAtuais);
    
    default int getQuantidadeNotasParaM1() {
        return getRotulosNotasIniciais().length;
    }

    default String getProximaProvaLabel(double[] notasAtuais) {
        int total = getRotulosNotasIniciais().length;
        if (notasAtuais.length < total) {
            return getRotulosNotasIniciais()[notasAtuais.length];
        }
        return "P3";
    }
}



