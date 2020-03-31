package br.com.matheus.people.api.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDTO {

    private String field;
    private String error;

}
