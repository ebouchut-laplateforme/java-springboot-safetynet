package com.ericbouchut.springboot.safetynet.controller;

import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~
    //  REST Endpoints
    // ~~~~~~~~~~~~~~~~~~~~~~

    /**
     * List all the persons.
     * @return all the persons (no duplicate)
     */
    @GetMapping("/person/")
    public Set<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    /**
     * Create a person
     * @param person the person to create
     */
    // TODO: Should return a HTTP status Code
    @PostMapping("/person")
    public void createPerson(@RequestBody Person person) {
        personService.createPerson(person);
    }

    /**
     * Remove the passed in person.
     * The unicity of a Person is based on their full name (that is the combination of firstName and lastName).
     *
     * @param person the Person to remove (only considering <code>firstName</code> and <code>lastName</code>)
     * @return a response with status Code 200 (Ok) if the person has been removed or 404 (Not Found) if it dopes not exist or the removal failed.
     */
    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(@RequestBody Person person) {
        if (personService.deletePerson(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Return the e-mails of people living in a city.
     *
     * @param city the city
     * @return a list of emails as a JSON
     */
    @GetMapping("/communityEmail")
    public List<String> getCityEmails(
            @RequestParam("city") String city) {
        return personService.getCityEmails(city);
    }

    /**
     * Return the phone numbers of residents served by a fire station.
     *
     * @param fireStationNumber a fire station number
     * @return the phone numbers of residents served by the passed-in fire station
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFireStation(
            @RequestParam("firestation") Integer fireStationNumber
    ) {
        return personService.getPhoneNumbersByFireStation(fireStationNumber);
    }
}
