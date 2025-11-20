package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.repository.FireStationRepository;
import com.ericbouchut.springboot.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class PersonServiceTest {

    @Autowired
    private  PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    /**
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#clock()
     */
    @Test
    void calculateAge() {
        // Fix the current date returned by LocalDate.now() to 2025-11-20
        Clock fixedClock = Clock.fixed(
                Instant.parse("2025-11-20T00:00:00Z"),
                ZoneId.systemDefault()
        );

        // Create a new instance of PersonService with a fixed current date
        PersonService personService = new PersonService(
                personRepository,
                fireStationRepository,
                fixedClock
        );
        LocalDate dateOfBirth = LocalDate.of(1900, 11, 20);

        assertEquals(125, personService.calculateAge(dateOfBirth));

        dateOfBirth = LocalDate.of(1900, 11, 21);

        assertEquals(124, personService.calculateAge(dateOfBirth));
    }
}