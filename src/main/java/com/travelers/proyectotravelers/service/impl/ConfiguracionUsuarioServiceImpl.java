package com.travelers.proyectotravelers.service.impl;

import com.travelers.proyectotravelers.entity.ConfiguracionUsuario;
import com.travelers.proyectotravelers.exception.ModelNotFount;
import com.travelers.proyectotravelers.repository.IConfiguracionUsuarioRepo;
import com.travelers.proyectotravelers.service.IConfiguracionUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracionUsuarioServiceImpl implements IConfiguracionUsuarioService {

    @Autowired
    private IConfiguracionUsuarioRepo configuracionUsuarioRepo;

    @Override
    public List<ConfiguracionUsuario> findAll() throws Exception {
        return configuracionUsuarioRepo.findAll();
    }

    @Override
    public ConfiguracionUsuario save(ConfiguracionUsuario configuracionUsuario) throws Exception {
        return configuracionUsuarioRepo.save(configuracionUsuario);
    }

    @Override
    public ConfiguracionUsuario update(ConfiguracionUsuario configuracionUsuario) throws Exception {
        return configuracionUsuarioRepo.save(configuracionUsuario);
    }

    @Override
    public ConfiguracionUsuario findById(Integer id) throws Exception {
        return configuracionUsuarioRepo.findById(id).orElseThrow(() -> new ModelNotFount("ID NOT FOUNT"));
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        configuracionUsuarioRepo.deleteById(id);
    }
}
