package com.ericbouchut.springboot.safetynet.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Objects;

// TODO: Remove? Not needed so far.
@Data
public class MedicationDTO {
    // TODO: Refactor: Move to a dedicated Helper class that contains constants
    private static String MEDICATION_DOSAGE_SEPARATOR = ":";

    private String name;
    private String dosage;

    /**
     * Build a <code>MedicationDTO</code> from a String.
     *
     * @param medicationNameAndDosage A String with the medication and the dosage separated by a colon (<code>:</code>).
     * @return a MedicationDTO or null
     * @throws NullPointerException if <code>medicationNameAndDosage</code> is null
     */
    public static MedicationDTO from(String medicationNameAndDosage) {
        Objects.requireNonNull(medicationNameAndDosage);
        String[] nameAndDosage = StringUtils.split(medicationNameAndDosage, MEDICATION_DOSAGE_SEPARATOR);

        MedicationDTO medicationDTO = new MedicationDTO();
        if (nameAndDosage != null) {
            medicationDTO.name   = nameAndDosage[0];

            if (nameAndDosage.length == 2) {
                medicationDTO.dosage = nameAndDosage[1];
            } else {
                medicationDTO.dosage = "";
            }
        }

        return medicationDTO;
    }
}
