package com.faculdade.notas.service;

import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.model.dto.ProfessorDTO;
import com.faculdade.notas.model.dto.RequisicaoNotaDTO;
import com.faculdade.notas.strategy.EstrategiaAvaliacao;
import com.faculdade.notas.strategy.StrategyFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaService {

    public List<ProfessorDTO> listarProfessores() {
        List<EstrategiaAvaliacao> estrategias = StrategyFactory.getEstrategiasDisponiveis();
        List<ProfessorDTO> professores = new ArrayList<>();

        for (int i = 0; i < estrategias.size(); i++) {
            EstrategiaAvaliacao estrategia = estrategias.get(i);
            professores.add(new ProfessorDTO(
                    i,
                    estrategia.getNomeProfessor(),
                    estrategia.getNomeMateria(),
                    estrategia.getRotulosNotasIniciais()
            ));
        }

        return professores;
    }

    public ResultadoAvaliacao avaliar(RequisicaoNotaDTO requisicao) {
        if (requisicao.getIndiceProfessor() == null) {
            throw new IllegalArgumentException("Índice do professor é obrigatório.");
        }

        EstrategiaAvaliacao estrategia = StrategyFactory.obterPorIndice(requisicao.getIndiceProfessor());
        if (estrategia == null) {
            throw new IllegalArgumentException("Professor/Estratégia não encontrado para o índice informado.");
        }

        if (requisicao.getNotasIniciais() == null || requisicao.getNotasIniciais().length == 0) {
            throw new IllegalArgumentException("As notas iniciais são obrigatórias.");
        }

        AvaliadorAcademico avaliador = new AvaliadorAcademico(estrategia);
        return avaliador.avaliar(requisicao.getNotasIniciais(), requisicao.getP3(), requisicao.getExame());
    }
}
