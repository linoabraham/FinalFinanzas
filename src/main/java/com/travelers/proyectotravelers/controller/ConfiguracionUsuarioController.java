package com.travelers.proyectotravelers.controller;

import com.travelers.proyectotravelers.dto.ConfiguracionUsuarioDTO;
import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.entity.ConfiguracionUsuario;
import com.travelers.proyectotravelers.service.IClienteService;
import com.travelers.proyectotravelers.service.IConfiguracionUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuraciones")
@CrossOrigin("*")
public class ConfiguracionUsuarioController {

    @Autowired
    private IConfiguracionUsuarioService configuracionService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    @Qualifier("configuracionMapper")
    private ModelMapper mapper;


    /*
    @PostMapping("/consultas")
    public ResponseEntity<?> save(@RequestBody ConfiguracionUsuarioDTO configuracionUsuarioDTO, HttpServletRequest req) throws Exception{
        configuracionUsuarioDTO.setCliente(obtenerCliente(req));
        ConfiguracionUsuario configuracion = configuracionService
                .save(mapper.map(configuracionUsuarioDTO, ConfiguracionUsuario.class));
        return new ResponseEntity<>(mapper.map(configuracion,ConfiguracionUsuarioDTO.class), HttpStatus.CREATED);
    }

    private Cliente obtenerCliente(HttpServletRequest req){
        String token = jwtTokenUtil.getToken(req);
        String clienteNombre = jwtTokenUtil.getUsernameFromToken(token);
        return clienteService.findOneByNombre(clienteNombre);
    }*/


}
