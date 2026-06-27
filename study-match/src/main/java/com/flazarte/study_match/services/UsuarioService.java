package com.flazarte.study_match.services;

import com.flazarte.study_match.dtos.auth.RegistroUsuarioDTO;
import com.flazarte.study_match.models.Perfil;
import com.flazarte.study_match.models.Usuario;
import com.flazarte.study_match.repositories.PerfilRepository;
import com.flazarte.study_match.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Transactional
    public String registrarUsuario(RegistroUsuarioDTO dto) {

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol("STUDENT"); // Rol por defecto para los nuevos registros

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        Perfil perfil = new Perfil();
        perfil.setNombres(dto.getNombres());
        perfil.setApellidos(dto.getApellidos());
        perfil.setCarrera(dto.getCarrera());
        perfil.setUsuario(usuarioGuardado);

        perfilRepository.save(perfil);

        return "Usuario registrado exitosamente con su perfil";
    }
}