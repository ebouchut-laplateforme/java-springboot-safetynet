package com.ericbouchut.springboot.safetynet.dto;

import java.util.List;
import java.util.Set;

/**
 * Medical history (of a person) Data Transfer Object.
 * <br/>
 * This is a <b>response</b> DTO,
 * meaning it is not intended to be deserialized.
 *
 * @see com.ericbouchut.springboot.safetynet.mapper.MedicalHistoryMapper
 * @see MedicationDTO
 */
public record MedicalHistoryDTO (
    List<MedicationDTO> medications,
    Set<String> allergies
) {}