package com.travelers.proyectotravelers.security;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    //UNA INYECCION DE DEPENDENCIA PARA PODER GENERAR EL TOKEN
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //PREGUNTANDO SI EXISTE EL BLOQUE DE AUTHORIZATION
        final String tokenHeader = request.getHeader("Authorization");
        String nombre = null;
        String jwtToken = null;

        if(tokenHeader!=null){
            if(tokenHeader.startsWith("Bearer ")|| tokenHeader.startsWith("bearer ")){
                jwtToken = tokenHeader.substring(7);
                try {
                    nombre = jwtTokenUtil.getUsernameFromToken(jwtToken);
                }catch (Exception ex){
                    request.setAttribute("exception",ex.getMessage());
                }
            }
        }

        if (nombre != null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(nombre);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
