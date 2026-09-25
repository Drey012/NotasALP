package com.faculdade.notas.config;

import com.faculdade.notas.model.Usuario;
import com.faculdade.notas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {
    @Bean
    CommandLineRunner provisionAdmin(UsuarioRepository repository,
                                     PasswordEncoder encoder,
                                     @Value("${app.admin.email:}") String email,
                                     @Value("${app.admin.password:}") String password,
                                     @Value("${app.admin.name:Administrador}") String name) {
        return args -> {
            if (email.isBlank() || password.isBlank() || repository.existsByEmail(email.toLowerCase())) return;
            Usuario admin = new Usuario(name.trim(), email.trim().toLowerCase(), encoder.encode(password), "ADMIN");
            repository.save(admin);
        };
    }
}
