package com.ericbouchut.springboot.safetynet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @JsonProperty("birthdate") // Custom JSON field name
    private LocalDate birthDate;

    private Set<String> medications;
    private Set<String> allergies;
}
