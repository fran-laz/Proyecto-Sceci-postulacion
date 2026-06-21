package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Optional<Materia> findByCodigo(String codigo);
}