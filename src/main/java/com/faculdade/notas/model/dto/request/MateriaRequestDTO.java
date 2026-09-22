package com.faculdade.notas.model.dto.request;

public record MateriaRequestDTO(
        String nome,
        String sigla,
        Long semestreId
) {}