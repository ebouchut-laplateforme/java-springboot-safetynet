package com.ericbouchut.springboot.safetynet.service;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class DateServiceTest {

    @Test
    void calculateAgeTest() {
        LocalDate dateOfBirth = LocalDate.of(1926, 11, 20);

        // Fix the current date returned by LocalDate.now() to the **day before** the birthday date: 2026-11-19
        DateService dateService = new DateService(
                Clock.fixed(
                        Instant.parse("2026-11-19T00:00:00Z"),
                        ZoneId.systemDefault()
                )
        );
        assertEquals(99, dateService.calculateAge(dateOfBirth));

        // Fix the current date returned by LocalDate.now() to the birthday date: 2026-11-20
        dateService = new DateService(
                Clock.fixed(
                        Instant.parse("2026-11-20T00:00:00Z"),
                        ZoneId.systemDefault()
                )
        );
        assertEquals(100, dateService.calculateAge(dateOfBirth));

        // Fix the current date returned by LocalDate.now() to the **year after** the birthday date: 2027-11-20
        dateService = new DateService(
                Clock.fixed(
                        Instant.parse("2027-11-20T00:00:00Z"),
                        ZoneId.systemDefault()
                )
        );
        assertEquals(101, dateService.calculateAge(dateOfBirth));
    }

    @Test
    public void isChildrenTest() {
        // Fix the current date returned by LocalDate.now() to: 2025-12-01
        DateService dateService = new DateService(
                Clock.fixed(
                        Instant.parse("2025-12-01T00:00:00Z"),
                        ZoneId.systemDefault()
                )
        );
        LocalDate dateOfBirthChildren = LocalDate.of(2007, 12, 1);
        LocalDate dateOfBirthAdult1   = LocalDate.of(2006, 11, 30);
        LocalDate dateOfBirthAdult2   = LocalDate.of(2006, 12, 1);

        assertTrue(dateService.isChildren(dateOfBirthChildren));
        assertFalse(dateService.isChildren(dateOfBirthAdult1));
        assertFalse(dateService.isChildren(dateOfBirthAdult2));
    }
}