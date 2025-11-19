package com.ericbouchut.springboot.safetynet.repository;

import com.ericbouchut.springboot.safetynet.model.Data;
import com.ericbouchut.springboot.safetynet.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class PersonRepository {
    private final Data data;

    public PersonRepository(Data data) {
        this.data = data;
    }

    public Set<Person> getAllPersons() {
        return data.getPersons();
    }

    public void createPerson(Person person) {
        data.getPersons().add(person);
    }

    public boolean deletePerson(Person person) {
        return data.getPersons().remove(person);
    }
}
