//Manipulador global de erros
//Captura a Exception e monta o ErroRespostaDTO com status
package com.faculdade.notas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRegraNegocio(RegraNegocioException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Intercepta qualquer outro erro genérico não tratado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> tratarGeral(Exception ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO("Ocorreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}