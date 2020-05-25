package br.com.matheus.people.api.controller;

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
import java.util.List;

@Api(value = "People CRUD")
@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @ApiOperation(value = "Get people")
    @GetMapping
    public List<Person> list() {
        return personRepository.findAll();
    }

    @ApiOperation(value = "Get a single person by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Person> detail(@PathVariable Long id) {
        return personRepository.findById(id).
                map(ResponseEntity::ok).
                orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Create new person")
    @PostMapping
    @Transactional
    public ResponseEntity<Person> create(@RequestBody @Valid PersonForm personForm, UriComponentsBuilder uriBuilder) {
        return ResponseEntity.created(uriBuilder.build().toUri()).
                body(personRepository.save(personForm.convertToPerson()));
    }

    @ApiOperation(value = "Update a person")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody @Valid PersonForm personForm) {
        return personRepository.findById(id)
                .map(person -> {
                    personForm.updatePerson(id, personRepository);
                    return ResponseEntity.ok().body(person);
                }).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Remove a person")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remove(@PathVariable Long id) {
        return personRepository.findById(id)
                .map(person -> {
                    personRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
