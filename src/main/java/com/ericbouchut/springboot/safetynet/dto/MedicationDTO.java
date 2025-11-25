package com.ericbouchut.springboot.safetynet.dto;

/**
 * A medication Data Transfer Object.
 * <br/>
 * This is a <b>response</b> DTO,
 * meaning it is not intended to be deserialized.
 *
 * @param name
 * @param dosage
 */
public record MedicationDTO (
        String name,
        String dosage
) {}
