package com.ericbouchut.springboot.safetynet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A person info Data Transfer Object.
 * <br/>
 * This is a <b>response</b> DTO,
 * meaning it is intended to be serialized,
 * but <b>NOT</b> deserialized.
 *
 * @param fullName             concatenation of the first and last name (separated with a space). Serialized as the JSON property <code>name</code>.
 * @param address
 * @param age                  serialized byt not deserialized.
 * @param email
 * @param medicalHistory       a {@link MedicalHistoryDTO}
 */
public record PersonInfoDTO(
        // Serialized as the JSON property name
        @JsonProperty("name")
        String fullName,

        String address,

        /*
         * <code>age</code> is a virtual attribute calculated from the date of birth.
         * It is <b>serialized</b> (written to JSON)
         * It is <b>NOT</b> deserialized (read from JSON).
         * <br/>
         * <code>JsonProperty.Access.READ_ONLY</code> prevents
         * this field from being deserialized.
         */
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        int age,

        String email,

        @JsonProperty("medical_history") // Use custom JSON property name for serialization
        MedicalHistoryDTO medicalHistoryDTO
) { }
