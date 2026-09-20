package com.faculdade.notas.service;

import com.faculdade.notas.model.ProfessorMateria;
import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.model.dto.ProfessorDTO;
import com.faculdade.notas.model.dto.RequisicaoNotaDTO;
import com.faculdade.notas.repository.ProfessorMateriaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotaService {

    private final ProfessorMateriaRepository repository;
    private final AvaliadorAcademico avaliador;
    private final ObjectMapper objectMapper;

    public NotaService(ProfessorMateriaRepository repository, AvaliadorAcademico avaliador, ObjectMapper objectMapper) {
        this.repository = repository;
        this.avaliador = avaliador;
        this.objectMapper = objectMapper;
    }

    public List<ProfessorDTO> listarProfessores() {
        List<ProfessorMateria> vinculacoes = repository.findAll();

        return vinculacoes.stream().map(pm -> {
            try {
                JsonNode formulaNode = objectMapper.readTree(pm.getJsonFormula());
                JsonNode rotulosNode = formulaNode.get("rotulos");

                String[] rotulos = new String[rotulosNode.size()];
                for (int i = 0; i < rotulosNode.size(); i++) {
                    rotulos[i] = rotulosNode.get(i).asText();
                }

                return new ProfessorDTO(
                        pm.getId().intValue(),
                        pm.getProfessor().getNome(),
                        pm.getMateria().getNome(),
                        rotulos
                );
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar fórmula JSON para o ID: " + pm.getId(), e);
            }
        }).collect(Collectors.toList());
    }

    public ResultadoAvaliacao avaliar(RequisicaoNotaDTO requisicao) {
        ProfessorMateria pm = repository.findById(requisicao.getIndiceProfessor().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Vínculo não encontrado."));

        try {
            JsonNode formulaNode = objectMapper.readTree(pm.getJsonFormula());

            // Lógica de compatibilidade: se o banco ainda não tiver a chave "formula", usa a do SIGA
            String formula = formulaNode.has("formula")
                    ? formulaNode.get("formula").asText()
                    : "MAX(MAX(P1+P2, P1+P3), P2+P3)/2";

            JsonNode rotulosNode = formulaNode.get("rotulos");
            String[] rotulos = new String[rotulosNode.size()];
            for (int i = 0; i < rotulosNode.size(); i++) {
                rotulos[i] = rotulosNode.get(i).asText();
            }

            // Envia os dados diretamente para o motor dinâmico
            return avaliador.avaliar(formula, rotulos, requisicao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a avaliação", e);
        }
    }
}