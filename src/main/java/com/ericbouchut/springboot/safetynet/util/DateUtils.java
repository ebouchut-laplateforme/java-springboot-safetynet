package com.ericbouchut.springboot.safetynet.util;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Period;
import java.time.LocalDate;

/**
 * This helper class contains utility methods to handle dates.
 */
public final class DateUtils {

    /**
     * Constructor made private to prevent instantiation
     */
    private DateUtils() {
        // Prevent instantiation
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Static Helper Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Return the age based on the passed-in date of birth.
     * <p>
     * This method is <b>designed</b> exclusively for <b>(unit) tests</b> to set a <b>fixed</b> current date.
     * Pass a fixed {@link Clock} to ensure the current date does not change:
     * <code><pre>
     * // Fix the current date returned by LocalDate.now() to 2025-11-01
     * Clock fixedClock = Clock.fixed(
     *      Instant.parse("2025-11-01T00:00:00Z"),
     *      ZoneId.systemDefault()
     * );
     * LocalDate dateOfBirth = LocalDate.of(1900, 12, 01);
     *
     * assertEquals(124, DateUtils.calculateAge(dateOfBirth, fixedClock));
     * </pre></code>
     * To calculate the age outside of a test, use the {@link calculateAge(LocalDate)} single-argument method.
     *
     * @param dateOfBirth the date of birth
     * @param clock the clock to use (fixed to a given date for testing purposes)
     * @return the age
     *
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#clock()
     */
    public static int calculateAge(LocalDate dateOfBirth, Clock clock) {
        return Period.between(dateOfBirth, LocalDate.now(clock)).getYears();
    }
}
