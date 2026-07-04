package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByCreador_Usuario_Email(String email);
}