package com.nexoservice.web.form;

import com.nexoservice.rol.entity.NombreRol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioForm {

    @NotBlank
    @Size(max = 60)
    private String username;

    @NotBlank
    @Email
    @Size(max = 120)
    private String correo;

    @Size(max = 120)
    private String password;

    @NotNull
    private NombreRol rol;

    private Long tecnicoId;

    private Boolean activo = true;
}
