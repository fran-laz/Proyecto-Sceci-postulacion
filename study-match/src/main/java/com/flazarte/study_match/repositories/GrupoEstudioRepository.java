package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.GrupoEstudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoEstudioRepository extends JpaRepository<GrupoEstudio, Long> {
    List<GrupoEstudio> findByGrupoMateriaMateriaId(Long materiaId);
}