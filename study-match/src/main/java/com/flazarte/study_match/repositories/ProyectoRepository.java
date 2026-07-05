package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    @Query("SELECT DISTINCT p FROM Proyecto p WHERE p.creador.usuario.email = :email OR EXISTS (SELECT 1 FROM p.integrantes i WHERE i.usuario.email = :email)")
    List<Proyecto> findMisProyectosYParticipaciones(@Param("email") String email);
    List<Proyecto> findByGrupoMateriaId(Long grupoMateriaId);
}