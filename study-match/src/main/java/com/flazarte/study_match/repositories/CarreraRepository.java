package com.flazarte.study_match.repositories;

import com.flazarte.study_match.models.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    boolean existsByNombre(String nombre);
}