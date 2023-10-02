package com.travelers.proyectotravelers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfertaVehicularDTO {

    private Integer idVehiculo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double van;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double tir;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double cuotaMensual;

    /*
    @NotEmpty
    @Size(max = 50,min = 3)
    private String tipoAuto;

     */

    @NotEmpty
    @Size(max = 20,min = 3)
    private String moneda;

    private double ingresoMensual;

    private double precioVehiculo;

    @NotNull
    private double porcentajeCuotaInicial;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double cuotaInicial;

    private int plazo; //24 o 36 meses

    private int plazosGracia;

    private double TEA;

    private List<CuotaDTO> cuotas;

}
