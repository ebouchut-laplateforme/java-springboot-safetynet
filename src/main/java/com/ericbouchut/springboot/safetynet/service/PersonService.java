package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.model.FireStation;
import com.ericbouchut.springboot.safetynet.model.Person;
import com.ericbouchut.springboot.safetynet.repository.FireStationRepository;
import com.ericbouchut.springboot.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final Clock clock;

    public PersonService(
            PersonRepository personRepository,
            FireStationRepository fireStationRepository,
            Clock clock
    ) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.clock = clock;
    }


    public Set<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }

    public void createPerson(Person person) {
        personRepository.createPerson(person);
    }

    public boolean deletePerson(Person person) {
        return personRepository.deletePerson(person);
    }

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
                .distinct()
                .toList();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Helper Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Return the age of a person based on their date of birth.
     * <p>
     * This method uses {@link #clock} to allow {@link PersonServiceTest}
     *
     * @param birthDate the date of birth
     * @return the age
     *
     * @see com.ericbouchut.springboot.safetynet.config.SafetynetConfiguration#clock()
     */
    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now(clock)).getYears();
    }
}
