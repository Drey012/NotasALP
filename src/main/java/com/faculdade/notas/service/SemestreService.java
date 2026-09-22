package com.faculdade.notas.service;

import com.faculdade.notas.exception.RecursoNaoEncontradoException;
import com.faculdade.notas.model.Curso;
import com.faculdade.notas.model.Semestre;
import com.faculdade.notas.model.dto.request.SemestreRequestDTO;
import com.faculdade.notas.model.dto.response.SemestreResponseDTO;
import com.faculdade.notas.repository.CursoRepository;
import com.faculdade.notas.repository.SemestreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemestreService {

    private final SemestreRepository semestreRepository;
    private final CursoRepository cursoRepository;

    public SemestreService(SemestreRepository semestreRepository, CursoRepository cursoRepository) {
        this.semestreRepository = semestreRepository;
        this.cursoRepository = cursoRepository;
    }

    public SemestreResponseDTO cadastrarSemestre(SemestreRequestDTO dto) {
        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso com ID " + dto.cursoId() + " não foi encontrado."));

        Semestre semestre = new Semestre();
        semestre.setOrdem(dto.ordem());
        semestre.setCurso(curso);

        Semestre salvo = semestreRepository.save(semestre);

        return new SemestreResponseDTO(salvo.getId(), salvo.getOrdem(), curso.getNome());
    }

    public List<SemestreResponseDTO> listarTodos() {
        return semestreRepository.findAll().stream().map(s ->
                new SemestreResponseDTO(s.getId(), s.getOrdem(), s.getCurso().getNome())
        ).collect(Collectors.toList());
    }
}