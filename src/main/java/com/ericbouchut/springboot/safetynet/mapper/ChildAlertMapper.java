package com.ericbouchut.springboot.safetynet.mapper;

import com.ericbouchut.springboot.safetynet.dto.ChildAlertDTO;
import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import com.ericbouchut.springboot.safetynet.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChildAlertMapper {
    public ChildAlertDTO toDTO(MedicalRecord childMedicalRecord, List<Person> otherHouseholdMembers) {

        return new ChildAlertDTO(
                childMedicalRecord.getFirstName(),
                childMedicalRecord.getLastName(),
                childMedicalRecord.age(),
                otherHouseholdMembers
        );
    }
}
