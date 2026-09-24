package com.faculdade.notas.model.dto.response;

public record LoginResponseDTO(
        String token,
        String tipo,
        String email,
        String nome
) {
    public LoginResponseDTO(String token, String email, String nome) {
        this(token, "Bearer", email, nome);
    }
}