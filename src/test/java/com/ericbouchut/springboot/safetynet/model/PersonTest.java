package com.ericbouchut.springboot.safetynet.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void hasFullName() {
        Person person = Person.builder()
                .firstName("Felicia")
                .lastName("Boyd")
                .address("1509 Culver St")
                .city("Culver")
                .zip("97451")
                .phone("841-874-6544")
                .build();

        assertTrue(person.hasFullName("Felicia", "Boyd"));

        assertFalse(person.hasFullName("John", "Boyd"));
        assertFalse(person.hasFullName("Felicia", "Carman"));
        assertFalse(person.hasFullName("Tessa", "Carman"));
    }
}