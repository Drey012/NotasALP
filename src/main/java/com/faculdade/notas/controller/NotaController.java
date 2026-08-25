package com.faculdade.notas.controller;

import com.faculdade.notas.model.ResultadoAvaliacao;
import com.faculdade.notas.model.dto.ProfessorDTO;
import com.faculdade.notas.model.dto.RequisicaoNotaDTO;
import com.faculdade.notas.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NotaController {

    private final NotaService notaService;

    @Autowired
    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @GetMapping("/professores")
    public ResponseEntity<List<ProfessorDTO>> listarProfessores() {
        return ResponseEntity.ok(notaService.listarProfessores());
    }

    @PostMapping("/avaliar")
    public ResponseEntity<ResultadoAvaliacao> avaliar(@RequestBody RequisicaoNotaDTO requisicao) {
        ResultadoAvaliacao resultado = notaService.avaliar(requisicao);
        return ResponseEntity.ok(resultado);
    }
}