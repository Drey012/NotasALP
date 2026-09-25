package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
    @Mock UsuarioRepository repository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;

    @Test
    void deveBloquearRegistroPublicoPorPadrao() {
        var service = new AutenticacaoService(repository, passwordEncoder, jwtService, false);
        var dto = new com.faculdade.notas.model.dto.request.RegistroRequestDTO("Ana", "ana@example.com", "senha123");

        assertThrows(RegraNegocioException.class, () -> service.registrar(dto));
    }

    @Test
    void deveCriarConsultorQuandoRegistroForHabilitado() {
        when(repository.existsByEmail("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("hash");
        when(jwtService.generateToken("ana@example.com")).thenReturn("jwt");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var service = new AutenticacaoService(repository, passwordEncoder, jwtService, true);
        var dto = new com.faculdade.notas.model.dto.request.RegistroRequestDTO("Ana", "ana@example.com", "senha123");

        var result = service.registrar(dto);

        assertEquals("CONSULTOR", result.cargo());
        assertEquals("jwt", result.token());
    }
}
