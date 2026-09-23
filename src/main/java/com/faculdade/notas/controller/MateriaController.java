package com.faculdade.notas.controller;

import com.faculdade.notas.model.dto.request.MateriaRequestDTO;
import com.faculdade.notas.model.dto.response.MateriaResponseDTO;
import com.faculdade.notas.service.MateriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MateriaController {

    private final MateriaService service;

    public MateriaController(MateriaService service) {
        this.service = service;
    }

    @PostMapping("/admin/materias")
    public ResponseEntity<MateriaResponseDTO> criar(@RequestBody MateriaRequestDTO dto) {
        MateriaResponseDTO materiaSalva = service.cadastrarMateria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaSalva);
    }

    @GetMapping("/materias")
    public ResponseEntity<List<MateriaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PutMapping("/admin/materias/{id}")
    public ResponseEntity<MateriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody MateriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarMateria(id, dto));
    }

    @DeleteMapping("/admin/materias/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirMateria(id);
        return ResponseEntity.noContent().build();
    }
}