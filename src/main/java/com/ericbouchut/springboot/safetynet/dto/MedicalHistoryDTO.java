package com.ericbouchut.springboot.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Medical history (medications, dosage, allergies) of a resident (<code>Person</code>).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryDTO {
    List<String> medications;
    List<String> allergies;
}
