package com.faculdade.notas.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SemestreRequestDTO(
        @NotNull(message = "A ordem do semestre é obrigatória.")
        @Min(value = 1, message = "A ordem do semestre deve ser pelo menos 1.")
        Integer ordem,

        @NotNull(message = "O ID do curso é obrigatório.")
        Long cursoId
) {}