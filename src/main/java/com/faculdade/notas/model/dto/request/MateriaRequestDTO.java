package com.faculdade.notas.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MateriaRequestDTO(
        @NotBlank(message = "O nome da matéria é obrigatório.")
        String nome,

        @NotBlank(message = "A sigla é obrigatória.")
        String sigla,

        @NotNull(message = "O ID do semestre é obrigatório.")
        Long semestreId
) {}