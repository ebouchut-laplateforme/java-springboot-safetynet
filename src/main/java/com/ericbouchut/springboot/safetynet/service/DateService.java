package com.ericbouchut.springboot.safetynet.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * This service contains utility methods to calculate dates.
 */
@Component
public final class DateService {
    /**
     * The Clock is injected to provide a way for tests to use a <b>fixed</b> date.
     *
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#clock()
     */
    private final Clock clock;

    public DateService(Clock clock) {
        this.clock = clock;
    }

    /**
     * This method calculates the number of (full) years between start and end
     * It truncates partial years.
     *
     * @param start the start of the period
     * @param end the end of the period
     *
     * @return the number of years between <code>start</code> and <code>end</code>
     */
    public int yearsBetween(Temporal start, Temporal end) {
        return (int) ChronoUnit.YEARS.between(start, end);
    }

    /**
     * @param dateOfBirth date of birth
     * @return the age
     */
    public int calculateAge(Temporal dateOfBirth) {
        return yearsBetween(dateOfBirth, LocalDate.now(clock));
    }

    /**
     * @param dateOfBirth date of birth
     * @return a boolean, <code>true</code> if the age calculated from the date of birth is 18 or less
     */
    public boolean isChildren(Temporal dateOfBirth) {
        return calculateAge(dateOfBirth) <= 18;
    }
}
