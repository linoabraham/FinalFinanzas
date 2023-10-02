package com.travelers.proyectotravelers.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;



@Getter
@AllArgsConstructor
@ToString
public class JwtResponse {

    private final String token;

}