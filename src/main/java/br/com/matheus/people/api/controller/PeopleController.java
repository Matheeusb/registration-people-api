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

    @ApiOperation(value = "Get people list")
    @GetMapping
    public List<PersonDTO> list() {
        List<Person> people = personRepository.findAll();
        return PersonDTO.convert(people);
    }

    @ApiOperation(value = "Get person by Id")
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> datail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.ok(new PersonDTO(person.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Create person")
    @PostMapping
    @Transactional
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonForm personForm, UriComponentsBuilder uriBuilder) {
        Person person = personForm.convert();
        personRepository.save(person);

        URI uri = uriBuilder.path("/people/{id}").buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(uri).body(new PersonDTO(person));
    }

    @ApiOperation(value = "Update person")
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

    @ApiOperation(value = "Remove person")
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
