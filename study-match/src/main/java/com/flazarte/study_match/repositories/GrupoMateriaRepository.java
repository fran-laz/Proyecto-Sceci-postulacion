package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.GrupoMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoMateriaRepository extends JpaRepository<GrupoMateria, Long> {
    List<GrupoMateria> findByMateriaId(Long materiaId);
}