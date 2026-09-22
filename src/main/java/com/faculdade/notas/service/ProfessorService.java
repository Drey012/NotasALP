package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.Professor;
import com.faculdade.notas.model.dto.request.ProfessorRequestDTO;
import com.faculdade.notas.model.dto.response.ProfessorResponseDTO;
import com.faculdade.notas.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public ProfessorResponseDTO cadastrarProfessor(ProfessorRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("O e-mail informado já está em uso por outro professor.");
        }

        Professor professor = new Professor();
        professor.setNome(dto.nome());
        professor.setEmail(dto.email());

        Professor salvo = repository.save(professor);
        return new ProfessorResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    public List<ProfessorResponseDTO> listarTodos() {
        return repository.findAll().stream().map(p ->
                new ProfessorResponseDTO(p.getId(), p.getNome(), p.getEmail())
        ).collect(Collectors.toList());
    }
}