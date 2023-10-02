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

    private double saldo;

    private double cuota;

    //private LocalDateTime fecha;


}
