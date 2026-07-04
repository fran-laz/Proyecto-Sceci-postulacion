package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.repository.query.Param;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    @Query(value = "SELECT m.* FROM materias m JOIN carrera_materia cm ON m.id = cm.materia_id WHERE cm.carrera_id = :carreraId ORDER BY m.semestre ASC, m.nombre ASC", nativeQuery = true)
    List<Materia> buscarMateriasPorCarreraExacta(@Param("carreraId") Long carreraId);
}