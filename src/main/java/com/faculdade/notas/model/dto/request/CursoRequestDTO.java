package com.faculdade.notas.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CursoRequestDTO(
        @NotBlank(message = "O nome do curso é obrigatório.")
        String nome,

        @NotBlank(message = "A sigla é obrigatória.")
        @Size(max = 10, message = "A sigla deve ter no máximo 10 caracteres.")
        String sigla
) {}