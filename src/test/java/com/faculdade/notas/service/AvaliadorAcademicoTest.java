package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.dto.RequisicaoNotaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AvaliadorAcademicoTest {
    private final AvaliadorAcademico avaliador = new AvaliadorAcademico();

    @Test
    void deveRejeitarFormulaComSintaxeInvalida() {
        assertThrows(
                RegraNegocioException.class,
                () -> avaliador.validarFormula("P1 + * P2", new String[]{"P1", "P2"})
        );
    }

    @Test
    void deveRejeitarResultadoDeDivisaoPorZero() {
        RequisicaoNotaDTO requisicao = new RequisicaoNotaDTO();
        requisicao.setNotasIniciais(new double[]{8.0});

        assertThrows(
                RegraNegocioException.class,
                () -> avaliador.avaliar("P1 / 0", new String[]{"P1"}, requisicao)
        );
    }

    @Test
    void deveRejeitarNotasIniciaisForaDoIntervaloDeZeroADez() {
        RequisicaoNotaDTO abaixoDoMinimo = requisicaoComNota(-0.1);
        RequisicaoNotaDTO acimaDoMaximo = requisicaoComNota(10.1);

        assertThrows(
                RegraNegocioException.class,
                () -> avaliador.avaliar("P1", new String[]{"P1"}, abaixoDoMinimo)
        );
        assertThrows(
                RegraNegocioException.class,
                () -> avaliador.avaliar("P1", new String[]{"P1"}, acimaDoMaximo)
        );
    }

    @Test
    void deveRejeitarP3EExameForaDoIntervaloDeZeroADez() {
        RequisicaoNotaDTO p3ForaDoIntervalo = requisicaoComNota(8.0);
        p3ForaDoIntervalo.setP3(10.1);
        RequisicaoNotaDTO exameForaDoIntervalo = requisicaoComNota(8.0);
        exameForaDoIntervalo.setExame(-0.1);

        assertThrows(
                RegraNegocioException.class,
                () -> avaliador.avaliar("P1", new String[]{"P1"}, p3ForaDoIntervalo)
        );
        assertThrows(
                RegraNegocioException.class,
                () -> avaliador.avaliar("P1", new String[]{"P1"}, exameForaDoIntervalo)
        );
    }

    private RequisicaoNotaDTO requisicaoComNota(double nota) {
        RequisicaoNotaDTO requisicao = new RequisicaoNotaDTO();
        requisicao.setNotasIniciais(new double[]{nota});
        return requisicao;
    }
}
