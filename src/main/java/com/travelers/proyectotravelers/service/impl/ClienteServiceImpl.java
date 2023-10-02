package com.travelers.proyectotravelers.service.impl;

import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.exception.ModelNotFount;
import com.travelers.proyectotravelers.repository.IClienteRepo;
import com.travelers.proyectotravelers.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteRepo clienteRepo;

    @Override
    public List<Cliente> findAll() throws Exception {
        return clienteRepo.findAll();
    }

    @Override
    public Cliente save(Cliente cliente) throws Exception {
        return clienteRepo.save(cliente);
    }

    @Override
    public Cliente update(Cliente cliente) throws Exception {
        return clienteRepo.save(cliente);
    }

    @Override
    public Cliente findById(Integer id) throws Exception {
        return clienteRepo.findById(id).orElseThrow(() -> new ModelNotFount("ID NOT FOUNT"));
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        clienteRepo.deleteById(id);
    }

    @Override
    public Cliente findOneByUsuario_Username(String username) {
        return clienteRepo.findOneByUsuario_Username(username);
    }

    @Override
    public Cliente findOneByNombre(String nombre) {
        return clienteRepo.findOneByNombre(nombre);
    }
}
