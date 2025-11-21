package com.ericbouchut.springboot.safetynet.dto;

import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int age;
    private String email;

    @JsonProperty("medical_history")
    private MedicalHistoryDTO medicalHistory;

    private List<String> allergies;

    private final DateUtils dateUtils;

    public PersonInfoDTO(Person person, MedicalRecord medicalRecord, DateUtils dateUtils) {
        this.dateUtils = dateUtils;

        this.fullName = person.getFirstName() + " " + person.getLastName();
        this.address  = person.getAddress();
        this.age = dateUtils.calculateAge(medicalRecord.getDateOfBirth());
        this.email = person.getEmail();
    }
}
