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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String key = request.getRemoteAddr() + ":" + routeGroup(path);
        Bucket bucket = buckets.computeIfAbsent(key, ignored -> createBucket(path));
        if (!bucket.tryConsume(1)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Retry-After", "60");
            response.getWriter().write("{\"status\":429,\"mensagem\":\"Limite de requisições excedido. Tente novamente em 1 minuto.\",\"timestamp\":\"" + LocalDateTime.now() + "\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private Bucket createBucket(String path) {
        int capacity = path.startsWith("/api/auth/") ? 8 : 60;
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.greedy(capacity, Duration.ofMinutes(1))))
                .build();
    }

    private String routeGroup(String path) {
        if (path.startsWith("/api/auth/")) return "auth";
        if (path.startsWith("/api/admin/")) return "admin";
        if (path.equals("/api/avaliar")) return "avaliar";
        return "public";
    }
}
