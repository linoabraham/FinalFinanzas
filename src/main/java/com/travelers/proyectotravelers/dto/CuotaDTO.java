package com.travelers.proyectotravelers.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuotaDTO {

    private int numeroCuota;

    private double deuda;

    private double amortización;

    private double interes;

    private double cuota;

    private double seguroVeh;

    private double desgravamen;

    private double totalPagar;

    private double saldo;

}
