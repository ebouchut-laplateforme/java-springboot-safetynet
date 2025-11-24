package com.ericbouchut.springboot.safetynet.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Data
public class MedicationDTO {
    // TODO: Refactor: Move to a dedicated final Constants class
    private static String MEDICATION_DOSAGE_SEPARATOR = ":";

    private String name;
    private String dosage;

    /**
     * Build from a String with the format <code>"medicationName:MedicationDosage"</code>,
     * such as <code>"tetracyclaz:650mg"</code>.
     *
     * @param nameAndDosage A String with the medication and the dosage separated by a colon (<code>:</code>).
     * @throws NullPointerException if <code>nameAndDosage</code> is null
     */
    public MedicationDTO(String nameAndDosage) {
        Objects.requireNonNull(nameAndDosage);

        // TODO: Extract to method: the code to split
        String[] nameAndDosageParts = StringUtils.split(nameAndDosage, MEDICATION_DOSAGE_SEPARATOR);

        if (nameAndDosageParts != null) {
            name = nameAndDosageParts[0];

            if (nameAndDosageParts.length == 2) {
                dosage = nameAndDosageParts[1];
            } else {
                dosage = "";
            }
        }
    }
}
