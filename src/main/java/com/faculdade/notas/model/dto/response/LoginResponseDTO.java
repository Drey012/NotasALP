package com.faculdade.notas.model.dto.response;

public record LoginResponseDTO(
        String token,
        String tipo,
        String email,
        String nome,
        String cargo
) {
    public LoginResponseDTO(String email, String nome, String cargo) {
        this(null, "Bearer", email, nome, cargo);
    }
}
