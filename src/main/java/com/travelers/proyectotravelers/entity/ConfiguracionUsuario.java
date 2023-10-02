package com.travelers.proyectotravelers.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConfiguracion;

    @ManyToOne
    @JoinColumn(name = "idCliente",nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idVehiculo",nullable = false)
    private OfertaVehicular ofertaVehicular;


}
