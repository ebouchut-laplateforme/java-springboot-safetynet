package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import com.ericbouchut.springboot.safetynet.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Set;

/**
 * An entity that models the medical record of a {@link Person}:
 * <ul>
 *     <li>({@link #firstName},</li>
 *     <li>{@link #lastName},</li>
 *     <li>{@link #dateOfBirth},</li>
 *     <li>{@link #medications} (optional),</li>
 *     <li>{@link #allergies} (optional)</li>
 * </ul>
 *
 * @see Person
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class MedicalRecord {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    /**
     * The date of birth mapped to the <code>birthdate</code> JSON property
     * when deserialized (read from JSON) (and serialized (saved to JSON),
     * using the format "MM/dd/yyyy" where:
     * <ul>
     *     <li><code>MM</code> denotes the month number (2 digits)</li>
     *     <li><code>dd</code> denotes the day of the month (2 digits)</li>
     *     <li><code>yyyy</code> denotes the year (4 digits)</li>
     * </ul>
     * Example: <code>"12/31/2025"</code>
     *
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#data(DataLoader)
     */
    // TODO: REST API v2 should take into account the timezone and use ZonedDate instead of LocalDate
    @JsonProperty("birthdate") // Custom JSON field name
    @JsonFormat(pattern = "MM/dd/yyyy") // "month(2 digits)/dayOfMonth(2 digits)/year(4 digits)"
    @NotBlank
    @Past  // must be a past date
    private LocalDate dateOfBirth;

    private Set<@NotEmpty String> medications;
    private Set<@NotEmpty String> allergies;

    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // Helper instance methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return the age calculated live from the {@link #dateOfBirth}.
     */
    public int age() {
        return DateUtils.calculateAge(getDateOfBirth());
    }

    /**
     * @return a boolean, <code>true</code> if the person is 18 years old or less
     */
    public boolean isChildren() {
        return age() <= 18;
    }
}
