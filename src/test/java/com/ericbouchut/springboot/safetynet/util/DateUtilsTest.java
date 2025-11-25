package com.ericbouchut.springboot.safetynet.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void calculateAge() {
        // Fix the current date returned by LocalDate.now() to 2025-11-20
        Clock fixedClock = Clock.fixed(
                Instant.parse("2025-11-20T00:00:00Z"),
                ZoneId.systemDefault()
        );

        LocalDate dateOfBirth = LocalDate.of(1900, 11, 20);
        assertEquals(125, DateUtils.calculateAge(dateOfBirth, fixedClock));

        dateOfBirth = LocalDate.of(1900, 11, 21);
        assertEquals(124, DateUtils.calculateAge(dateOfBirth, fixedClock));
    }
}