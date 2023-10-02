package com.travelers.proyectotravelers.service.impl;

import com.travelers.proyectotravelers.entity.Usuario;
import com.travelers.proyectotravelers.exception.ModelNotFount;
import com.travelers.proyectotravelers.repository.IUsuarioRepo;
import com.travelers.proyectotravelers.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepo repo;

    @Override
    public List<Usuario> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Usuario save(Usuario usuario) throws Exception {
        return repo.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) throws Exception {
        return repo.save(usuario);
    }

    @Override
    public Usuario findById(Integer id) throws Exception {
        return repo.findById(id).orElseThrow(() -> new ModelNotFount("ID NOT FOUNT"));
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        repo.deleteById(id);
    }
}
