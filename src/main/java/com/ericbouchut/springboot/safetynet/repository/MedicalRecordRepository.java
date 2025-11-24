package com.ericbouchut.springboot.safetynet.repository;

import com.ericbouchut.springboot.safetynet.model.Data;
import com.ericbouchut.springboot.safetynet.model.MedicalRecord;
import com.ericbouchut.springboot.safetynet.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MedicalRecordRepository {
    private final Data data;

    public MedicalRecordRepository(Data data) {
        this.data = data;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * IMPORTANT: We can have homonyms with a different address,
     * that is several persons with the same first and last names
     * but with a different address (that is where the tuple (address, zip code, city) is different.
     *
     * @param person the person we are searching the medical records of
     * @return the medical records of personS with the same first and last name than the passed-in person
     */
    public List<MedicalRecord> getMedicalRecordsByPerson(Person person) {
        return data.getMedicalRecords()
                .stream()
                .filter(m -> m.getLastName().equals(person.getLastName())
                        && m.getFirstName().equals(person.getFirstName()))
                .toList();
    }

    /**
     * @param persons list of people whose medical records we want to retrieve and associate.
     * @return a Map with (key, value) pairs, where the key is a <code>Person</code> and the value is this person's list of medical records.
     */
    public Map<Person, List<MedicalRecord>> getMedicalRecordsByPersons(Set<Person> persons) {
        return persons.stream()
                .collect(
                    Collectors.toMap(
                        // Function.identity() <=> person -> person
                        Function.identity(),            // Key:   person
                        this::getMedicalRecordsByPerson // Value: List<MedicalRecord>
                    )
                );
    }
}
