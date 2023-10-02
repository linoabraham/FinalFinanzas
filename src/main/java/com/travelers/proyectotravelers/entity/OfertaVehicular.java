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
public class OfertaVehicular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVehiculo;

    @Column(columnDefinition = "decimal(8,2)",nullable = false)
    private double van;

    @Column(columnDefinition = "decimal(8,2)",nullable = false)
    private double tir;

    @Column(nullable = false,length = 20)
    private String moneda;

    @Column(columnDefinition = "decimal(8,2)",nullable = false)
    private double ingresoMensual;

    @Column(columnDefinition = "decimal(8,2)",nullable = false)
    private double precioVehiculo;

    @Column(columnDefinition = "decimal(8,2)",nullable = false)
    private double porcentajeCuotaInicial;

    @Column(nullable = false,length = 30)
    private int plazo;

    @Column(nullable = false,length = 30)
    private int plazosGracia;

    @Column(columnDefinition = "decimal(6,2)",nullable = false)
    private double TEA;

}


