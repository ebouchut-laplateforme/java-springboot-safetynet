package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.mapper.ChildAlertMapper;
import com.ericbouchut.springboot.safetynet.mapper.PersonInfoMapper;
import com.ericbouchut.springboot.safetynet.repository.FireStationRepository;
import com.ericbouchut.springboot.safetynet.repository.MedicalRecordRepository;
import com.ericbouchut.springboot.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private FireStationRepository fireStationRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private PersonInfoMapper personInfoMapper;
    @Mock
    private ChildAlertMapper childAlertMapper;
    @Mock
    private DateService dateService;

    @InjectMocks
    private PersonService personService;

    @Test
    void getAllPersons() {
        // TODO: Do it
    }

    @Test
    void createPerson() {
        // TODO: Do it
    }

    @Test
    void deletePerson() {
        // TODO: Do it
    }

    @Test
    void getCityEmails() {
        // TODO: Do it
    }

    @Test
    void getPhoneNumbersByFireStation() {
        // TODO: Do it
    }

    @Test
    void getPersonInfo() {
        // TODO: Do it
    }

    @Test
    void getChildAlerts() {
        // TODO: Do it
    }

    @Test
    void getFloodDTO() {
        // TODO: Do it
    }
}