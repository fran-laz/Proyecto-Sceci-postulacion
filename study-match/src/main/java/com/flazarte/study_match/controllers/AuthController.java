package com.flazarte.study_match.controllers;

import com.flazarte.study_match.config.JwtUtil;
import com.flazarte.study_match.dtos.auth.AuthResponseDTO;
import com.flazarte.study_match.dtos.auth.LoginRequestDTO;
import com.flazarte.study_match.dtos.auth.RegistroUsuarioDTO;
import com.flazarte.study_match.models.Usuario;
import com.flazarte.study_match.repositories.UsuarioRepository;
import com.flazarte.study_match.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody RegistroUsuarioDTO dto) {
        try {
            String mensaje = usuarioService.registrarUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Comparamos la contraseña en texto plano con el Hash de la BD
            if (passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
                String token = jwtUtil.generarToken(usuario.getEmail());
                return ResponseEntity.ok(new AuthResponseDTO(token));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }
}