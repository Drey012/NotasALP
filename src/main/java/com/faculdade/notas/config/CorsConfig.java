package com.faculdade.notas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica para todas as rotas da API (/api/**, /api/admin/**, etc.)
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001") // Permite o front-end Next.js (adicione outras portas se necessário)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos HTTP principais
                        .allowedHeaders("*"); // Permite todos os headers (Content-Type, Authorization, etc.)
            }
        };
    }
}