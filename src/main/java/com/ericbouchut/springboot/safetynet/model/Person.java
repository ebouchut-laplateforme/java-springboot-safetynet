package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The detailed information about a person that SafetyNet can send alerts to.
 * Instances of this class deserialized (read and instantiated) from a JSON file.
 * No JPA, nor database storage involved.
 * <p>
 * The "primary key" of a Person is their full name,
 * that is the combination of their <code>fistName</code> and <code>lastName</code>.
 * The <b>unicity</b> of a <code>Person</code> is solely based on these 2 fields.
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

    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}
