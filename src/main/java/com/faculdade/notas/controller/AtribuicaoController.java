package com.faculdade.notas.controller;

import com.faculdade.notas.model.dto.request.AtribuicaoRequestDTO;
import com.faculdade.notas.model.dto.response.AtribuicaoResponseDTO;
import com.faculdade.notas.service.AtribuicaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AtribuicaoController {

    private final AtribuicaoService service;

    public AtribuicaoController(AtribuicaoService service) {
        this.service = service;
    }

    @PostMapping("/admin/atribuicoes")
    public ResponseEntity<AtribuicaoResponseDTO> criar(@Valid @RequestBody AtribuicaoRequestDTO dto) {
        AtribuicaoResponseDTO criada = service.cadastrarAtribuicao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping("/atribuicoes")
    public ResponseEntity<List<AtribuicaoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PutMapping("/admin/atribuicoes/{id}")
    public ResponseEntity<AtribuicaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AtribuicaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarAtribuicao(id, dto));
    }

    @DeleteMapping("/admin/atribuicoes/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirAtribuicao(id);
        return ResponseEntity.noContent().build();
    }
}