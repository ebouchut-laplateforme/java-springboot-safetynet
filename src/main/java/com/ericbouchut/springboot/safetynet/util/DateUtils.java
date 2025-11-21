package com.ericbouchut.springboot.safetynet.util;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Period;
import java.time.LocalDate;

/**
 * This helper class contains utility methods to handle dates.
 */
@Component
public final class DateUtils {

    private final Clock clock;

    public DateUtils(Clock clock) {
        this.clock = clock;
    }

    /**
     * Return the age based on the passed-in date of birth.
     * <p>
     * This method uses {@link #clock} for the unit test
     * to set a fixed current date.
     *
     * @param dateOfBirth the date of birth
     * @return the age
     *
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#clock()
     */
    public int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now(clock)).getYears();
    }
}
