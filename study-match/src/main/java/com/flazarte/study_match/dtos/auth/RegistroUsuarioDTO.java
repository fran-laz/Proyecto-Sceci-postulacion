package com.flazarte.study_match.dtos.auth;

import lombok.Data;

@Data
public class RegistroUsuarioDTO {
    private String email;
    private String password;
    private String nombres;
    private String apellidos;
    private String carrera;
}