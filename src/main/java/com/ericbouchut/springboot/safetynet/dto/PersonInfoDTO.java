package com.ericbouchut.springboot.safetynet.dto;

import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonInfoDTO {
    /**
     * Full name: concatenation of the first and last name (separated with a space)
     */
    @JsonProperty("name")
    private String fullName;

    private String address;

    /**
     * The age is a virtual attribute calculated from the date of birth.
     * It is <b>serialized</b> (written to JSON)
     * but <b>NOT deserialized</b> (read from JSON).
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int age;

    private String email;

    @JsonProperty("medical_history") // Use this custom field name for JSON (de)serialization
    private MedicalHistoryDTO medicalHistory;

    public PersonInfoDTO(Person person, List<MedicalRecord> medicalRecords) {
        fullName = person.getFirstName() + " " + person.getLastName();
        address  = person.getAddress();
        email    = person.getEmail();

        medicalRecords.stream()
                // Keep the first medical record and discard the rest
                .findFirst()
                .ifPresentOrElse(
                m -> {
                        // There is at least one medical record
                        age = DateUtils.calculateAge(m.getDateOfBirth());
                        medicalHistory = new MedicalHistoryDTO(m);
                    },
                    () -> {
                        // No associated medical record
                        age = -1;
                        medicalHistory = new MedicalHistoryDTO();
                    }
                );
    }
}
