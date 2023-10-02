package com.travelers.proyectotravelers.repository;

import com.travelers.proyectotravelers.entity.Cliente;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteRepo extends IGenericRepo<Cliente,Integer>{

    Cliente findOneByUsuario_Username(String username);

    Cliente findOneByNombre(String nombre);

}
