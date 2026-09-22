package com.faculdade.notas.model.dto.response;

public record AtribuicaoResponseDTO(
        Long id,
        String nomeProfessor,
        String nomeMateria,
        String turno,
        String jsonFormula
) {}