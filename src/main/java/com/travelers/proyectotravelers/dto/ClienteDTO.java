package com.travelers.proyectotravelers.dto;

import com.travelers.proyectotravelers.entity.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Integer idCliente;

    private UsuarioDTO usuario;


    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;


    @NotEmpty
    private String telefono;


    @NotEmpty
    private String direccion;


    @NotEmpty
    @Email
    private String email;

}
