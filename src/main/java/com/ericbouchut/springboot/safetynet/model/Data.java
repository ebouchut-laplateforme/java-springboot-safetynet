package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;

/**
 * The only instance of this class holds all the application data.
 * It is deserialized from a JSON file at application startup
 * and serialized (written to the JSON file) when the application shuts down.
 *
 * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#data(DataLoader)
 *
 * @see FireStation
 * @see Person
 * @see MedicalRecord
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Data {
    @NotEmpty
    Set<@Valid Person> persons;

    @JsonProperty("firestations") // Custom JSON field name
    @NotEmpty
    Set<@Valid FireStation> fireStations;

    @JsonProperty("medicalrecords") // Custom JSON field name
    @NotEmpty
    Set<@Valid MedicalRecord> medicalRecords;

    /**
     * Search a Person by its full name (firstName + lastName).
     * The unicity of a Personis solely based on these 2 fields
     *
     * @param firstName
     * @param lastName
     * @return maybe a Person
     *
     * @see Person#hashCode()
     */
    public Optional<Person> getPersonByFirstNameAndLastName(String firstName, String lastName) {
        Person searchedPerson = new Person();
        searchedPerson.setFirstName(firstName);
        searchedPerson.setLastName(lastName);

        return persons.stream()
                .filter(p -> p.equals(searchedPerson))
                .findFirst();
    }
}
