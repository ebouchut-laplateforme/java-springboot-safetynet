package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.dto.FireStationDTO;
import com.ericbouchut.springboot.safetynet.model.FireStation;
import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.repository.FireStationRepository;
import com.ericbouchut.springboot.safetynet.repository.MedicalRecordRepository;
import com.ericbouchut.springboot.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FireStationService {
    private final FireStationRepository fireStationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~
    //  CRUD Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return all fire stations (no duplicates)
     */
    public Set<FireStation> getAllFireStations() {
        return fireStationRepository.getAllFireStations();
    }

    public List<FireStation> getFireStationsByNumber(Integer fireStationNumber) {
        return fireStationRepository.getFireStationsByNumber(fireStationNumber);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public FireStationDTO getFireStationDTOByNumber(Integer fireStationNumber) {
        // Naming cargo cult on steroids ;-)!
        Set<String> fireStationAddresses =
                fireStationRepository.getFireStationAddressesByNumber(fireStationNumber);
        Set<Person> personsServedByFireStation =
                personRepository.getPersonsByAddresses(fireStationAddresses);

        Map<Person, List<MedicalRecord>> medicalRecordsByPersonServedByFireStation =
                medicalRecordRepository.getMedicalRecordsByPersons(personsServedByFireStation);

        Map<Boolean, Long> countByIsChild = medicalRecordsByPersonServedByFireStation
            .values()
            .stream()
            .flatMap(List::stream)
            .distinct()  // TODO: Improve the design here, building a List + distinct is clunky!
            .collect(
                // Generate a Map with 2 entries (lists are guaranteed to exist):
                // Key = true  => Value = count of children's medical records
                // Key = false => Value = count of adults     medical records
                Collectors.partitioningBy(
                        MedicalRecord::isChildren, // true (children), false (adult)
                        Collectors.counting()      // count of the corresponding medical records
                )
            );

        // JC> Gather the ingredients ... to build a FireStationDTO :-)
        Long childrenCount = countByIsChild.get(true);
        Long adultsCount   = countByIsChild.get(false);
        List<FireStationDTO.PersonDTO> personsDTO = personsServedByFireStation.stream()
                .map(p ->
                        new FireStationDTO.PersonDTO(
                                p.getFirstName(),
                                p.getLastName(),
                                p.getAddress(),
                                p.getPhone()
                        )
                )
                .toList();

        return new FireStationDTO(personsDTO, adultsCount, childrenCount);
    }
}
