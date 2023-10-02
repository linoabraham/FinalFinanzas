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
            //EL PRECIO DEL VEHICULO ES COMO EL PRESTAMO
            double prestamo = e.getPrecioVehiculo()-e.getCuotaInicial();
            //PARA EL VAN
            double tem = Math.pow(1 + (e.getTEA() / 100), 1.0 / 12) - 1;
            double cuotaMensual = (prestamo * tem) / (1 - Math.pow(1 + tem, -e.getPlazo()));
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
                double valorPresente = flujosEfectivo[i] / Math.pow(1 + tem, i);
                van += valorPresente;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            e.setCuotaMensual(Double.parseDouble(decimalFormat.format(cuotaMensual)));
            e.setVan(Double.parseDouble(decimalFormat.format(van)));

            e.setCuotas(calcularCuotas(e,prestamo));
        });

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    private List<CuotaDTO> calcularCuotas(OfertaVehicularDTO ofertaVehicularDTO,double prestamo) {
        List<CuotaDTO> cuotas = new ArrayList<>();
        double saldo = prestamo;
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        for (int i = 1; i <= ofertaVehicularDTO.getPlazo(); i++) {
            CuotaDTO cuota = CuotaDTO.builder()
                    .cuota(ofertaVehicularDTO.getCuotaMensual())
                    .numeroCuota(i)
                    .saldo(Double.parseDouble(decimalFormat.format(saldo)))
                    .build();
            cuotas.add(cuota);
            saldo -= ofertaVehicularDTO.getCuotaMensual();
        }
        return cuotas;
    }

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam("id") Integer id) throws Exception {
        OfertaVehicularDTO ofertaVehicularDTO = mapper.map(ofertaVehicularService.findById(id), OfertaVehicularDTO.class);
        return new ResponseEntity<>(ofertaVehicularDTO, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> save(@Valid @RequestBody OfertaVehicularDTO ofertaVehicularDTO) throws Exception {
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
