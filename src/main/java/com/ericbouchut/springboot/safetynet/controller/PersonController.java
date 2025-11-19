package com.ericbouchut.springboot.safetynet.controller;

import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public Set<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    // TODO: Should return a HTTP status Code
    @PostMapping
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
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestBody Person person) {
        if (personService.deletePerson(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
