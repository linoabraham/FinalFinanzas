package com.travelers.proyectotravelers.controller;

import com.travelers.proyectotravelers.dto.ClienteDTO;
import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.service.IClienteService;
import com.travelers.proyectotravelers.service.IUsuarioService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    @Qualifier("clienteMapper")
    private ModelMapper mapper;


    @GetMapping
    public ResponseEntity<?> findAll() throws Exception{
        List<ClienteDTO> lista = clienteService.findAll()
                .stream()
                .map(cliente -> mapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/username")
    public ResponseEntity<?> findByUsername(@RequestParam("username")String username) throws Exception{
        return new ResponseEntity<>(clienteService.findOneByUsuario_Username(username),HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam("id")Integer id) throws Exception{
        ClienteDTO clienteDTO = mapper.map(clienteService.findById(id),ClienteDTO.class);
        return new ResponseEntity<>(clienteDTO,HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> save(@Valid @RequestBody ClienteDTO clienteDTO) throws Exception{
        Cliente cliente = clienteService.save(mapper.map(clienteDTO,Cliente.class));
        return new ResponseEntity<>(mapper.map(cliente,ClienteDTO.class),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ClienteDTO clienteDTO) throws Exception{
        Cliente cliente = clienteService.update(mapper.map(clienteDTO,Cliente.class));
        return new ResponseEntity<>(mapper.map(cliente,ClienteDTO.class),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id) throws Exception{
        clienteService.deleteById(id);
        return new ResponseEntity<>(Map.of("id_delete",id),HttpStatus.NO_CONTENT);
    }
}
