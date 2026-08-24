//retorna a mensagem de erro de regras de negócio
package com.faculdade.notas.exception;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}