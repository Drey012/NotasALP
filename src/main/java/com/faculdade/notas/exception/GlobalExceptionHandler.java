package com.faculdade.notas.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException; // Adicione esta importação (requer Spring Security na fase 2, se der erro agora, pode comentar esse método)
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - NOT FOUND
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 400 - BAD REQUEST (Regras de Negócio)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroRespostaDTO> handleRegraNegocio(RegraNegocioException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 400 - BAD REQUEST (JSON malformado ou erro de encoding/sintaxe)
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDTO> handleJsonError(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                "O corpo da requisição (JSON) está malformado ou contém caracteres inválidos.",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 409 - CONFLICT (Integridade do Banco - Efeito Cascata)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroRespostaDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                "Este registro não pode ser excluído pois possui outros dados vinculados a ele no sistema.",
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    // 400 - BAD REQUEST (Validação de Campos do Frontend - @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> handleValidations(MethodArgumentNotValidException ex) {
        // Extrai a mensagem de erro de cada campo que falhou
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        ErroRespostaDTO erro = new ErroRespostaDTO("Falha na validação dos dados enviados.", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 403 - FORBIDDEN (Usuário logado tenta acessar rota que não tem permissão)
/*
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroRespostaDTO> handleAccessDenied(AccessDeniedException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO("Você não tem permissão para realizar esta ação.", HttpStatus.FORBIDDEN.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }
*/

    // 500 - INTERNAL SERVER ERROR (Qualquer erro que não prevemos, o "pega-tudo")
    // Isso esconde o StackTrace do Java do usuário final por motivos de segurança
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> handleGenerico(Exception ex) {
        // Num cenário real, você daria um console.log ou log.error(ex.getMessage()) aqui para investigar depois
        ErroRespostaDTO erro = new ErroRespostaDTO("Ocorreu um erro interno inesperado no servidor. Contate o suporte.", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}