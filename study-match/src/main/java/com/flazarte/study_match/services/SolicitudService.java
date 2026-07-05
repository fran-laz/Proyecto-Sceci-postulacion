package com.flazarte.study_match.services;

import com.flazarte.study_match.dtos.solicitud.SolicitudRequestDTO;
import com.flazarte.study_match.dtos.solicitud.SolicitudResponseDTO;
import com.flazarte.study_match.models.GrupoEstudio;
import com.flazarte.study_match.models.Perfil;
import com.flazarte.study_match.models.Proyecto;
import com.flazarte.study_match.models.Solicitud;
import com.flazarte.study_match.models.Usuario;
import com.flazarte.study_match.repositories.GrupoEstudioRepository;
import com.flazarte.study_match.repositories.PerfilRepository;
import com.flazarte.study_match.repositories.ProyectoRepository;
import com.flazarte.study_match.repositories.SolicitudRepository;
import com.flazarte.study_match.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private GrupoEstudioRepository grupoEstudioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public SolicitudResponseDTO crearSolicitud(SolicitudRequestDTO dto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Perfil postulante = perfilRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Perfil del postulante no encontrado"));

        Solicitud solicitud = new Solicitud();
        solicitud.setPostulante(postulante);
        solicitud.setMensajePostulacion(dto.getMensajePostulacion() != null ? dto.getMensajePostulacion() : "Me gustaría unirme a tu equipo.");
        solicitud.setEstado("PENDIENTE");

        if (dto.getProyectoId() != null) {
            Proyecto proyecto = proyectoRepository.findById(dto.getProyectoId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
            solicitud.setProyecto(proyecto);
        } else if (dto.getGrupoEstudioId() != null) {
            GrupoEstudio grupo = grupoEstudioRepository.findById(dto.getGrupoEstudioId())
                    .orElseThrow(() -> new RuntimeException("Grupo de estudio no encontrado"));
            solicitud.setGrupoEstudio(grupo);
        } else {
            throw new RuntimeException("Debe especificar un ID de proyecto o de grupo de estudio");
        }

        Solicitud guardada = solicitudRepository.save(solicitud);
        return convertirParaBandeja(guardada);
    }

    @Transactional
    public SolicitudResponseDTO responderSolicitud(Long solicitudId, String respuesta) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!respuesta.equals("ACEPTADA") && !respuesta.equals("RECHAZADA")) {
            throw new RuntimeException("La respuesta debe ser ACEPTADA o RECHAZADA");
        }

        solicitud.setEstado(respuesta);

        if (respuesta.equals("ACEPTADA")) {
            if (solicitud.getProyecto() != null) {
                Proyecto proyecto = solicitud.getProyecto();
                if (proyecto.getIntegrantes() == null) {
                    proyecto.setIntegrantes(new java.util.ArrayList<>());
                }

                if (proyecto.getIntegrantes().size() >= proyecto.getMaximoIntegrantes()) {
                    throw new RuntimeException("El proyecto ya está lleno");
                }
                proyecto.getIntegrantes().add(solicitud.getPostulante());
                proyectoRepository.save(proyecto);

            } else if (solicitud.getGrupoEstudio() != null) {
                GrupoEstudio grupo = solicitud.getGrupoEstudio();
                if (grupo.getIntegrantes() == null) {
                    grupo.setIntegrantes(new java.util.ArrayList<>());
                }

                if (grupo.getIntegrantes().size() >= grupo.getMaximoIntegrantes()) {
                    throw new RuntimeException("El grupo de estudio ya está lleno");
                }
                grupo.getIntegrantes().add(solicitud.getPostulante());
                grupoEstudioRepository.save(grupo);
            }
        }

        return convertirADTO(solicitudRepository.save(solicitud));
    }

    public List<SolicitudResponseDTO> obtenerSolicitudesPendientesPorCreador(String email) {
        return solicitudRepository.findAll().stream()
                .filter(s -> s.getEstado().equals("PENDIENTE"))
                .filter(s -> (s.getProyecto() != null && s.getProyecto().getCreador().getUsuario().getEmail().equals(email)) ||
                        (s.getGrupoEstudio() != null && s.getGrupoEstudio().getCreador().getUsuario().getEmail().equals(email)))
                .map(this::convertirParaBandeja)
                .collect(Collectors.toList());
    }

    private SolicitudResponseDTO convertirParaBandeja(Solicitud solicitud) {
        SolicitudResponseDTO dto = convertirADTO(solicitud);
        dto.setNombreRemitente(solicitud.getPostulante().getNombres() + " " + solicitud.getPostulante().getApellidos());

        if (solicitud.getProyecto() != null) {
            dto.setTipo("proyecto");
            dto.setTituloDestino(solicitud.getProyecto().getTitulo());
        } else {
            dto.setTipo("grupo");
            dto.setTituloDestino(solicitud.getGrupoEstudio().getTitulo());
        }
        return dto;
    }

    private SolicitudResponseDTO convertirADTO(Solicitud solicitud) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(solicitud.getId());
        dto.setNombrePostulante(solicitud.getPostulante().getNombres() + " " + solicitud.getPostulante().getApellidos());
        dto.setCarreraPostulante(solicitud.getPostulante().getCarrera());
        dto.setMensajePostulacion(solicitud.getMensajePostulacion());
        dto.setEstado(solicitud.getEstado());

        if (solicitud.getProyecto() != null) {
            dto.setTipoPublicacion("PROYECTO");
            dto.setTituloPublicacion(solicitud.getProyecto().getTitulo());
        } else if (solicitud.getGrupoEstudio() != null) {
            dto.setTipoPublicacion("GRUPO DE ESTUDIO");
            dto.setTituloPublicacion(solicitud.getGrupoEstudio().getTitulo());
        }

        return dto;
    }
}