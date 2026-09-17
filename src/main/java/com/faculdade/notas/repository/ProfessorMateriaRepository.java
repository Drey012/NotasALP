package com.faculdade.notas.repository;

import com.faculdade.notas.model.ProfessorMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProfessorMateriaRepository extends JpaRepository<ProfessorMateria, Long> {

    // Busca todas as matérias e regras vinculadas a um professor específico
    List<ProfessorMateria> findByProfessorId(Long professorId);

    // Busca a regra de cálculo exata cruzando o ID do Professor e o ID da Matéria
    Optional<ProfessorMateria> findByProfessorIdAndMateriaId(Long professorId, Long materiaId);
}