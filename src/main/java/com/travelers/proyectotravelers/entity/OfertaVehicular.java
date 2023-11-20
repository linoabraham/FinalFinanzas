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

    @Column(columnDefinition = "decimal(9,4)",nullable = false)
    private double seguroVehicularAnual;

    @Column(columnDefinition = "decimal(11,8)",nullable = false)
    private double seguroVehicularMensual;

    @Column(nullable = false,length = 30)
    private int plazo;

    private int plazosGraciaTotal;

    private int plazoGraciaParcial;

    @Column(columnDefinition = "decimal(6,2)",nullable = false)
    private double TEA;

    @Column(columnDefinition = "decimal(6,2)",nullable = false)
    private double desgravamen;

    private double COK;

}


