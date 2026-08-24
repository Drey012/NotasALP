package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.model.dto.ProfessorDTO;
import com.faculdade.notas.model.dto.RequisicaoNotaDTO;
import com.faculdade.notas.strategy.EstrategiaAvaliacao;
import com.faculdade.notas.strategy.StrategyFactory;
import com.faculdade.notas.util.CalculoUtils;
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
            throw new RegraNegocioException("O índice do professor é obrigatório.");
        }

        // Busca a estratégia (se o índice for inválido, o StrategyFactory lança RecursoNaoEncontradoException)
        EstrategiaAvaliacao estrategia = StrategyFactory.obterPorIndice(requisicao.getIndiceProfessor());

        // Validações de limites e quantidade de notas
        validarNotas(requisicao.getNotasIniciais(), estrategia.getRotulosNotasIniciais().length);
        if (requisicao.getP3() != null) validarNota("P3", requisicao.getP3());
        if (requisicao.getExame() != null) validarNota("Exame Final", requisicao.getExame());

        AvaliadorAcademico avaliador = new AvaliadorAcademico(estrategia);
        ResultadoAvaliacao resultado = avaliador.avaliar(
                requisicao.getNotasIniciais(),
                requisicao.getP3(),
                requisicao.getExame()
        );

        // Aplica o arredondamento de 2 casas decimais na nota retornada
        double notaArredondada = CalculoUtils.arredondar(resultado.getNotaAtual());

        return new ResultadoAvaliacao(
                notaArredondada,
                resultado.getStatus(),
                resultado.isPrecisaP3(),
                resultado.isPrecisaExame()
        );
    }

    private void validarNotas(double[] notas, int quantidadeEsperada) {
        if (notas == null || notas.length != quantidadeEsperada) {
            throw new RegraNegocioException("Quantidade de notas enviadas é inválida. Esperado: " + quantidadeEsperada);
        }
        for (double nota : notas) {
            validarNota("Nota", nota);
        }
    }

    private void validarNota(String nomeNota, double valor) {
        if (valor < 0.0 || valor > 10.0) {
            throw new RegraNegocioException("A " + nomeNota + " deve estar entre 0.0 e 10.0.");
        }
    }
}