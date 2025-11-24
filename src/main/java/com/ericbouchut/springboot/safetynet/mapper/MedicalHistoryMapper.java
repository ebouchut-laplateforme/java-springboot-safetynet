package com.ericbouchut.springboot.safetynet.mapper;

import com.ericbouchut.springboot.safetynet.dto.MedicalHistoryDTO;
import com.ericbouchut.springboot.safetynet.dto.MedicationDTO;
import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Mapper for converting a <code>MedicalRecord</code> entity to a <code>MedicalHistoryDTO</code>.
 *
 * @see MedicalRecord
 * @see MedicalHistoryDTO
 */
@Component
public class MedicalHistoryMapper {
    private final MedicationMapper medicationMapper;

    public MedicalHistoryMapper(MedicationMapper medicationMapper) {
        this.medicationMapper = medicationMapper;
    }

    @Nonnull
    public MedicalHistoryDTO toDTO(@Nullable MedicalRecord medicalRecord) {

        if(medicalRecord == null) {
            return new MedicalHistoryDTO(Collections.emptyList(), Collections.emptySet());
        }

        List<MedicationDTO> medications = medicalRecord.getMedications()
                .stream()
                // Remove null or empty medications
                .filter(medication -> !ObjectUtils.isEmpty(medication))
                // .map(medication -> medicationMapper.toDTO(medication))
                .map(medicationMapper::toDTO)
                .toList();

        Set<String> allergies = medicalRecord.getAllergies();

        return new MedicalHistoryDTO(medications, allergies);
    }
}
