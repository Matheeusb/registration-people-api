package br.com.matheus.people.api.controller.dto;

import br.com.matheus.people.api.model.Person;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PersonDTO {

    private Long id;
    private String name;
    private Integer age;
    private String email;

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.age = person.getAge();
        this.email = person.getEmail();
    }

    public static List<PersonDTO> convert(List<Person> people) {
        return people.stream().map(PersonDTO::new).collect(Collectors.toList());
    }
}
