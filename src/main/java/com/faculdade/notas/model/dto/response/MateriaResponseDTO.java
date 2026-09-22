package com.faculdade.notas.model.dto.response;

public record MateriaResponseDTO(
        Long id,
        String nome,
        String sigla,
        Integer ordemSemestre,
        String nomeCurso
) {}