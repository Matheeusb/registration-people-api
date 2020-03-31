package br.com.matheus.people.api.repository;

import br.com.matheus.people.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
