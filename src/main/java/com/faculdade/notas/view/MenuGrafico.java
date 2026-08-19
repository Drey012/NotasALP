//Camada da interface com JOptionPane.
package com.faculdade.notas.view;

import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.service.AvaliadorAcademico;
import com.faculdade.notas.strategy.EstrategiaAvaliacao;
import com.faculdade.notas.strategy.StrategyFactory;

import javax.swing.JOptionPane;
import java.util.List;

public class MenuGrafico {
    public void iniciar() {
        List<EstrategiaAvaliacao> estrategias = StrategyFactory.getEstrategiasDisponiveis();
        String[] opcoes = estrategias.stream().map(EstrategiaAvaliacao::getNomeProfessor).toArray(String[]::new);

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Selecione o Professor/Matéria:",
                "Sistema de Notas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha < 0) return;

        EstrategiaAvaliacao estrategia = StrategyFactory.obterPorIndice(escolha);
        AvaliadorAcademico avaliador = new AvaliadorAcademico(estrategia);

        String[] rotulos = estrategia.getRotulosNotasIniciais();
        double[] notasIniciais = new double[rotulos.length];

        for (int i = 0; i < rotulos.length; i++) {
            double nota = pedirNota("Digite a nota de " + rotulos[i] + ":");
            if (nota == -1) return;
            notasIniciais[i] = nota;
        }

        // Avaliação M1
        ResultadoAvaliacao res = avaliador.avaliar(notasIniciais, null, null);
        exibirMensagem("Média 1: " + String.format("%.2f", res.getNotaAtual()) + "\nSituação: " + res.getStatus());

        Double p3 = null;
        if (res.isPrecisaP3()) {
            double notaP3 = pedirNota("Digite a nota da P3:");
            if (notaP3 == -1) return;
            p3 = notaP3;

            res = avaliador.avaliar(notasIniciais, p3, null);
            exibirMensagem("Média 2: " + String.format("%.2f", res.getNotaAtual()) + "\nSituação: " + res.getStatus());
        }

        if (res.isPrecisaExame()) {
            double notaExame = pedirNota("Digite a nota do Exame Final:");
            if (notaExame == -1) return;

            res = avaliador.avaliar(notasIniciais, p3, notaExame);
            exibirMensagem("Resultado Exame: " + String.format("%.2f", res.getNotaAtual()) + "\nSituação Final: " + res.getStatus());
        }
    }

    private double pedirNota(String mensagem) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, mensagem);
            if (input == null) return -1;
            try {
                return Double.parseDouble(input.replace(",", "."));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite um número válido ex: 7.5", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
}