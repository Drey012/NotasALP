//A classe que contém as regras institucionais e o fluxo do diagrama.
package com.faculdade.notas.service;

import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.strategy.EstrategiaAvaliacao;

public class AvaliadorAcademico {
    private final EstrategiaAvaliacao estrategia;

    public AvaliadorAcademico(EstrategiaAvaliacao estrategia) {
        this.estrategia = estrategia;
    }

    public ResultadoAvaliacao avaliar(double[] notasIniciais, Double p3, Double exame) {
        String proximaProvaLabel = estrategia.getProximaProvaLabel(notasIniciais);

        // Regras 1 e 2: Se foram fornecidas menos notas do que as necessárias para calcular M1
        if (notasIniciais.length < estrategia.getQuantidadeNotasParaM1()) {
            double notaNecessaria = estrategia.calcularNotaNecessaria(notasIniciais);
            String status = "NECESSÁRIO " + proximaProvaLabel.toUpperCase();
            return new ResultadoAvaliacao(0.0, status, false, false, notaNecessaria, proximaProvaLabel);
        }

        // M1
        double m1 = estrategia.calcularMedia1(notasIniciais);
        if (m1 >= 6.0) {
            return new ResultadoAvaliacao(m1, "APROVADO", false, false, null, null);
        }

        double notaNecessaria = estrategia.calcularNotaNecessaria(notasIniciais);

        if (p3 == null) {
            return new ResultadoAvaliacao(m1, "NECESSÁRIO P3", true, false, notaNecessaria, "P3");
        }

        // M2
        double m2 = estrategia.calcularMedia2(notasIniciais, p3);
        if (m2 >= 6.0) {
            return new ResultadoAvaliacao(m2, "APROVADO COM P3", false, false, null, null);
        }
        if (m2 < 4.0) {
            return new ResultadoAvaliacao(m2, "REPROVADO (Inelegível para Exame Final)", false, false, null, null);
        }

        if (exame == null) {
            return new ResultadoAvaliacao(m2, "NECESSÁRIO EXAME FINAL", false, true, null, "Exame Final");
        }

        // Exame Final
        if (exame >= 6.0) {
            return new ResultadoAvaliacao(exame, "APROVADO NO EXAME FINAL", false, false, null, null);
        } else {
            return new ResultadoAvaliacao(exame, "REPROVADO NO EXAME FINAL", false, false, null, null);
        }
    }
}