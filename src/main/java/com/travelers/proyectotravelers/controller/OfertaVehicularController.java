package com.travelers.proyectotravelers.controller;

import com.travelers.proyectotravelers.dto.CuotaDTO;
import com.travelers.proyectotravelers.dto.OfertaVehicularDTO;
import com.travelers.proyectotravelers.entity.OfertaVehicular;
import com.travelers.proyectotravelers.entity.Usuario;
import com.travelers.proyectotravelers.service.IOfertaVehicularService;
import jakarta.validation.Valid;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ofertas")
@CrossOrigin("*")
public class OfertaVehicularController {

    DecimalFormat decimalFormat = new DecimalFormat("#.####");

    @Autowired
    private IOfertaVehicularService ofertaVehicularService;

    @Autowired
    @Qualifier("ofertaVehicularMapper")
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<?> findAll() throws Exception {
        List<OfertaVehicularDTO> lista = ofertaVehicularService.findAll()
                .stream()
                .map(oferta -> mapper.map(oferta, OfertaVehicularDTO.class))
                .collect(Collectors.toList());
        lista.forEach(e -> {
            e.setCuotaInicial((e.getPorcentajeCuotaInicial() / 100) * e.getPrecioVehiculo());
            //EL PRECIO DEL VEHICULO ES COMO EL PRESTAMO (financiamiento)
            double financiamiento = e.getPrecioVehiculo() - e.getCuotaInicial();
            //PARA EL VAN
            double tem = Math.pow(1 + (e.getTEA() / 100), 1.0 / 12) - 1;
            tem = Double.parseDouble(decimalFormat.format(tem));
            e.setDesgravamen(0.0005 * financiamiento);
            double cuotaMensual = (financiamiento + e.getDesgravamen()) * (tem * (Math.pow(1 + tem, e.getPlazo())) / (Math.pow(1 + tem, e.getPlazo()) - 1));
            e.setCuotaMensual(Double.parseDouble(decimalFormat.format(cuotaMensual)));
            e.setCuotas(calcularCuotas(e, financiamiento + e.getDesgravamen(), tem));
            e.setVan(calcularVan(e.getCuotas(), e.getCOK(), financiamiento + e.getDesgravamen(), e));
        });

        calcularFlujosTir(lista);

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    private void calcularFlujosTir(List<OfertaVehicularDTO> lista) {
        List<Double> flujos = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            int plazos = 0;
            if (lista.get(i).getPlazosGraciaTotal() != 0 && lista.get(i).getPlazoGraciaParcial() == 0) {
                plazos = lista.get(i).getPlazo() - lista.get(i).getPlazosGraciaTotal();
            }
            if (lista.get(i).getPlazosGraciaTotal() == 0 && lista.get(i).getPlazoGraciaParcial() != 0) { //cuando hay gracia parcial
                plazos = lista.get(i).getPlazo();
            }
            if (lista.get(i).getPlazosGraciaTotal() == 0 && lista.get(i).getPlazoGraciaParcial() == 0) {
                plazos = lista.get(i).getPlazo();
            }
            for (int j = 0; j <= lista.get(i).getPlazo(); j++) {
                double flujo = lista.get(i).getCuotas().get(j).getFlujo();
                if (flujo != 0.0) {
                    flujos.add(flujo);
                }
            }
            lista.get(i).setFlujos(flujos);
            lista.get(i).setTir(calculateTIR(flujos, plazos));
            flujos = new ArrayList<>();
        }
    }

    // Método para calcular la TIR dado un arreglo de flujos de efectivo y la cantidad de flujos
    public static double calculateTIR(List<Double> flujos, int numeroFlujos) {
        double suposicion = 0.05;  // Suposición inicial para la tasa de descuento
        final double tolerancia = 0.0000000001;  // Tolerancia para la convergencia
        final int maximoIteraciones = 1000;  // Número máximo de iteraciones
        for (int i = 0; i < maximoIteraciones; i++) {
            double npv = 0;
            double derivative = 0;
            for (int t = 0; t <= numeroFlujos; t++) {
                double discountFactor = Math.pow(1 + suposicion, t);
                npv += flujos.get(t) / discountFactor;
                derivative -= t * flujos.get(t) / discountFactor / (1 + suposicion);
            }
            suposicion = suposicion - (npv / derivative);

            if (Math.abs(npv) < tolerancia) {
                return suposicion*100;
            }
        }
        throw new RuntimeException("No se alcanzó la convergencia después de " + maximoIteraciones + " iteraciones.");
    }

    private double calcularVan(List<CuotaDTO> flujos, double cok, double financiamiento, OfertaVehicularDTO ofertaVehicularDTO) {
        double van = 0;
        if (ofertaVehicularDTO.getPlazosGraciaTotal() != 0 && ofertaVehicularDTO.getPlazoGraciaParcial() == 0) {
            for (int i = 0; i <= ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazosGraciaTotal(); i++) {
                if (i == 0) {
                    van = flujos.get(ofertaVehicularDTO.getPlazosGraciaTotal()).getFlujo();
                    continue;
                }
                van += (flujos.get(ofertaVehicularDTO.getPlazosGraciaTotal() + i).getFlujo()) / (Math.pow((1 + cok), i));
            }
        } else if (ofertaVehicularDTO.getPlazosGraciaTotal() == 0 && ofertaVehicularDTO.getPlazoGraciaParcial() != 0) {
            van = -flujos.get(0).getDeuda();
            for (int i = 1; i <= ofertaVehicularDTO.getPlazo(); i++) {
                van += (flujos.get(i).getFlujo()) / (Math.pow((1 + cok), i));
            }
        }
        if (ofertaVehicularDTO.getPlazosGraciaTotal() == 0 && ofertaVehicularDTO.getPlazoGraciaParcial() == 0) {
            van = -flujos.get(0).getDeuda();
            for (int i = 1; i <= ofertaVehicularDTO.getPlazo(); i++) {
                van += (flujos.get(i).getFlujo()) / (Math.pow((1 + cok), i));
            }
        }
        return van;
    }

    //va a ser para calcular la las cuotas y el van
    private List<CuotaDTO> calcularCuotas(OfertaVehicularDTO ofertaVehicularDTO, double financiamiento, double tem) {
        List<CuotaDTO> cuotas = new ArrayList<>();
        //List<Double> flujos = new ArrayList<>();
        double interes = tem * (financiamiento);
        double deuda = financiamiento;
        double cuotaMensual = ofertaVehicularDTO.getCuotaMensual();
        DecimalFormat formatInteres = new DecimalFormat("#.##");
        for (int i = 0; i <= ofertaVehicularDTO.getPlazo(); i++) {
            if (i == 0 && (ofertaVehicularDTO.getPlazosGraciaTotal() == 0 && ofertaVehicularDTO.getPlazoGraciaParcial() == 0)) {
                cuotas.add(new CuotaDTO().builder()
                        .numeroCuota(0)
                        .deuda(financiamiento)
                        .saldo(financiamiento)
                        .flujo(-financiamiento)
                        .build());
                continue;
            } else if (i == 0 && (ofertaVehicularDTO.getPlazosGraciaTotal() > 0 || ofertaVehicularDTO.getPlazoGraciaParcial() == 0)) {
                cuotas.add(new CuotaDTO().builder()
                        .numeroCuota(0)
                        .deuda(financiamiento)
                        .saldo(financiamiento)
                        .build());
                continue;
            } else if (i == 0 && (ofertaVehicularDTO.getPlazosGraciaTotal() == 0 || ofertaVehicularDTO.getPlazoGraciaParcial() > 0)) {
                cuotas.add(new CuotaDTO().builder()
                        .numeroCuota(0)
                        .deuda(financiamiento)
                        .saldo(financiamiento)
                        .flujo(-financiamiento)
                        .build());
                continue;
            }
            if (ofertaVehicularDTO.getPlazosGraciaTotal() > 0 && ofertaVehicularDTO.getPlazoGraciaParcial() == 0) { //valido si hay plazos de gracia totales
                if (i <= ofertaVehicularDTO.getPlazosGraciaTotal()) {
                    CuotaDTO cuotaDTO = CuotaDTO.builder()
                            .numeroCuota(i)
                            .amortización(0)
                            .interes(0)
                            .deuda(convertirDouble(decimalFormat, deuda + interes + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual()))
                            .cuota(0)
                            .seguroVeh(0)
                            .totalPagar(0)
                            .saldo(convertirDouble(decimalFormat, deuda + interes + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual()))
                            .build();
                    if (i == ofertaVehicularDTO.getPlazosGraciaTotal()) {
                        cuotaDTO.setFlujo(convertirDouble(decimalFormat, -(deuda + interes + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual())));
                        //flujos.add(convertirDouble(decimalFormat,-(deuda+interes+ofertaVehicularDTO.getPrecioVehiculo()*ofertaVehicularDTO.getSeguroVehicularMensual())));
                    }
                    cuotas.add(cuotaDTO);
                    deuda = cuotaDTO.getSaldo();
                    interes = tem * (deuda);
                    cuotaMensual = (cuotaDTO.getSaldo()) * (tem * (Math.pow(1 + tem, ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazosGraciaTotal())) / (Math.pow(1 + tem, ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazosGraciaTotal()) - 1));
                    continue;
                }
                ofertaVehicularDTO.setCuotaMensual(cuotaMensual);
            } else if (ofertaVehicularDTO.getPlazosGraciaTotal() == 0 && ofertaVehicularDTO.getPlazoGraciaParcial() > 0) { //si hay plazos de gracia parciales
                if (i <= ofertaVehicularDTO.getPlazoGraciaParcial()) {
                    CuotaDTO cuotaDTO = CuotaDTO.builder()
                            .numeroCuota(i)
                            .amortización(0)
                            .interes(0)
                            .deuda(convertirDouble(decimalFormat, deuda))
                            .cuota((deuda) * (tem * (Math.pow(1 + tem, ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazoGraciaParcial())) / (Math.pow(1 + tem, ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazoGraciaParcial()) - 1)))
                            .seguroVeh(0)
                            .totalPagar(convertirDouble(decimalFormat, cuotaMensual + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual()))
                            .saldo(convertirDouble(decimalFormat, deuda))
                            .flujo(Double.parseDouble(formatInteres.format(interes)) + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual())
                            .build();
                    //flujos.add(Double.parseDouble(formatInteres.format(interes))+ofertaVehicularDTO.getPrecioVehiculo()*ofertaVehicularDTO.getSeguroVehicularMensual());
                    cuotas.add(cuotaDTO);
                    deuda = cuotaDTO.getDeuda();
                    interes = tem * (deuda);
                    cuotaMensual = (deuda) * (tem * (Math.pow(1 + tem, ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazoGraciaParcial())) / (Math.pow(1 + tem, ofertaVehicularDTO.getPlazo() - ofertaVehicularDTO.getPlazoGraciaParcial()) - 1));
                    continue;
                }
                ofertaVehicularDTO.setCuotaMensual(cuotaMensual);
            }
            CuotaDTO cuota = CuotaDTO.builder()
                    .numeroCuota(i)
                    .amortización(convertirDouble(decimalFormat, cuotaMensual - interes))
                    .interes(convertirDouble(decimalFormat, interes))
                    .deuda(convertirDouble(decimalFormat, deuda))
                    .cuota(cuotaMensual)
                    .seguroVeh(ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual())
                    .totalPagar(convertirDouble(decimalFormat, cuotaMensual + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual()))
                    .saldo(convertirDouble(decimalFormat, deuda - (cuotaMensual - interes)))
                    .flujo(convertirDouble(decimalFormat, cuotaMensual + ofertaVehicularDTO.getPrecioVehiculo() * ofertaVehicularDTO.getSeguroVehicularMensual()))
                    .build();
            //flujos.add(convertirDouble(decimalFormat,cuotaMensual+ofertaVehicularDTO.getPrecioVehiculo()*ofertaVehicularDTO.getSeguroVehicularMensual()));
            cuotas.add(cuota);
            deuda -= (cuotaMensual - interes);
            interes = tem * (deuda);
            if (cuota.getSaldo() < 0) {
                cuota.setSaldo(0);
            }
        }
        //ofertaVehicularDTO.setFlujos(flujos);
        return cuotas;
    }

/*
    public static double calculateTIR(List<Double> flujos, int periodo) {
        double guess = 0.05; // Suposición inicial más cercana al valor esperado
        double tolerance = 1e-10; // Tolerancia más baja para una mayor precisión
        int maxIterations = 1000; // Número máximo de iteraciones

        double previousGuess = guess + 1.0; // Valor inicial para la iteración

        for (int i = 0; i < maxIterations; i++) {
            double f = 0.0;
            double fPrime = 0.0;

            for (int t = 0; t < periodo; t++) {
                f += flujos.get(t) / Math.pow(1 + guess, t);
                fPrime += -t * flujos.get(t) / Math.pow(1 + guess, t + 1);
            }

            guess = guess - f / fPrime; // Fórmula de Newton-Raphson

            if (Math.abs(guess - previousGuess) < tolerance) {
                return guess;
            }

            previousGuess = guess;
        }

        // Si no se alcanza la precisión deseada, se retorna un valor NaN (No es un número)
        return 0;
    }*/


    private double convertirDouble(DecimalFormat decimalFormat, double numero) {
        return Double.parseDouble(decimalFormat.format(numero));
    }

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam("id") Integer id) throws Exception {
        OfertaVehicularDTO ofertaVehicularDTO = mapper.map(ofertaVehicularService.findById(id), OfertaVehicularDTO.class);
        return new ResponseEntity<>(ofertaVehicularDTO, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> save(@Valid @RequestBody OfertaVehicularDTO ofertaVehicularDTO) throws Exception {
        double seguroVehicularMensual = (ofertaVehicularDTO.getSeguroVehicularAnual() * 30.00) / 36000.00;
        ofertaVehicularDTO.setSeguroVehicularMensual(seguroVehicularMensual);
        OfertaVehicular oferta = ofertaVehicularService.save(mapper.map(ofertaVehicularDTO, OfertaVehicular.class));
        return new ResponseEntity<>(mapper.map(oferta, OfertaVehicularDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody OfertaVehicularDTO ofertaVehicularDTO) throws Exception {
        OfertaVehicular cliente = ofertaVehicularService.update(mapper.map(ofertaVehicularDTO, OfertaVehicular.class));
        return new ResponseEntity<>(mapper.map(cliente, OfertaVehicularDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id) throws Exception {
        ofertaVehicularService.deleteById(id);
        return new ResponseEntity<>(Map.of("id_delete", id), HttpStatus.NO_CONTENT);
    }

}
