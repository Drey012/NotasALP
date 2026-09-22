package com.faculdade.notas.controller;

import com.faculdade.notas.model.dto.request.ProfessorRequestDTO;
import com.faculdade.notas.model.dto.response.ProfessorResponseDTO;
import com.faculdade.notas.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping("/admin/professores")
    public ResponseEntity<ProfessorResponseDTO> criar(@RequestBody ProfessorRequestDTO dto) {
        ProfessorResponseDTO professorSalvo = service.cadastrarProfessor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }

    @GetMapping("/professores-cadastrados")
    public ResponseEntity<List<ProfessorResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}