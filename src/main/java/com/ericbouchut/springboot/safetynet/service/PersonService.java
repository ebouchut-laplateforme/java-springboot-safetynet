package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.dto.ChildAlertDTO;
import com.ericbouchut.springboot.safetynet.dto.FloodDTO;
import com.ericbouchut.springboot.safetynet.dto.PersonInfoDTO;
import com.ericbouchut.springboot.safetynet.mapper.ChildAlertMapper;
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
    private final DateService dateService;

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    private final PersonInfoMapper personInfoMapper;
    private final ChildAlertMapper childAlertMapper;

    public PersonService(
            DateService dateService,

            PersonRepository personRepository,
            FireStationRepository fireStationRepository,
            MedicalRecordRepository medicalRecordRepository,

            PersonInfoMapper personInfoMapper,
            ChildAlertMapper childAlertMapper
    ) {
        this.dateService = dateService;

        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordRepository = medicalRecordRepository;

        this.personInfoMapper = personInfoMapper;
        this.childAlertMapper = childAlertMapper;
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
     * Return an unmodifiable <code>List</code> (not a <code>Set</code>) of phone numbers,
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
     * and medical history (medications and allergies).
     * <br/>
     * This function returns a list because several persons can
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

    /**
     * Return a JSON with a list of children (age <= 18) living at this address.
     * Each list entry contains the children's first name, last name, age and a list of other household members.
     *
     * @param address used to search for children living at this address
     * @return a <code>List</code> of {@link ChildAlertDTO} or an empty List if there are no children at this address
     */
    public List<ChildAlertDTO> getChildAlerts(String address) {
        // People living at this address
        Set<Person> householdMembers = personRepository.getPersonsByAddress(address);

        Map<Person, List<MedicalRecord>> medicalRecordsByHouseholdMember = medicalRecordRepository.getMedicalRecordsByPersons(householdMembers);

        List<MedicalRecord> householdMedicalRecords = medicalRecordsByHouseholdMember
                .values()
                .stream()
                // Convert Stream<List<MedicalRecord>> into Stream<MedicalRecord>
                .flatMap(List::stream)
                .toList();

        List<ChildAlertDTO> childAlertsDTO = householdMedicalRecords.stream()
                // Keep only children medical records in the Stream (remove adults' medical records)
                .filter(m -> dateService.isChildren(m.getDateOfBirth()))
                .map( childMedicalRecord -> {
                        // Build the household members excluding this child
                        List<Person> otherHouseHoldMembers = householdMembers.stream()
                                .filter(p -> !p.hasFullName(childMedicalRecord.getFirstName(), childMedicalRecord.getLastName()))
                                .toList();

                        return childAlertMapper.toDTO(childMedicalRecord, otherHouseHoldMembers);
                })
                .toList();

        return childAlertsDTO;
    }

    public List<FloodDTO> getFloodDTO(List<Integer> fireStationNumbers) {
        // TODO: Do it
        return null;
    }
}