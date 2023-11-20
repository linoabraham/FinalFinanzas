package com.travelers.proyectotravelers.repository;

import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.entity.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepo extends IGenericRepo<Usuario,Integer>{

    Usuario findOneByUsername(String username);

    Usuario findOneByUsernameAndPassword(String username,String password);

}
