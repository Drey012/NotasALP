package com.faculdade.notas.controller;

import com.faculdade.notas.model.Professor;
import com.faculdade.notas.model.dto.request.ProfessorRequestDTO;
import com.faculdade.notas.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Professor> criar(@RequestBody ProfessorRequestDTO dto) {
        Professor professorSalvo = service.cadastrarProfessor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }
}