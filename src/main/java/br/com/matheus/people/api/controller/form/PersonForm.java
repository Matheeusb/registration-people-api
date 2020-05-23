package br.com.matheus.people.api.controller.form;

import br.com.matheus.people.api.model.Person;
import br.com.matheus.people.api.repository.PersonRepository;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PersonForm {

    @NotNull @NotEmpty @Size(max = 50)
    private String name;

    @NotNull @Positive
    private Integer age;

    @NotNull @NotEmpty @Email @Size(max = 50)
    private String email;

    public Person convert() {
        return new Person(name, age, email);
    }

    public Person update(Long id, PersonRepository personRepository) {
        Person person = personRepository.getOne(id);
        person.setName(this.name);
        person.setAge(this.age);
        person.setEmail(this.email);

        return person;
    }
}
