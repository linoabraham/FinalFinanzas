package com.travelers.proyectotravelers.security;

import com.travelers.proyectotravelers.entity.Cliente;
import com.travelers.proyectotravelers.repository.IClienteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IClienteRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = repo.findOneByUsuario_Username(username);
        if(cliente ==null){
            throw new UsernameNotFoundException("User not fount");
        }
        //ESTO ES PARA ROLES
        List<GrantedAuthority> listRoles = new ArrayList<>();
        listRoles.add(new SimpleGrantedAuthority("USER"));
        return new User(cliente.getUsuario().getUsername(),cliente.getUsuario().getPassword(),listRoles);
    }
}
