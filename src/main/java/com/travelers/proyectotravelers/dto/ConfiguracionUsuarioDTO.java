package com.travelers.proyectotravelers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.entity.OfertaVehicular;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionUsuarioDTO {

    private Integer idConfiguracion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Cliente cliente;

    private OfertaVehicular ofertaVehicular;

}
