package com.faculdade.notas.controller;

import com.faculdade.notas.model.dto.response.LoginResponseDTO;
import com.faculdade.notas.model.dto.request.LoginRequestDTO;
import com.faculdade.notas.model.dto.request.RegistroRequestDTO;
import com.faculdade.notas.service.AutenticacaoResultado;
import com.faculdade.notas.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {
    private static final String COOKIE_NAME = "NOTASALP_SESSION";
    private final AutenticacaoService autenticacaoService;
    private final boolean cookieSecure;
    private final String cookieSameSite;

    public AutenticacaoController(AutenticacaoService autenticacaoService,
                                  @Value("${app.auth.cookie-secure:false}") boolean cookieSecure,
                                  @Value("${app.auth.cookie-same-site:Lax}") String cookieSameSite) {
        this.autenticacaoService = autenticacaoService;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
    }

    @PostMapping("/registrar")
    public ResponseEntity<LoginResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO dto) {
        return responder(HttpStatus.CREATED, autenticacaoService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return responder(HttpStatus.OK, autenticacaoService.autenticar(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie(null, Duration.ZERO).toString())
                .build();
    }

    private ResponseEntity<LoginResponseDTO> responder(HttpStatus status, AutenticacaoResultado resultado) {
        LoginResponseDTO resposta = new LoginResponseDTO(resultado.email(), resultado.nome(), resultado.cargo());
        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, cookie(resultado.token(), Duration.ofHours(8)).toString())
                .body(resposta);
    }

    private ResponseCookie cookie(String value, Duration maxAge) {
        return ResponseCookie.from(COOKIE_NAME, value == null ? "" : value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
