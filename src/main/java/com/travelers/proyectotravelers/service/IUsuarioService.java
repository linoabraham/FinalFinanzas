package com.travelers.proyectotravelers.service;

import com.travelers.proyectotravelers.entity.Usuario;

public interface IUsuarioService extends ICRUDService<Usuario,Integer>{

    Usuario findOneByUsername(String username);

    Usuario findOneByUsernameAndPassword(String username,String password);
}
