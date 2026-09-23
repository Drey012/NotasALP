package com.faculdade.notas.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

// O JsonInclude faz com que campos nulos (como a lista de erros) não apareçam no JSON final se não forem usados
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroRespostaDTO {

    private String mensagem;
    private int status;
    private LocalDateTime timestamp;
    private List<String> detalhes; // Novo campo para listar erros específicos (ex: validação de campos)

    public ErroRespostaDTO(String mensagem, int status, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = timestamp;
    }

    public ErroRespostaDTO(String mensagem, int status, LocalDateTime timestamp, List<String> detalhes) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = timestamp;
        this.detalhes = detalhes;
    }

    // Getters
    public String getMensagem() { return mensagem; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<String> getDetalhes() { return detalhes; }
}