package com.faculdade.notas.service;

import com.faculdade.notas.exception.RecursoNaoEncontradoException;
import com.faculdade.notas.model.Materia;
import com.faculdade.notas.model.Professor;
import com.faculdade.notas.model.ProfessorMateria;
import com.faculdade.notas.model.dto.request.AtribuicaoRequestDTO;
import com.faculdade.notas.model.dto.response.AtribuicaoResponseDTO;
import com.faculdade.notas.repository.MateriaRepository;
import com.faculdade.notas.repository.ProfessorMateriaRepository;
import com.faculdade.notas.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtribuicaoService {

    private final ProfessorMateriaRepository atribuicaoRepository;
    private final ProfessorRepository professorRepository;
    private final MateriaRepository materiaRepository;

    public AtribuicaoService(ProfessorMateriaRepository atribuicaoRepository,
                             ProfessorRepository professorRepository,
                             MateriaRepository materiaRepository) {
        this.atribuicaoRepository = atribuicaoRepository;
        this.professorRepository = professorRepository;
        this.materiaRepository = materiaRepository;
    }

    public AtribuicaoResponseDTO cadastrarAtribuicao(AtribuicaoRequestDTO dto) {
        Professor professor = professorRepository.findById(dto.professorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor ID " + dto.professorId() + " não encontrado."));

        Materia materia = materiaRepository.findById(dto.materiaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria ID " + dto.materiaId() + " não encontrada."));

        ProfessorMateria pm = new ProfessorMateria();
        pm.setProfessor(professor);
        pm.setMateria(materia);
        pm.setTurno(dto.turno());
        pm.setJsonFormula(dto.jsonFormula());

        ProfessorMateria salva = atribuicaoRepository.save(pm);

        return new AtribuicaoResponseDTO(
                salva.getId(),
                professor.getNome(),
                materia.getNome(),
                salva.getTurno(),
                salva.getJsonFormula()
        );
    }

    public List<AtribuicaoResponseDTO> listarTodas() {
        return atribuicaoRepository.findAll().stream().map(pm ->
                new AtribuicaoResponseDTO(
                        pm.getId(),
                        pm.getProfessor().getNome(),
                        pm.getMateria().getNome(),
                        pm.getTurno(),
                        pm.getJsonFormula()
                )
        ).collect(Collectors.toList());
    }
}