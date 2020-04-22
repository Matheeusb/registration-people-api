package br.com.matheus.people.api.controller.form;

import br.com.matheus.people.api.model.Person;
import br.com.matheus.people.api.repository.PersonRepository;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@Data
public class PersonForm {

    @NotNull @NotEmpty @Length(max = 50)
    private String name;

    @NotNull @Min(1)
    private Integer age;

    @NotNull @NotEmpty @Email @Length(max = 50)
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
