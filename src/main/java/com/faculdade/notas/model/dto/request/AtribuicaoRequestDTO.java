package com.faculdade.notas.model.dto.request;

public record AtribuicaoRequestDTO(
        Long professorId,
        Long materiaId,
        String turno,
        String jsonFormula
) {}