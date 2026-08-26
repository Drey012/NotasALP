package com.faculdade.notas.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Armazena um "balde" de tokens para cada IP de cliente
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Regra: Máximo de 20 requisições por minuto por IP
    private Bucket criarNovoBalde() {
        Bandwidth limite = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limite).build();
    }

    private Bucket obterBaldePorIp(String ip) {
        return buckets.computeIfAbsent(ip, k -> criarNovoBalde());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extrai o IP do cliente (considerando proxies ou Nginx/Azure VM)
        String ipCliente = request.getHeader("X-Forwarded-For");
        if (ipCliente == null || ipCliente.isEmpty()) {
            ipCliente = request.getRemoteAddr();
        }

        Bucket balde = obterBaldePorIp(ipCliente);

        // Consome 1 token por requisição
        if (balde.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {
                    "status": 429,
                    "mensagem": "Limite de requisições excedido. Tente novamente em 1 minuto.",
                    "timestamp": "%s"
                }
            """.formatted(java.time.LocalDateTime.now()));
        }
    }
}