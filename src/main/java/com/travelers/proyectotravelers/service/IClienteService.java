package com.travelers.proyectotravelers.service;

import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.entity.Usuario;

public interface IClienteService extends ICRUDService<Cliente,Integer>{

    Cliente findOneByUsuario_Username(String username);

    Cliente findOneByNombre(String nombre);


}
