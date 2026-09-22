package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.Professor;
import com.faculdade.notas.model.dto.request.ProfessorRequestDTO;
import com.faculdade.notas.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public Professor cadastrarProfessor(ProfessorRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("O e-mail informado já está em uso por outro professor.");
        }

        Professor professor = new Professor();
        professor.setNome(dto.nome());
        professor.setEmail(dto.email());

        return repository.save(professor);
    }
}