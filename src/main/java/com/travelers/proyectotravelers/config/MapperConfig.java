package com.travelers.proyectotravelers.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

@Configuration
public class MapperConfig {

    @Bean(name = "usuarioMapper")
    public ModelMapper usuarioMapper(){
        return new ModelMapper();
    }


    @Bean(name = "clienteMapper")
    public ModelMapper clienteMapper(){
        return new ModelMapper();
    }

    @Bean(name = "ofertaVehicularMapper")
    public ModelMapper ofertaVehicularMapper(){
        return new ModelMapper();
    }

    @Bean(name = "configuracionMapper")
    public ModelMapper configuracionMapper(){
        return new ModelMapper();
    }

}
