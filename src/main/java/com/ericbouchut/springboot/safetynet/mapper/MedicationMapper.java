package com.ericbouchut.springboot.safetynet.mapper;

import com.ericbouchut.springboot.safetynet.dto.MedicationDTO;
import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting a <code>MedicalRecord</code> entity to a <code>MedicationDTO</code>.
 *
 * @see MedicalRecord
 * @see MedicationDTO
 */
@Component
public class MedicationMapper {
    /**
     * The character that separates the medication name and dosage.
     */
    private static final String MEDICATION_DOSAGE_SEPARATOR = ":";

    /**
     * Build a <code>MedicationDTO</code> from a String with the format
     * <code>"medicationName:MedicationDosage"</code>,
     * such as <code>"tetracyclaz:650mg"</code>.
     * The colon separator (<code>:</code>) is optional.
     * The dosage (second part after the separator) is optional.
     *
     * @param medicationNameAndDosage A String with the medication name and the dosage separated by a colon (<code>:</code>).
     * @return a MedicationDTO or a NullObject  MedicationDTO object if the argument is null
     */
    public MedicationDTO toDTO(String medicationNameAndDosage) {
        String medicationName   = null;
        String medicationDosage = null;

        if (medicationNameAndDosage != null) {
            int separatorIndex = medicationNameAndDosage.indexOf(MEDICATION_DOSAGE_SEPARATOR);

            if (separatorIndex == -1) {
                // Medication name only (no dosage) "doliprane"
                medicationName = medicationNameAndDosage;
            } else {
                // Medication name and dosage: "doliprane:1000mg"
                medicationName   = medicationNameAndDosage.substring(0, separatorIndex).trim();
                medicationDosage = medicationNameAndDosage.substring(separatorIndex+1).trim();
            }
        }
        return new MedicationDTO(medicationName, medicationDosage);
    }
}
