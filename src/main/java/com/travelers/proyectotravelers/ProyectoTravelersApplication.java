package com.travelers.proyectotravelers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ProyectoTravelersApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoTravelersApplication.class, args);
    }

    /*
    @Override
    public void run(String... args) throws Exception {
                Scanner scanner = new Scanner(System.in);

                // Solicitar al usuario los datos necesarios
                System.out.print("Ingrese el monto del préstamo: ");
                double montoPrestamo = scanner.nextDouble();

                System.out.print("Ingrese la tasa de interés anual (porcentaje): ");
                double tasaInteresAnual = scanner.nextDouble();

                System.out.print("Ingrese el plazo del préstamo en meses: ");
                int plazoMeses = scanner.nextInt();

                System.out.print("Ingrese la cuota inicial: ");
                double cuotaInicial = scanner.nextDouble();

                // Convertir la tasa de interés anual a mensual
                double tasaInteresMensual = (tasaInteresAnual / 100) / 12;

                // Restar la cuota inicial al monto del préstamo
                double montoRealPrestamo = montoPrestamo - cuotaInicial;

                // Calcular la cuota mensual utilizando la fórmula de cuota de préstamo
                double cuotaMensual = (montoRealPrestamo * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, plazoMeses))
                        / (Math.pow(1 + tasaInteresMensual, plazoMeses) - 1);

                // Mostrar la cuota mensual
                System.out.println("La cuota mensual del préstamo es: $" + cuotaMensual);

                // Cerrar el escáner
                scanner.close();

    }

     */
}
