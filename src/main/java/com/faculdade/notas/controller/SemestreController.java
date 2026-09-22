package com.faculdade.notas.controller;

import com.faculdade.notas.model.dto.request.SemestreRequestDTO;
import com.faculdade.notas.model.dto.response.SemestreResponseDTO;
import com.faculdade.notas.service.SemestreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SemestreController {

    private final SemestreService service;

    public SemestreController(SemestreService service) {
        this.service = service;
    }

    @PostMapping("/admin/semestres")
    public ResponseEntity<SemestreResponseDTO> criar(@RequestBody SemestreRequestDTO dto) {
        SemestreResponseDTO semestreSalvo = service.cadastrarSemestre(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(semestreSalvo);
    }

    @GetMapping("/semestres")
    public ResponseEntity<List<SemestreResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}