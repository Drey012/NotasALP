package com.faculdade.notas.service;

import com.faculdade.notas.exception.RecursoNaoEncontradoException;
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

    public ProfessorResponseDTO atualizarProfessor(Long id, ProfessorRequestDTO dto) {
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor com ID " + id + " não encontrado."));

        if (!professor.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("O e-mail informado já está em uso por outro professor.");
        }

        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        Professor atualizado = repository.save(professor);
        return new ProfessorResponseDTO(atualizado.getId(), atualizado.getNome(), atualizado.getEmail());
    }

    public void excluirProfessor(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Professor com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }
}