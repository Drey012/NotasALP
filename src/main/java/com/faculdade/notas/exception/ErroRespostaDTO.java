package com.faculdade.notas.exception;

import java.time.LocalDateTime;

public class ErroRespostaDTO {
    private final String mensagem;
    private final int status;
    private final LocalDateTime timestamp;

    public ErroRespostaDTO(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getMensagem() { return mensagem; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}