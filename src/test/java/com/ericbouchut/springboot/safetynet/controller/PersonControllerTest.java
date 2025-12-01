package com.ericbouchut.springboot.safetynet.controller;

import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    /**
     * ObjectMapper converts objects (models) to JSON (serialization) adn from JSON to objects (deserialization).
     */
    private final ObjectMapper objectMapper;

    /**
     * MockMvc is the tool that simulates HTTP requests
     */
    private final MockMvc mockMvc;

    /**
     * Mock the service since it is not loaded by <code>MockMvc</code>.
     */
    @MockitoBean
    private PersonService personService;

    @Autowired
    public PersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, PersonService personService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.personService = personService;
    }

    @Test
    void getAllPersonsTest() throws Exception {
        // Arrange (Given): Set up the test context (mocks)
        Set<Person> mockPersons = Set.of(
                Person.builder()
                        .firstName("Sophie")
                        .lastName("Moreau")
                        .address("12 rue des Lavandes")
                        .zip("13008")
                        .city("Marseille")
                        .phone("06 42 15 73 89")
                        .email("sophie.moreau@example.fr")
                        .build(),
                Person.builder()
                        .firstName("Thomas")
                        .lastName("Deckert")
                        .address("45 avenue du Prado")
                        .zip("13006")
                        .city("Marseille")
                        .phone("06 91 28 54 16")
                        .email("thomas.deckert@example.fr")
                        .build()
        );
        when(personService.getAllPersons()).thenReturn(mockPersons);


        mockMvc
                // Act (aka. Given): Simulate a GET /person HTTP request
                .perform(get("/person"))
//                .andDo(print())  // Print the response to stdout

                // Assert (aka. When): verify the expected outcome)
                .andExpectAll(
                        status().isOk(),  // HTTP Status Code 200
                        content().contentType(MediaType.APPLICATION_JSON), // Response body should use JSON
                        jsonPath("$").isArray(), // Response body should be an array
                        jsonPath("$.length()").value(2) // Response body array contains 2 items
                );
    }

    @Test
    void createPersonOk() throws Exception {
        // Arrange (Given): Set up the test context (mocks)
        Person person = Person.builder()
                .firstName("Sophie")
                .lastName("Moreau")
                .address("12 rue des Lavandes")
                .zip("13008")
                .city("Marseille")
                .phone("06 42 15 73 89")
                .email("sophie.moreau@example.fr")
                .build();
        // Mock the controller's method called when the REST endpoint "POST /persons" is invoked.
        // In other words, this simulates this method returns the successfully created Person.
        when(personService.createPerson(any(Person.class)))
                .thenReturn(Optional.of(person));

        mockMvc
                // Act (aka. Given): Simulate a GET /persons HTTP request
                .perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)          // Request body content in JSON
                                .content(objectMapper.writeValueAsString(person)) // Request body: person serialized as a JSON string
                )

                // Assert (aka. When): verify the expected outcome)
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.firstName").value("Sophie"),
                        jsonPath("$.lastName").value("Moreau"),
                        jsonPath("$.address").value("12 rue des Lavandes"),
                        jsonPath("$.zip").value("13008"),
                        jsonPath("$.city").value("Marseille"),
                        jsonPath("$.email").value("sophie.moreau@example.fr"),
                        jsonPath("$.phone").value("06 42 15 73 89")
                );
    }

    @Test
    void createPersonKo() throws Exception {
        // Arrange (Given): Set up the test context (mocks)
        Person requestPerson = Person.builder()
                .firstName("Sophie")
                .lastName("Moreau")
                .address("12 rue des Lavandes")
                .zip("13008")
                .city("Marseille")
                .phone("06 42 15 73 89")
                .email("sophie.moreau@example.fr")
                .build();
        // Simulate a namesake: a person with the same first and last names already exists
        when(personService.createPerson(any(Person.class)))
                .thenReturn(Optional.empty());

        mockMvc
                // Act (aka. Given): Simulate a POST /persons HTTP request
                .perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)                 // Request body uses JSON
                                .content(objectMapper.writeValueAsString(requestPerson)) // Request body: person as a JSON string
                )

                // Assert (aka. When): verify the expected outcome)
                .andExpect(status().isConflict());
    }

    @Test
    void deletePersonOk() throws Exception {
        // Arrange (Given): Set up the test context (mocks)
        Person person = Person.builder()
                .firstName("Sophie")
                .lastName("Moreau")
                .address("12 rue des Lavandes")
                .zip("13008")
                .city("Marseille")
                .phone("06 42 15 73 89")
                .email("sophie.moreau@example.fr")
                .build();
        when(personService.deletePerson(any(Person.class)))
                .thenReturn(true);

        mockMvc
                // Act (aka. When): Simulate a successfully person removal
                .perform(
                    delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)                 // Request body uses JSON
                        .content(objectMapper.writeValueAsString(person)) // Request body: person as a JSON string
                )

                // Assert (aka. Then)
                .andExpect(status().isOk()); // Expect HTTP Status Code 200
    }

    @Test
    void deletePersonKo() throws Exception {
        // Arrange (Given): Set up the test context (mocks)
        Person person = Person.builder()
                .firstName("Sophie")
                .lastName("Moreau")
                .address("12 rue des Lavandes")
                .zip("13008")
                .city("Marseille")
                .phone("06 42 15 73 89")
                .email("sophie.moreau@example.fr")
                .build();
        when(personService.deletePerson(any(Person.class)))
                .thenReturn(false);

        mockMvc
                // Act (aka. When): Simulate a successfully person removal
                .perform(
                        delete("/person")
                                .contentType(MediaType.APPLICATION_JSON)                 // Request body uses JSON
                                .content(objectMapper.writeValueAsString(person)) // Request body: person as a JSON string
                )

                // Assert (aka. Then)
                .andExpect(status().isNotFound()); // Expect HTTP Status Code 404
    }

    @Test
    void getCityEmails() {
        // TODO: Do it
    }

    @Test
    void getPhoneNumbersByFireStation() {
        // TODO: Do it
    }

    @Test
    void getPersonInfo() {
        // TODO: Do it
    }

    @Test
    void getChildAlerts() {
        // TODO: Do it
    }

    @Test
    void getFloodDTO() {
        // TODO: Do it
    }
}