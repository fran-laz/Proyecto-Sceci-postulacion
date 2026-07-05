package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.GrupoEstudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoEstudioRepository extends JpaRepository<GrupoEstudio, Long> {
    @Query("SELECT DISTINCT g FROM GrupoEstudio g WHERE g.creador.usuario.email = :email OR EXISTS (SELECT 1 FROM g.integrantes i WHERE i.usuario.email = :email)")
    List<GrupoEstudio> findMisGruposYParticipaciones(@Param("email") String email);
    List<GrupoEstudio> findByGrupoMateriaId(Long grupoMateriaId);
}