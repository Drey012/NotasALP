package com.faculdade.notas.service;

import com.faculdade.notas.exception.RecursoNaoEncontradoException;
import com.faculdade.notas.model.Materia;
import com.faculdade.notas.model.Semestre;
import com.faculdade.notas.model.dto.request.MateriaRequestDTO;
import com.faculdade.notas.model.dto.response.MateriaResponseDTO;
import com.faculdade.notas.repository.MateriaRepository;
import com.faculdade.notas.repository.SemestreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final SemestreRepository semestreRepository;

    public MateriaService(MateriaRepository materiaRepository, SemestreRepository semestreRepository) {
        this.materiaRepository = materiaRepository;
        this.semestreRepository = semestreRepository;
    }

    public MateriaResponseDTO cadastrarMateria(MateriaRequestDTO dto) {
        Semestre semestre = semestreRepository.findById(dto.semestreId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Semestre com ID " + dto.semestreId() + " não foi encontrado."));

        Materia materia = new Materia();
        materia.setNome(dto.nome());
        materia.setSigla(dto.sigla());
        materia.setSemestre(semestre);

        Materia salva = materiaRepository.save(materia);

        return new MateriaResponseDTO(
                salva.getId(),
                salva.getNome(),
                salva.getSigla(),
                semestre.getOrdem(),
                semestre.getCurso().getNome()
        );
    }

    public List<MateriaResponseDTO> listarTodas() {
        return materiaRepository.findAll().stream().map(m ->
                new MateriaResponseDTO(
                        m.getId(),
                        m.getNome(),
                        m.getSigla(),
                        m.getSemestre().getOrdem(),
                        m.getSemestre().getCurso().getNome()
                )
        ).collect(Collectors.toList());
    }

    public MateriaResponseDTO atualizarMateria(Long id, MateriaRequestDTO dto) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria com ID " + id + " não encontrada."));

        Semestre semestre = semestreRepository.findById(dto.semestreId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Semestre com ID " + dto.semestreId() + " não encontrado."));

        materia.setNome(dto.nome());
        materia.setSigla(dto.sigla());
        materia.setSemestre(semestre);
        Materia atualizada = materiaRepository.save(materia);

        return new MateriaResponseDTO(
                atualizada.getId(),
                atualizada.getNome(),
                atualizada.getSigla(),
                semestre.getOrdem(),
                semestre.getCurso().getNome()
        );
    }

    public void excluirMateria(Long id) {
        if (!materiaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Matéria com ID " + id + " não encontrada.");
        }
        materiaRepository.deleteById(id);
    }
}