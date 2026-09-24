package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.Usuario;
import com.faculdade.notas.model.dto.request.LoginRequestDTO;
import com.faculdade.notas.model.dto.request.RegistroRequestDTO;
import com.faculdade.notas.model.dto.response.LoginResponseDTO;
import com.faculdade.notas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AutenticacaoService(UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder,
                               JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("O e-mail informado já está em uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setCargo("ADMIN"); // Define o cargo padrao para novos cadastros

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getEmail());
        return new LoginResponseDTO(token, usuario.getEmail(), usuario.getNome());
    }

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        // Garantindo que a variavel receba o tipo correto (Usuario)
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas."));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new RegraNegocioException("Credenciais inválidas.");
        }

        String token = jwtService.generateToken(usuario.getEmail());
        return new LoginResponseDTO(token, usuario.getEmail(), usuario.getNome());
    }
}