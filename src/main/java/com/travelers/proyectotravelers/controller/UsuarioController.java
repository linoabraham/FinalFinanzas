package com.travelers.proyectotravelers.controller;

import com.travelers.proyectotravelers.dto.UsuarioDTO;
import com.travelers.proyectotravelers.entity.Usuario;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService service;

    @Autowired
    @Qualifier("usuarioMapper")
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<?> findAll() throws Exception{
        List<UsuarioDTO> lista = service.findAll()
                .stream()
                .map(usuario -> mapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam("id")Integer id) throws Exception{
        UsuarioDTO usuarioDTO = mapper.map(service.findById(id),UsuarioDTO.class);
        return new ResponseEntity<>(usuarioDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody UsuarioDTO usuarioDTO) throws Exception{
        Usuario usuario = service.save(mapper.map(usuarioDTO,Usuario.class));
        return new ResponseEntity<>(mapper.map(usuario,UsuarioDTO.class),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody UsuarioDTO usuarioDTO) throws Exception{
        Usuario usuario = service.update(mapper.map(usuarioDTO,Usuario.class));
        return new ResponseEntity<>(mapper.map(usuario,UsuarioDTO.class),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id) throws Exception{
        service.deleteById(id);
        return new ResponseEntity<>(Map.of("id_delete",id),HttpStatus.NO_CONTENT);
    }
}
