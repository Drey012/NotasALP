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
        // M1
        double m1 = estrategia.calcularMedia1(notasIniciais);
        if (m1 >= 6.0) {
            return new ResultadoAvaliacao(m1, "APROVADO", false, false);
        }

        if (p3 == null) {
            return new ResultadoAvaliacao(m1, "NECESSÁRIO P3", true, false);
        }

        // M2
        double m2 = estrategia.calcularMedia2(notasIniciais, p3);
        if (m2 >= 6.0) {
            return new ResultadoAvaliacao(m2, "APROVADO COM P3", false, false);
        }
        if (m2 < 4.0) {
            return new ResultadoAvaliacao(m2, "REPROVADO (Inelegível para Exame Final)", false, false);
        }

        if (exame == null) {
            return new ResultadoAvaliacao(m2, "NECESSÁRIO EXAME FINAL", false, true);
        }

        // Exame Final
        if (exame >= 6.0) {
            return new ResultadoAvaliacao(exame, "APROVADO NO EXAME FINAL", false, false);
        } else {
            return new ResultadoAvaliacao(exame, "REPROVADO NO EXAME FINAL", false, false);
        }
    }
}