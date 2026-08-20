//Centraliza a busca do professor para evitar usar switch/case diretamente na interface do usuário.
package com.faculdade.notas.strategy;

import java.util.List;

public class StrategyFactory {
    private static final List<EstrategiaAvaliacao> ESTRATEGIAS = List.of(
            //Adicionar matérias
            new AvaliacaoALP(),
            new AvaliacaoOS(),
            new AvaliacaoDD(),
            new AvaliacaoMBD(),
            new AvaliacaoES()
    );

    public static List<EstrategiaAvaliacao> getEstrategiasDisponiveis() {
        return ESTRATEGIAS;
    }

    public static EstrategiaAvaliacao obterPorIndice(int indice) {
        if (indice >= 0 && indice < ESTRATEGIAS.size()) {
            return ESTRATEGIAS.get(indice);
        }
        return null;
    }
}