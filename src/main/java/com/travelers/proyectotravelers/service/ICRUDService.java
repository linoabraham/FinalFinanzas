package com.travelers.proyectotravelers.service;

import java.util.List;

public interface ICRUDService<T,ID> {

    List<T> findAll() throws Exception;

    T save(T t) throws Exception;

    T update(T t) throws Exception;

    T findById(ID id) throws Exception;

    void deleteById(ID id) throws Exception;

}
