package com.travelers.proyectotravelers.controller;

import com.travelers.proyectotravelers.dto.CuotaDTO;
import com.travelers.proyectotravelers.dto.OfertaVehicularDTO;
import com.travelers.proyectotravelers.entity.OfertaVehicular;
import com.travelers.proyectotravelers.service.IOfertaVehicularService;
import jakarta.validation.Valid;
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
public class OfertaVehicularController {

    //private final double seguroVehicularAnual = 0.0472; //en porcentaje 4.72%

    private final double seguroDesgravamen = 0.0005; //en porcenaje 0.050%

    //private final double seguroVehicularMensual = 0.0039333;


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
            double financiamiento = e.getPrecioVehiculo()-e.getCuotaInicial();
            //PARA EL VAN
            double tem = Math.pow(1 + (e.getTEA() / 100), 1.0 / 12) - 1;
            double cuotaMensual = financiamiento*(tem*(Math.pow(1+tem,e.getPlazo()))/(Math.pow(1+tem,e.getPlazo())-1));
            // +1 PARA INDICAR QUE VA DESDE EL PRIMERA MES
            double[] flujosEfectivo = new double[e.getPlazo() + 1];
            for (int i = 0; i < e.getPlazo(); i++) {
                if (i < e.getPlazosGracia()) {
                    flujosEfectivo[i] = e.getIngresoMensual();
                } else {
                    flujosEfectivo[i] = e.getIngresoMensual() - cuotaMensual;
                }
            }

            // Calcular el VAN
            double van = -e.getPrecioVehiculo();
            for (int i = 1; i <= e.getPlazo(); i++) {
                van += flujosEfectivo[i] / Math.pow(1 + tem, i);
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            e.setTir(Double.parseDouble(decimalFormat.format(tem)));
            e.setCuotaMensual(Double.parseDouble(decimalFormat.format(cuotaMensual)));
            e.setVan(Double.parseDouble(decimalFormat.format(van)));

            e.setCuotas(calcularCuotas(e,financiamiento,tem));
        });

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    private List<CuotaDTO> calcularCuotas(OfertaVehicularDTO ofertaVehicularDTO,double financiamiento,double tem) {
        List<CuotaDTO> cuotas = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        double interes = tem*(financiamiento);
        double deuda = financiamiento;
        for (int i = 0; i <= ofertaVehicularDTO.getPlazo(); i++) {
            if(i==0){
                cuotas.add(new CuotaDTO().builder()
                        .numeroCuota(0)
                        .deuda(financiamiento)
                        .saldo(financiamiento)
                        .build());
                continue;
            }
            CuotaDTO cuota = CuotaDTO.builder()
                    .numeroCuota(i)
                    .amortización(convertirDouble(decimalFormat,ofertaVehicularDTO.getCuotaMensual()-interes))
                    .interes(convertirDouble(decimalFormat,interes))
                    .deuda(convertirDouble(decimalFormat,deuda))
                    .cuota(ofertaVehicularDTO.getCuotaMensual())
                    .seguroVeh(ofertaVehicularDTO.getPrecioVehiculo()*ofertaVehicularDTO.getSeguroVehicularMensual())
                    .desgravamen(convertirDouble(decimalFormat,deuda*seguroDesgravamen))
                    .totalPagar(convertirDouble(decimalFormat,ofertaVehicularDTO.getCuotaMensual()+ofertaVehicularDTO.getPrecioVehiculo()*ofertaVehicularDTO.getSeguroVehicularMensual()+deuda*seguroDesgravamen))
                    .saldo(convertirDouble(decimalFormat,deuda-(ofertaVehicularDTO.getCuotaMensual()-interes)))
                    .build();
            cuotas.add(cuota);
            deuda -= (ofertaVehicularDTO.getCuotaMensual()-interes);
            interes = tem*(deuda);
            if(deuda <= 0){
                deuda=0;
            }
        }
        return cuotas;
    }

    private double convertirDouble(DecimalFormat decimalFormat,double numero){
        return Double.parseDouble(decimalFormat.format(numero));
    }

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam("id") Integer id) throws Exception {
        OfertaVehicularDTO ofertaVehicularDTO = mapper.map(ofertaVehicularService.findById(id), OfertaVehicularDTO.class);
        return new ResponseEntity<>(ofertaVehicularDTO, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> save(@Valid @RequestBody OfertaVehicularDTO ofertaVehicularDTO) throws Exception {
        double seguroVehicularMensual = (ofertaVehicularDTO.getSeguroVehicularAnual()*30.0)/36000.0;
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
