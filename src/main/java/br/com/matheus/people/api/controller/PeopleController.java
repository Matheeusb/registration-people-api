package br.com.matheus.people.api.controller;

import br.com.matheus.people.api.controller.dto.PersonDTO;
import br.com.matheus.people.api.controller.form.PersonForm;
import br.com.matheus.people.api.model.Person;
import br.com.matheus.people.api.repository.PersonRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Api(value = "People CRUD")
@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @ApiOperation(value = "Get people")
    @GetMapping
    public List<PersonDTO> list() {
        List<Person> people = personRepository.findAll();

        return PersonDTO.convert(people);
    }

    @ApiOperation(value = "Get a single person by Id")
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> detail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.ok(new PersonDTO(person.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Create new person")
    @PostMapping
    @Transactional
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonForm personForm, UriComponentsBuilder uriBuilder) {
        Person person = personForm.convert();
        personRepository.save(person);
        URI uri = uriBuilder.path("/people/{id}").buildAndExpand(person.getId()).toUri();

        return ResponseEntity.created(uri).body(new PersonDTO(person));
    }

    @ApiOperation(value = "Update a person")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PersonDTO> update(@PathVariable Long id, @RequestBody @Valid PersonForm personForm) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isPresent()) {
            Person person = personForm.update(id, personRepository);
            return ResponseEntity.ok(new PersonDTO(person));
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Update a person`s data")
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<PersonDTO> updateData(@PathVariable Long id, @RequestBody PersonForm personForm) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isPresent()) {
            Person person = optional.get();

            if (personForm.getName() != null) {
                person.setName(personForm.getName());
            } else if (personForm.getEmail() != null) {
                person.setEmail(personForm.getEmail());
            } else {
                person.setAge(personForm.getAge());
            }

            return ResponseEntity.ok(new PersonDTO(person));
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Remove a person")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<PersonDTO> remove(@PathVariable Long id) {
        Optional<Person> optional = personRepository.findById(id);
        if (optional.isPresent()) {
            personRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
