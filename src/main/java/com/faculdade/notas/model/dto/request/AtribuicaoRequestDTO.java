package com.faculdade.notas.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtribuicaoRequestDTO(
        @NotNull(message = "O ID do professor é obrigatório.")
        Long professorId,

        @NotNull(message = "O ID da matéria é obrigatório.")
        Long materiaId,

        @NotBlank(message = "O turno é obrigatório (ex: MANHA, TARDE, NOITE).")
        String turno,

        @NotBlank(message = "A fórmula JSON é obrigatória.")
        String jsonFormula
) {}