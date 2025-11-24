package com.ericbouchut.springboot.safetynet.dto;

import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Medical history (medications, dosage, allergies) of a resident (<code>Person</code>).
 */
@Data
public class MedicalHistoryDTO {
    List<MedicationDTO> medications;
    Set<String> allergies;

    public MedicalHistoryDTO() {
        medications = Collections.emptyList();
        allergies = Collections.emptySet();
    }

    public MedicalHistoryDTO(MedicalRecord medicalRecord) {
        medications = medicalRecord.getMedications()
                .stream()
                .filter(medication -> !ObjectUtils.isEmpty(medication))
                //.map(m -> new MedicationDTO(m))
                .map(MedicationDTO::new)
                .toList();

        allergies = medicalRecord.getAllergies();
    }
}
