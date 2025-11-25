package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * An entity that models the medical record of a {@link Person}:
 * ({@link #firstName}, {@link #lastName}, {@link #dateOfBirth},
 * {@link #medications}, {@link #allergies})
 *
 * @see Person
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private String firstName;
    private String lastName;

    /**
     * The date of birth mapped to the <code>birthdate</code> JSON property
     * when deserialized (read from JSON) and serialized (saved to JSON),
     * using the format "MM/dd/yyyy" where:
     * <ul>
     *     <li><code>MM</code> denotes the month number (2 digits)</li>
     *     <li><code>dd</code> denotes the day of the month (2 digits)</li>
     *     <li><code>yyyy</code> denotes the year (4 digits)</li>
     * </ul>
     * followed by the day of the month (2 digits)
     *
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#data(DataLoader)
     */
    @JsonFormat(pattern = "MM/dd/yyyy") // "month(2 digits)/dayOfMonth(2 digits)/year(4 digits)"
    @JsonProperty("birthdate") // Custom JSON field name
    private LocalDate dateOfBirth;

    private Set<String> medications;
    private Set<String> allergies;
}
