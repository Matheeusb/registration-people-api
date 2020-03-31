package br.com.matheus.people.api.controller;

import br.com.matheus.people.api.controller.dto.PersonDTO;
import br.com.matheus.people.api.controller.form.PersonForm;
import br.com.matheus.people.api.model.Person;
import br.com.matheus.people.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public List<PersonDTO> list() {
        List<Person> people = personRepository.findAll();
        return PersonDTO.convert(people);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> datail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.ok(new PersonDTO(person.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonForm personForm, UriComponentsBuilder uriBuilder) {
        Person person = personForm.convert();
        personRepository.save(person);

        URI uri = uriBuilder.path("/people/{id}").buildAndExpand(person.getId()).toUri();
        return ResponseEntity.created(uri).body(new PersonDTO(person));
    }

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
