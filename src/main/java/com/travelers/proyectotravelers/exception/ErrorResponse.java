package com.travelers.proyectotravelers.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private Integer statusCode;

    private LocalDateTime dateTime;

    private String message;

    private String path;


}
