package com.faculdade.notas.service;

import com.faculdade.notas.exception.RegraNegocioException;
import com.faculdade.notas.model.Curso;
import com.faculdade.notas.model.dto.request.CursoRequestDTO;
import com.faculdade.notas.model.dto.response.CursoResponseDTO;
import com.faculdade.notas.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public CursoResponseDTO cadastrarCurso(CursoRequestDTO dto) {
        if (repository.existsBySigla(dto.sigla())) {
            throw new RegraNegocioException("Já existe um curso cadastrado com a sigla: " + dto.sigla());
        }

        Curso curso = new Curso();
        curso.setNome(dto.nome());
        curso.setSigla(dto.sigla());

        Curso cursoSalvo = repository.save(curso);

        return new CursoResponseDTO(cursoSalvo.getId(), cursoSalvo.getNome(), cursoSalvo.getSigla());
    }

    public List<CursoResponseDTO> listarTodos() {
        List<Curso> cursos = repository.findAll();

        return cursos.stream().map(curso ->
                new CursoResponseDTO(
                        curso.getId(),
                        curso.getNome(),
                        curso.getSigla()
                )
        ).collect(Collectors.toList());
    }
}