package com.ericbouchut.springboot.safetynet.model;

import com.ericbouchut.springboot.safetynet.data.DataLoader;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The only instance of this class holds all the application data.
 * It is deserialized from a JSON file at application startup
 * and serialized (written to the JSON file) when the application shuts down.
 *
 * @see com.ericbouchut.springboot.safetynet.config.JavaSpringBootSafetynetConfiguration#data(DataLoader)
 *
 * @see FireStation
 * @see Person
 * @see MedicalRecord
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    List<Person> persons;

    @JsonProperty("firestations") // Custom JSON field name
    List<FireStation> fireStations;

    @JsonProperty("medicalrecords") // Custom JSON field name
    List<MedicalRecord> medicalRecords;
}
