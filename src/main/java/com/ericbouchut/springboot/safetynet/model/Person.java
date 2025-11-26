package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @EqualsAndHashCode.Include
    private String firstName;

    @EqualsAndHashCode.Include
    private String lastName;

    @EqualsAndHashCode.Include
    private String address;

    @EqualsAndHashCode.Include
    private String city;

    @EqualsAndHashCode.Include
    private String zip;

    private String phone;
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
