package com.ericbouchut.springboot.safetynet.repository;

import com.ericbouchut.springboot.safetynet.model.Data;
import com.ericbouchut.springboot.safetynet.model.Person;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonRepository {
    private final Data data;

    public PersonRepository(Data data) {
        this.data = data;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  CRUD methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Set<Person> getAllPersons() {
        return data.getPersons();
    }

    public void createPerson(Person person) {
        data.getPersons().add(person);
    }

    public boolean deletePerson(Person person) {
        return data.getPersons().remove(person);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public List<String> getEmailsByCity(String searchedCity) {
        if (searchedCity.isEmpty()){
            return Collections.emptyList();
        }

        return data.getPersons()
                .stream()
                .filter(person -> person.getCity() != null && person.getCity().equals(searchedCity))
                .map(Person::getEmail)
                .toList();
    }

    /**
     * Return the person living at a given address.
     *
     * @param address the address
     * @return the persons living at the passed-in address (noi duplicates)
     */
    public Set<Person> getPersonsByAddress(String address) {
        return data.getPersons()
                .stream()
                .filter(p -> p.getAddress() != null && p.getAddress().equals(address))
                .collect(Collectors.toSet());
    }

    /**
     * @return a set of persons with the same full name (i.e., first and last name)
     */
    public Set<Person> getPersonsByFirstNameAndLastName(String firstName, String lastName) {
        return data.getPersons()
                .stream()
                .filter(p -> p.getLastName().equals(lastName) && p.getFirstName().equals(firstName))
                .collect(Collectors.toSet());
    }

}
