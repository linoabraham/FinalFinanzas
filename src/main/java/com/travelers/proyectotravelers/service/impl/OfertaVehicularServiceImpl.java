package com.travelers.proyectotravelers.service.impl;

import com.travelers.proyectotravelers.entity.OfertaVehicular;
import com.travelers.proyectotravelers.exception.ModelNotFount;
import com.travelers.proyectotravelers.repository.IOfertaVehicularRepo;
import com.travelers.proyectotravelers.service.IOfertaVehicularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfertaVehicularServiceImpl implements IOfertaVehicularService {

    @Autowired
    private IOfertaVehicularRepo ofertaVehicularRepo;

    @Override
    public List<OfertaVehicular> findAll() throws Exception {
        return ofertaVehicularRepo.findAll();
    }

    @Override
    public OfertaVehicular save(OfertaVehicular ofertaVehicular) throws Exception {
        return ofertaVehicularRepo.save(ofertaVehicular);
    }

    @Override
    public OfertaVehicular update(OfertaVehicular ofertaVehicular) throws Exception {
        return ofertaVehicularRepo.save(ofertaVehicular);
    }

    @Override
    public OfertaVehicular findById(Integer id) throws Exception {
        return ofertaVehicularRepo.findById(id).orElseThrow(() -> new ModelNotFount("ID NOT FOUNT"));
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        ofertaVehicularRepo.deleteById(id);
    }
}
