package com.flazarte.study_match.services;

import com.flazarte.study_match.dtos.proyecto.ProyectoRequestDTO;
import com.flazarte.study_match.dtos.proyecto.ProyectoResponseDTO;
import com.flazarte.study_match.models.GrupoMateria;
import com.flazarte.study_match.models.Perfil;
import com.flazarte.study_match.models.Proyecto;
import com.flazarte.study_match.models.Usuario;
import com.flazarte.study_match.repositories.GrupoMateriaRepository;
import com.flazarte.study_match.repositories.PerfilRepository;
import com.flazarte.study_match.repositories.ProyectoRepository;
import com.flazarte.study_match.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private GrupoMateriaRepository grupoMateriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ProyectoResponseDTO crearProyecto(ProyectoRequestDTO dto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Error: Cuenta de usuario no encontrada"));
        Perfil creador = perfilRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Error: Perfil del creador no encontrado"));
        GrupoMateria grupoMateria = grupoMateriaRepository.findById(dto.getGrupoMateriaId())
                .orElseThrow(() -> new RuntimeException("Error: Paralelo/Materia no encontrado"));

        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo(dto.getTitulo());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setMaximoIntegrantes(dto.getMaximoIntegrantes());
        proyecto.setFechaLimite(dto.getFechaLimite());
        proyecto.setCreador(creador);
        proyecto.setGrupoMateria(grupoMateria);
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        return convertirADTO(proyectoGuardado);
    }

    public List<ProyectoResponseDTO> obtenerTodosLosProyectos() {
        return proyectoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProyectoResponseDTO> obtenerProyectosPorEstudiante(String email) {
        return proyectoRepository.findMisProyectosYParticipaciones(email).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }

    public List<ProyectoResponseDTO> obtenerDisponiblesPorMateria(Long grupoMateriaId) {
        return proyectoRepository.findByGrupoMateriaId(grupoMateriaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<String> obtenerIntegrantes(Long id) {
        Proyecto p = proyectoRepository.findById(id).orElseThrow();
        return p.getIntegrantes().stream()
                .map(perfil -> perfil.getNombres() + " " + perfil.getApellidos())
                .collect(Collectors.toList());
    }

    private ProyectoResponseDTO convertirADTO(Proyecto proyecto) {
        ProyectoResponseDTO dto = new ProyectoResponseDTO();
        dto.setId(proyecto.getId());
        dto.setTitulo(proyecto.getTitulo());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setMaximoIntegrantes(proyecto.getMaximoIntegrantes());
        dto.setFechaLimite(proyecto.getFechaLimite());

        dto.setNombreCreador(proyecto.getCreador().getNombres() + " " + proyecto.getCreador().getApellidos());
        dto.setNombreMateria(proyecto.getGrupoMateria().getMateria().getNombre());
        dto.setNumeroGrupo(proyecto.getGrupoMateria().getNumeroGrupo());
        dto.setNombreDocente(proyecto.getGrupoMateria().getNombreDocente());
        dto.setEmailCreador(proyecto.getCreador().getUsuario().getEmail());
        return dto;
    }
}