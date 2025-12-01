package com.ericbouchut.springboot.safetynet.controller;

import com.ericbouchut.springboot.safetynet.dto.ChildAlertDTO;
import com.ericbouchut.springboot.safetynet.dto.FloodDTO;
import com.ericbouchut.springboot.safetynet.dto.PersonInfoDTO;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
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
     *
     * @return all the persons (no duplicate)
     */
    @GetMapping("/person")
    public Set<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    /**
     * Create a person
     *
     * @param person the person to create
     */
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(
            @RequestBody
            @Valid  // Validates the JSON Body
            Person person
    ) {
        Optional<Person> personCreated = personService.createPerson(person);

        // If created successfully returns HTTP Status code 201, and the person JSONified as the body
        // Otherwise returns HTTP Status code 409 conflict if not created because this person already exists (namesake)
        return personCreated
                .map(p -> ResponseEntity
                    .status(HttpStatus.CREATED) // HTTP Status Code 201 (Created)
                    .body(p)
                ).orElseGet( () -> ResponseEntity
                    .status(HttpStatus.CONFLICT) // HTTP Status Code 409 (Conflict)
                    .build()
                );
    }

    /**
     * Remove the passed in person.
     * The unicity of a Person is based on their full name (that is the combination of firstName and lastName).
     *
     * @param person the Person to remove (only considering <code>firstName</code> and <code>lastName</code>)
     * @return a response with status Code 200 (Ok) if the person has been removed or 404 (Not Found) if it dopes not exist or the removal failed.
     */
    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(
            @RequestBody
            @Valid
            Person person
    ) {
        if (personService.deletePerson(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return the e-mails of people living in a city.
     *
     * @param city the city
     * @return a list of emails as a JSON
     */
    @GetMapping("/communityEmail")
    public List<String> getCityEmails(
            @RequestParam
            @NotEmpty  // Requires @Validated on the class
            String city
    ) {
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
            @RequestParam("firestation")
            @Positive
            Integer fireStationNumber
    ) {
        return personService.getPhoneNumbersByFireStation(fireStationNumber);
    }

    /**
     * Handles <code>GET /personInfo?firstName=FIRST_NAME&lastName=LAST_NAME</code>
     * requests.
     * If several people have the same (last?) name, they must all appear.
     *
     * @return a list of {@link PersonInfoDTO} each with the name, address, age, email, and medical history (medications, dosage, allergies) of each resident.
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfo(
            @RequestParam
            @NotBlank
            String firstName,

            @RequestParam
            @NotBlank
            String lastName
    ) {
        return personService.getPersonInfo(firstName, lastName);
    }

    /**
     * Create the custom finder REST endpoint to handle
     * <code>GET /childAlert?address=ADDRESS_HERE</code> requests
     * The response is a JSON with the list of children (age <= 18)
     * living at this address.
     * Each list entry contains a child's description:
     * <lu>
     * <li>first name</li>
     * <li>last name</li>
     * <li>age</li>
     * <li>list of other household members</li>
     * </lu>
     * If there are no children, this URL may return an empty List.
     *
     * @return a list of children (any individual aged 18 or under) living at this address.
     */
    // TODO: Add zip and city to distinguish children with same fist and last names living in different places
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlerts(
            @RequestParam
            @NotBlank
            String address
    ) {
        return personService.getChildAlerts(address);
    }

    /**
     * This REST endpoint handles requests such as <code>GET /flood/stations?stations=2,3</code>
     * and responds with a {@link FloodDTO} serialized as JSON.
     * The response contains a list of all households served by the fire station
     * grouped by address.
     * Each household member should include the name, phone number
     * and age of the residents,
     * and list their medical history (medications, dosage and allergies)
     * next to each name.
     *
     * @param fireStationNumbers List of fire station numbers
     * @return the list of all households served by the fire station.
     * <p>
     * See {@link FloodDTO} for the content of the expected response
     */
    @GetMapping("/flood/stations")
    public List<FloodDTO> getFloodDTO(
            @RequestParam("stations")
            @NotBlank
            List<@Positive Integer> fireStationNumbers
    ) {
        return personService.getFloodDTO(fireStationNumbers);
    }
}
