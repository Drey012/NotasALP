package com.faculdade.notas.controller;

import com.faculdade.notas.model.dto.request.CursoRequestDTO;
import com.faculdade.notas.model.dto.response.CursoResponseDTO;
import com.faculdade.notas.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    // Rota de Inserção (Administrativa)
    @PostMapping("/admin/cursos")
    public ResponseEntity<CursoResponseDTO> criar(@RequestBody CursoRequestDTO dto) {
        CursoResponseDTO cursoSalvo = service.cadastrarCurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
    }

    // Rota de Listagem (Pública/Uso Geral)
    @GetMapping("/cursos")
    public ResponseEntity<List<CursoResponseDTO>> listarCursos() {
        List<CursoResponseDTO> cursos = service.listarTodos();
        return ResponseEntity.ok(cursos);
    }

    @PutMapping("/admin/cursos/{id}")
    public ResponseEntity<CursoResponseDTO> atualizar(@PathVariable Long id, @RequestBody CursoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarCurso(id, dto));
    }

    @DeleteMapping("/admin/cursos/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirCurso(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content (padrão para deleção com sucesso)
    }
}