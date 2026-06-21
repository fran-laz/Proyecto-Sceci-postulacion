package com.flazarte.study_match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grupos_materias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrupoMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_grupo", nullable = false, length = 50)
    private String numeroGrupo;

    @Column(name = "nombre_docente", length = 100)
    private String nombreDocente;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;
}