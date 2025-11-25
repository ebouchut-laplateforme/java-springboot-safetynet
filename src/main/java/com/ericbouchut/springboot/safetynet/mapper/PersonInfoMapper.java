package com.ericbouchut.springboot.safetynet.mapper;

import com.ericbouchut.springboot.safetynet.dto.MedicalHistoryDTO;
import com.ericbouchut.springboot.safetynet.dto.PersonInfoDTO;
import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.util.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * Mapper for converting a ({@link Person} and a {@link List} of {@link MedicalRecord})
 * to a {@link PersonInfoDTO}.
 */
@Component
public class PersonInfoMapper {
    private final MedicalHistoryMapper medicalHistoryMapper;

    public PersonInfoMapper(MedicalHistoryMapper medicalHistoryMapper) {
        this.medicalHistoryMapper = medicalHistoryMapper;
    }

    public PersonInfoDTO toDTO(Person person, List<MedicalRecord> medicalRecords) {
        String fullName = person.getFirstName() + " " + person.getLastName();
        String address = person.getAddress();
        String email = person.getEmail();

        Optional<MedicalRecord> maybeFirstMedicalRecord = medicalRecords.stream()
                .filter(m -> !ObjectUtils.isEmpty(m))
                // Keep the first one and discard the rest
                .findFirst();

        int age = maybeFirstMedicalRecord
                .map(m -> DateUtils.calculateAge(m.getDateOfBirth()))
                .orElse(-1);

        MedicalHistoryDTO medicalHistoryDTO = maybeFirstMedicalRecord
                .map(medicalHistoryMapper::toDTO)
                .orElseGet(() -> medicalHistoryMapper.toDTO(null));

        return new PersonInfoDTO(fullName, address, age, email, medicalHistoryDTO);
    }
}
