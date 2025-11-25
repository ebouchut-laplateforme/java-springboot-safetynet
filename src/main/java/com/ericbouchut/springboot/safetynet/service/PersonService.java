package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.dto.PersonInfoDTO;
import com.ericbouchut.springboot.safetynet.mapper.PersonInfoMapper;
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
public class PersonService {
    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PersonInfoMapper personInfoMapper;

    public PersonService(
            PersonRepository personRepository,
            FireStationRepository fireStationRepository,
            MedicalRecordRepository medicalRecordRepository,
            PersonInfoMapper personInfoMapper
    ) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.personInfoMapper = personInfoMapper;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  CRUD methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Set<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }

    public void createPerson(Person person) {
        personRepository.createPerson(person);
    }

    public boolean deletePerson(Person person) {
        return personRepository.deletePerson(person);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    public List<String> getCityEmails(String city) {
        return personRepository.getEmailsByCity(city);
    }

    /**
     * Return a unmodifiable <code>List</code> (not a <code>Set</code>) of phone numbers,
     * because several persons can share the same phone number.
     * We can have duplicate phone numbers.
     * <br/>
     * Important: Several fire stations can share the same station number.
     *
     * @param fireStationNumber the number of a fire station
     * @return a list of inhabitant phone numbers that are served by fire stations sharing the same number.
     */
    public List<String> getPhoneNumbersByFireStation(Integer fireStationNumber) {
        Set<String> fireStationAddresses = fireStationRepository.getFireStationsByNumber(fireStationNumber)
                .stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        return personRepository.getAllPersons()
                .stream()
                .filter(p -> fireStationAddresses.contains(p.getAddress()))
                .map(Person::getPhone)
                // The Stream now only contains phone numbers
                // i.e., each person has benn replaced with their phone numbers
                .distinct() // Remove duplicate phone numbers
                .toList();
    }

    /**
     * Search for persons with a given first and last names,
     * then for each person found,
     * return a list of {@link PersonInfoDTO}:
     * a mix of their personal information
     * and medical history (medications + allergies).
     *
     * This function returns a list because  several persons can
     * have the same (first and last) name (namesake).
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return a list with the PersonInfoDTO of each person found
     */
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        Set<Person> persons = personRepository.getPersonsByFirstNameAndLastName(firstName, lastName);

        Map<Person, List<MedicalRecord>> medicalRecords = medicalRecordRepository.getMedicalRecordsByPersons(persons);
        return medicalRecords
                .entrySet()
                .stream()
                .map( entry ->
                        // Key: person, Value: List<MedicalRecord>
                        personInfoMapper.toDTO(entry.getKey(), entry.getValue())
                ).toList();
    }
}
