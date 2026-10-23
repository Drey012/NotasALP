package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.model.dto.RequisicaoNotaDTO;
import com.faculdade.notas.util.CalculoUtils;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.springframework.stereotype.Service;

@Service
public class AvaliadorAcademico {

    // Ensina o exp4j a processar a função MAX do SIGA
    private final Function maxFunction = new Function("MAX", 2) {
        @Override
        public double apply(double... args) {
            return Math.max(args[0], args[1]);
        }
    };

    public void validarFormula(String formula, String[] rotulos) {
        Expression expression = criarExpressao(formula, rotulos);
        for (String rotulo : rotulos) {
            expression.setVariable(rotulo, 1.0);
        }
        if (formula.contains("P3")) {
            expression.setVariable("P3", 1.0);
        }
        try {
            expression.evaluate();
        } catch (ArithmeticException | IllegalArgumentException ex) {
            throw new RegraNegocioException("A sintaxe da fórmula é inválida. Revise a expressão e suas variáveis.");
        }
    }

    public ResultadoAvaliacao avaliar(String formula, String[] rotulos, RequisicaoNotaDTO requisicao) {
        double[] notas = requisicao.getNotasIniciais();
        Double p3 = requisicao.getP3();
        Double exame = requisicao.getExame();
        validarNotas(notas);
        validarNota(p3);
        validarNota(exame);

        // Regras 1 e 2: Se foram fornecidas menos notas do que as necessárias
        if (notas.length < rotulos.length) {
            String proximaProva = rotulos[notas.length];
            double notaNecessaria = simularNotaNecessaria(formula, rotulos, notas, 6.0);
            return new ResultadoAvaliacao(0.0, "NECESSÁRIO " + proximaProva.toUpperCase(), false, false, notaNecessaria, proximaProva);
        }

        // M1 (P3 assume 0.0 na fórmula do SIGA para não interferir na Média 1)
        double m1 = calcularExpressao(formula, rotulos, notas, 0.0);
        if (m1 >= 6.0) {
            return new ResultadoAvaliacao(m1, "APROVADO", false, false, null, null);
        }

        // Se M1 < 6, exige P3
        if (p3 == null) {
            double p3Necessaria = simularNotaNecessariaP3(formula, rotulos, notas, 6.0);
            return new ResultadoAvaliacao(m1, "NECESSÁRIO P3", true, false, p3Necessaria, "P3");
        }

        // M2 (O próprio MAX da fórmula fará a substituição da menor nota)
        double m2 = calcularExpressao(formula, rotulos, notas, p3);
        if (m2 >= 6.0) {
            return new ResultadoAvaliacao(m2, "APROVADO COM P3", false, false, null, null);
        }
        if (m2 < 4.0) {
            return new ResultadoAvaliacao(m2, "REPROVADO (Inelegível para Exame Final)", false, false, null, null);
        }

        if (exame == null) {
            return new ResultadoAvaliacao(m2, "NECESSÁRIO EXAME FINAL", false, true, 6.0, "Exame Final"); // Exame precisa ser 6
        }

        // Exame Final
        if (exame >= 6.0) {
            return new ResultadoAvaliacao(exame, "APROVADO NO EXAME FINAL", false, false, null, null);
        } else {
            return new ResultadoAvaliacao(exame, "REPROVADO NO EXAME FINAL", false, false, null, null);
        }
    }

    private double calcularExpressao(String formula, String[] rotulos, double[] notasIniciais, double p3Valor) {
        Expression expression = criarExpressao(formula, rotulos);

        for (int i = 0; i < rotulos.length; i++) {
            expression.setVariable(rotulos[i], notasIniciais[i]);
        }
        if (formula.contains("P3")) {
            expression.setVariable("P3", p3Valor);
        }

        double resultado;
        try {
            resultado = expression.evaluate();
        } catch (ArithmeticException ex) {
            throw new RegraNegocioException(
                    "A fórmula produziu um resultado inválido. Verifique operações como divisão por zero."
            );
        }
        if (!Double.isFinite(resultado)) {
            throw new RegraNegocioException(
                    "A fórmula produziu um resultado inválido. Verifique operações como divisão por zero."
            );
        }
        return CalculoUtils.arredondar(resultado);
    }

    private Expression criarExpressao(String formula, String[] rotulos) {
        if (formula == null || formula.isBlank() || rotulos == null) {
            throw new RegraNegocioException("A fórmula ou seus rótulos estão ausentes ou inválidos.");
        }

        ExpressionBuilder builder = new ExpressionBuilder(formula).function(maxFunction);
        for (String rotulo : rotulos) {
            if (rotulo == null || rotulo.isBlank()) {
                throw new RegraNegocioException("Os rótulos das notas da fórmula são inválidos.");
            }
            builder.variable(rotulo);
        }
        if (formula.contains("P3")) {
            builder.variable("P3");
        }

        try {
            return builder.build();
        } catch (IllegalArgumentException ex) {
            throw new RegraNegocioException("A sintaxe da fórmula é inválida. Revise a expressão e suas variáveis.");
        }
    }

    private void validarNotas(double[] notas) {
        if (notas == null) {
            throw new RegraNegocioException("As notas iniciais são obrigatórias.");
        }
        for (double nota : notas) {
            validarNota(nota);
        }
    }

    private void validarNota(Double nota) {
        if (nota != null && (!Double.isFinite(nota) || nota < 0.0 || nota > 10.0)) {
            throw new RegraNegocioException("Cada nota informada deve estar entre 0 e 10.");
        }
    }

    // Algoritmo de Força Bruta: Testa notas de 0.0 a 10.0 até o motor responder >= 6.0
    private double simularNotaNecessaria(String formula, String[] rotulos, double[] notasAtuais, double notaAlvo) {
        double[] notasSimuladas = new double[rotulos.length];
        System.arraycopy(notasAtuais, 0, notasSimuladas, 0, notasAtuais.length);

        int indexAlvo = notasAtuais.length;

        for (double tentativa = 0.0; tentativa <= 10.0; tentativa += 0.1) {
            notasSimuladas[indexAlvo] = tentativa;
            double resultado = calcularExpressao(formula, rotulos, notasSimuladas, 0.0);
            if (resultado >= notaAlvo) {
                return CalculoUtils.arredondar(tentativa);
            }
        }
        return 10.0;
    }

    private double simularNotaNecessariaP3(String formula, String[] rotulos, double[] notasIniciais, double notaAlvo) {
        for (double tentativa = 0.0; tentativa <= 10.0; tentativa += 0.1) {
            double resultado = calcularExpressao(formula, rotulos, notasIniciais, tentativa);
            if (resultado >= notaAlvo) {
                return CalculoUtils.arredondar(tentativa);
            }
        }
        return 10.0;
    }
}