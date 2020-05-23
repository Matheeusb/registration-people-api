package br.com.matheus.people.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponseDTO {

    private String field;
    private String error;

}
