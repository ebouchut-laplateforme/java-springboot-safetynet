package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.model.Data;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Set<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }

    public void createPerson(Person person) {
        personRepository.createPerson(person);
    }

    public boolean deletePerson(Person person) {
        return personRepository.deletePerson(person);
    }
}
