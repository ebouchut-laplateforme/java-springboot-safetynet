package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.Data;

import java.util.Objects;

/**
 * The detailed information about a person that SafetyNet can send alerts to.
 * Instances of this class deserialized (read and instantiated) from a JSON file.
 * No JPA, nor database storage involved.
 * <p>
 * The "primary key" of a Person is composed of the following fields:
 * <ul>
 *   </li><code>fistName</code></li>
 *   <li><code>lastName</code></li>
 *   <li><code>address</code></li>
 *   <li><code>city</code></li>
 *   <li><code>zip</code></li>
 * </ul>
 * The <b>unicity</b> of a <code>Person</code>
 * is solely based on the above fields.
 *
 * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#data(DataLoader)
 * @see com.ericbouchut.springboot.safetynet.model.Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Why both @Builder and constructors? To learn...
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @EqualsAndHashCode.Include
    @NotBlank
    private String firstName;

    @EqualsAndHashCode.Include
    @NotBlank
    private String lastName;

    @EqualsAndHashCode.Include
    @NotBlank
    private String address;

    @EqualsAndHashCode.Include
    @NotBlank
    private String city;

    @EqualsAndHashCode.Include
    @NotBlank
    private String zip;

    private String phone;

    @Email
    @NotBlank
    private String email;

    /**
     * Test if a person has a given full name (first and last name)
     * @param firstName
     * @param lastName
     * @return true if this Person has the passed in full name (same firstName
     */
    public boolean hasFullName(String firstName, String lastName) {
        return Objects.equals(this.firstName, firstName)
                && Objects.equals(this.lastName, lastName);
    }
}
