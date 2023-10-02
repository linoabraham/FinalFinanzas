package com.travelers.proyectotravelers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {

    private Integer idUsuario;

    @NotEmpty
    @Size(min = 3,max = 100)
    private String username;


    @NotEmpty
    @Size(min = 3,max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
