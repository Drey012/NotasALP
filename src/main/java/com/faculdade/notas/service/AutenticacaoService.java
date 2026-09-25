package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.Usuario;
import com.faculdade.notas.model.dto.request.LoginRequestDTO;
import com.faculdade.notas.model.dto.request.RegistroRequestDTO;
import com.faculdade.notas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final boolean registroPublico;

    public AutenticacaoService(UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder,
                               JwtService jwtService,
                               @Value("${app.auth.public-registration:false}") boolean registroPublico) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.registroPublico = registroPublico;
    }

    public AutenticacaoResultado registrar(RegistroRequestDTO dto) {
        if (!registroPublico) {
            throw new RegraNegocioException("O cadastro público está desativado. Solicite um convite ao administrador.");
        }
        String email = dto.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("O e-mail informado já está em uso.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setCargo("CONSULTOR");
        usuarioRepository.save(usuario);
        return gerarResultado(usuario);
    }

    public AutenticacaoResultado autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email().trim().toLowerCase())
                .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas."));
        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new RegraNegocioException("Credenciais inválidas.");
        }
        return gerarResultado(usuario);
    }

    private AutenticacaoResultado gerarResultado(Usuario usuario) {
        return new AutenticacaoResultado(
                jwtService.generateToken(usuario.getEmail()),
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getCargo()
        );
    }
}
