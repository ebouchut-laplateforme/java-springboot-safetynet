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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonService}.
 *
 * This test class comprehensively tests all PersonService methods including:
 * - CRUD operations (getAllPersons, createPerson, deletePerson)
 * - Custom finder methods (getCityEmails, getPhoneNumbersByFireStation)
 * - Complex DTO transformations (getPersonInfo, getChildAlerts)
 *
 * Testing strategies:
 * - Mock all dependencies (repositories, services, mappers)
 * - Test happy paths and edge cases
 * - Verify complex data transformations and filtering logic
 * - Ensure proper interaction with dependencies
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private DateService dateService;

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

    @InjectMocks
    private PersonService personService;

    private Person adult1;
    private Person adult2;
    private Person child1;
    private Person child2;
    private FireStation fireStation1;
    private FireStation fireStation2;
    private MedicalRecord adultMedicalRecord1;
    private MedicalRecord adultMedicalRecord2;
    private MedicalRecord childMedicalRecord1;
    private MedicalRecord childMedicalRecord2;

    /**
     * Setup test data before each test.
     * Creates realistic scenarios with:
     * - Multiple persons (adults and children)
     * - Fire stations serving different addresses
     * - Medical records with different birthdates
     */
    @BeforeEach
    void setUp() {
        // Adults
        adult1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .city("Springfield")
                .zip("12345")
                .phone("555-1234")
                .email("john.doe@example.com")
                .build();

        adult2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .address("456 Oak Ave")
                .city("Springfield")
                .zip("12345")
                .phone("555-5678")
                .email("jane.smith@example.com")
                .build();

        // Children
        child1 = Person.builder()
                .firstName("Tommy")
                .lastName("Doe")
                .address("123 Main St")
                .city("Springfield")
                .zip("12345")
                .phone("555-1234")
                .email("tommy.doe@example.com")
                .build();

        child2 = Person.builder()
                .firstName("Lucy")
                .lastName("Doe")
                .address("123 Main St")
                .city("Springfield")
                .zip("12345")
                .phone("555-1234")
                .email("lucy.doe@example.com")
                .build();

        // Fire stations
        fireStation1 = new FireStation("123 Main St", 1);
        fireStation2 = new FireStation("456 Oak Ave", 1);

        // Medical records
        adultMedicalRecord1 = new MedicalRecord(
                "John",
                "Doe",
                LocalDate.of(1980, 1, 1),
                Set.of("aspirin:100mg"),
                Set.of()
        );

        adultMedicalRecord2 = new MedicalRecord(
                "Jane",
                "Smith",
                LocalDate.of(1975, 5, 20),
                Set.of(),
                Set.of("shellfish")
        );

        childMedicalRecord1 = new MedicalRecord(
                "Tommy",
                "Doe",
                LocalDate.of(2015, 6, 15),
                Set.of("aznol:350mg"),
                Set.of("peanut")
        );

        childMedicalRecord2 = new MedicalRecord(
                "Lucy",
                "Doe",
                LocalDate.of(2017, 3, 10),
                Set.of(),
                Set.of()
        );
    }

    // ========================================
    // CRUD Methods Tests
    // ========================================

    /**
     * Test: getAllPersons should return all persons without duplicates.
     *
     * Design choice: Returns Set to prevent duplicates.
     */
    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        // Arrange
        Set<Person> expectedPersons = Set.of(adult1, adult2, child1);
        when(personRepository.getAllPersons()).thenReturn(expectedPersons);

        // Act
        Set<Person> result = personService.getAllPersons();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(adult1));
        assertTrue(result.contains(adult2));
        assertTrue(result.contains(child1));
        verify(personRepository, times(1)).getAllPersons();
    }

    /**
     * Test: getAllPersons should return empty set when no persons exist.
     *
     * Edge case: Empty data scenario.
     */
    @Test
    void getAllPersons_ShouldReturnEmptySet_WhenNoPersons() {
        // Arrange
        when(personRepository.getAllPersons()).thenReturn(Set.of());

        // Act
        Set<Person> result = personService.getAllPersons();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(personRepository, times(1)).getAllPersons();
    }

    /**
     * Test: createPerson should delegate to repository.
     *
     * Implementation: Service acts as pass-through to repository.
     */
    @Test
    void createPerson_ShouldDelegateToRepository() {
        // Arrange
        doNothing().when(personRepository).createPerson(adult1);

        // Act
        personService.createPerson(adult1);

        // Assert
        verify(personRepository, times(1)).createPerson(adult1);
    }

    /**
     * Test: deletePerson should return true when person exists and is deleted.
     */
    @Test
    void deletePerson_ShouldReturnTrue_WhenPersonDeleted() {
        // Arrange
        when(personRepository.deletePerson(adult1)).thenReturn(true);

        // Act
        boolean result = personService.deletePerson(adult1);

        // Assert
        assertTrue(result);
        verify(personRepository, times(1)).deletePerson(adult1);
    }

    /**
     * Test: deletePerson should return false when person doesn't exist.
     *
     * Edge case: Attempting to delete non-existent person.
     */
    @Test
    void deletePerson_ShouldReturnFalse_WhenPersonNotFound() {
        // Arrange
        when(personRepository.deletePerson(adult1)).thenReturn(false);

        // Act
        boolean result = personService.deletePerson(adult1);

        // Assert
        assertFalse(result);
        verify(personRepository, times(1)).deletePerson(adult1);
    }

    // ========================================
    // Custom Finder Methods Tests
    // ========================================

    /**
     * Test: getCityEmails should return all unique emails for a city.
     *
     * Use case: Community email notifications.
     */
    @Test
    void getCityEmails_ShouldReturnEmailsForCity() {
        // Arrange
        List<String> expectedEmails = List.of("john.doe@example.com", "jane.smith@example.com");
        when(personRepository.getEmailsByCity("Springfield")).thenReturn(expectedEmails);

        // Act
        List<String> result = personService.getCityEmails("Springfield");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("john.doe@example.com"));
        assertTrue(result.contains("jane.smith@example.com"));
        verify(personRepository, times(1)).getEmailsByCity("Springfield");
    }

    /**
     * Test: getCityEmails should return empty list for city with no residents.
     *
     * Edge case: Non-existent or empty city.
     */
    @Test
    void getCityEmails_ShouldReturnEmptyList_WhenCityHasNoResidents() {
        // Arrange
        when(personRepository.getEmailsByCity("EmptyCity")).thenReturn(List.of());

        // Act
        List<String> result = personService.getCityEmails("EmptyCity");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(personRepository, times(1)).getEmailsByCity("EmptyCity");
    }

    /**
     * Test: getPhoneNumbersByFireStation should return distinct phone numbers.
     *
     * Important implementation details:
     * - Retrieves fire station addresses by number
     * - Filters persons living at those addresses
     * - Extracts phone numbers and removes duplicates
     * - Returns as List (not Set) because duplicates are meaningful in raw data
     */
    @Test
    void getPhoneNumbersByFireStation_ShouldReturnDistinctPhoneNumbers() {
        // Arrange
        List<FireStation> fireStations = List.of(fireStation1, fireStation2);
        Set<Person> allPersons = Set.of(adult1, adult2, child1);

        when(fireStationRepository.getFireStationsByNumber(1)).thenReturn(fireStations);
        when(personRepository.getAllPersons()).thenReturn(allPersons);

        // Act
        List<String> result = personService.getPhoneNumbersByFireStation(1);

        // Assert
        assertNotNull(result);
        // adult1 and child1 share the same phone number (555-1234), so it should appear once
        assertEquals(2, result.size(), "Should return 2 distinct phone numbers");
        assertTrue(result.contains("555-1234"));
        assertTrue(result.contains("555-5678"));

        verify(fireStationRepository, times(1)).getFireStationsByNumber(1);
        verify(personRepository, times(1)).getAllPersons();
    }

    /**
     * Test: getPhoneNumbersByFireStation should handle multiple persons with same phone.
     *
     * Real-world scenario: Family members sharing a landline.
     */
    @Test
    void getPhoneNumbersByFireStation_ShouldDeduplicatePhoneNumbers() {
        // Arrange
        // All three persons at the same address with same phone
        List<FireStation> fireStations = List.of(fireStation1);
        Set<Person> allPersons = Set.of(adult1, child1, child2); // All at 123 Main St with 555-1234

        when(fireStationRepository.getFireStationsByNumber(1)).thenReturn(fireStations);
        when(personRepository.getAllPersons()).thenReturn(allPersons);

        // Act
        List<String> result = personService.getPhoneNumbersByFireStation(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size(), "Should deduplicate and return only one phone number");
        assertEquals("555-1234", result.get(0));
    }

    /**
     * Test: getPhoneNumbersByFireStation should return empty list when no matches.
     *
     * Edge case: Fire station exists but serves no addresses with residents.
     */
    @Test
    void getPhoneNumbersByFireStation_ShouldReturnEmptyList_WhenNoPersonsServed() {
        // Arrange
        when(fireStationRepository.getFireStationsByNumber(999)).thenReturn(List.of());
        when(personRepository.getAllPersons()).thenReturn(Set.of(adult1, adult2));

        // Act
        List<String> result = personService.getPhoneNumbersByFireStation(999);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========================================
    // getPersonInfo Tests
    // ========================================

    /**
     * Test: getPersonInfo should return list of PersonInfoDTO for matching persons.
     *
     * Important: Can return multiple results (namesakes).
     * Uses PersonInfoMapper to transform Person + MedicalRecords to DTO.
     */
    @Test
    void getPersonInfo_ShouldReturnPersonInfoDTOs_ForMatchingPersons() {
        // Arrange
        Set<Person> persons = Set.of(adult1);
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                adult1, List.of(adultMedicalRecord1)
        );
        PersonInfoDTO expectedDTO = new PersonInfoDTO(
                "John Doe",
                "123 Main St",
                44,
                "john.doe@example.com",
                null
        );

        when(personRepository.getPersonsByFirstNameAndLastName("John", "Doe")).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);
        when(personInfoMapper.toDTO(adult1, List.of(adultMedicalRecord1))).thenReturn(expectedDTO);

        // Act
        List<PersonInfoDTO> result = personService.getPersonInfo("John", "Doe");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).fullName());
        assertEquals("123 Main St", result.get(0).address());
        assertEquals(44, result.get(0).age());

        verify(personRepository, times(1)).getPersonsByFirstNameAndLastName("John", "Doe");
        verify(medicalRecordRepository, times(1)).getMedicalRecordsByPersons(persons);
        verify(personInfoMapper, times(1)).toDTO(adult1, List.of(adultMedicalRecord1));
    }

    /**
     * Test: getPersonInfo should handle multiple persons with same name (namesakes).
     *
     * Real-world scenario: Multiple "John Smith" in the system.
     */
    @Test
    void getPersonInfo_ShouldReturnMultipleDTOs_WhenNamesakes() {
        // Arrange
        Person adult1Copy = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .address("999 Other St")
                .city("OtherCity")
                .zip("99999")
                .phone("555-9999")
                .email("other.john@example.com")
                .build();

        Set<Person> persons = Set.of(adult1, adult1Copy);
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                adult1, List.of(adultMedicalRecord1),
                adult1Copy, List.of(adultMedicalRecord1)
        );

        PersonInfoDTO dto1 = new PersonInfoDTO("John Doe", "123 Main St", 44, "john.doe@example.com", null);
        PersonInfoDTO dto2 = new PersonInfoDTO("John Doe", "999 Other St", 44, "other.john@example.com", null);

        when(personRepository.getPersonsByFirstNameAndLastName("John", "Doe")).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);
        when(personInfoMapper.toDTO(eq(adult1), anyList())).thenReturn(dto1);
        when(personInfoMapper.toDTO(eq(adult1Copy), anyList())).thenReturn(dto2);

        // Act
        List<PersonInfoDTO> result = personService.getPersonInfo("John", "Doe");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "Should return DTOs for both namesakes");
        verify(personInfoMapper, times(2)).toDTO(any(Person.class), anyList());
    }

    /**
     * Test: getPersonInfo should return empty list when person not found.
     *
     * Edge case: Non-existent person.
     */
    @Test
    void getPersonInfo_ShouldReturnEmptyList_WhenPersonNotFound() {
        // Arrange
        when(personRepository.getPersonsByFirstNameAndLastName("NonExistent", "Person")).thenReturn(Set.of());
        when(medicalRecordRepository.getMedicalRecordsByPersons(Set.of())).thenReturn(Map.of());

        // Act
        List<PersonInfoDTO> result = personService.getPersonInfo("NonExistent", "Person");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(personInfoMapper, never()).toDTO(any(), any());
    }

    // ========================================
    // getChildAlerts Tests
    // ========================================

    /**
     * Test: getChildAlerts should return children at address with household members.
     *
     * Complex business logic:
     * 1. Find all persons at address (household members)
     * 2. Get medical records for all household members
     * 3. Filter for children (age <= 18)
     * 4. For each child, build DTO with other household members
     */
    @Test
    void getChildAlerts_ShouldReturnChildrenWithHouseholdMembers() {
        // Arrange
        Set<Person> householdMembers = Set.of(adult1, child1, child2);
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                adult1, List.of(adultMedicalRecord1),
                child1, List.of(childMedicalRecord1),
                child2, List.of(childMedicalRecord2)
        );

        ChildAlertDTO childAlert1 = new ChildAlertDTO(
                "Tommy", "Doe", 8, List.of(adult1, child2)
        );
        ChildAlertDTO childAlert2 = new ChildAlertDTO(
                "Lucy", "Doe", 6, List.of(adult1, child1)
        );

        when(personRepository.getPersonsByAddress("123 Main St")).thenReturn(householdMembers);
        when(medicalRecordRepository.getMedicalRecordsByPersons(householdMembers)).thenReturn(medicalRecordsMap);
        when(dateService.isChildren(childMedicalRecord1.getDateOfBirth())).thenReturn(true);
        when(dateService.isChildren(childMedicalRecord2.getDateOfBirth())).thenReturn(true);
        when(dateService.isChildren(adultMedicalRecord1.getDateOfBirth())).thenReturn(false);

        when(childAlertMapper.toDTO(eq(childMedicalRecord1), anyList())).thenReturn(childAlert1);
        when(childAlertMapper.toDTO(eq(childMedicalRecord2), anyList())).thenReturn(childAlert2);

        // Act
        List<ChildAlertDTO> result = personService.getChildAlerts("123 Main St");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "Should return 2 children");
        assertTrue(result.stream().anyMatch(c -> c.firstName().equals("Tommy")));
        assertTrue(result.stream().anyMatch(c -> c.firstName().equals("Lucy")));

        verify(personRepository, times(1)).getPersonsByAddress("123 Main St");
        verify(medicalRecordRepository, times(1)).getMedicalRecordsByPersons(householdMembers);
        verify(dateService, times(3)).isChildren(any(LocalDate.class));
        verify(childAlertMapper, times(2)).toDTO(any(MedicalRecord.class), anyList());
    }

    /**
     * Test: getChildAlerts should return empty list when no children at address.
     *
     * Edge case: Address has only adults.
     */
    @Test
    void getChildAlerts_ShouldReturnEmptyList_WhenNoChildrenAtAddress() {
        // Arrange
        Set<Person> householdMembers = Set.of(adult1, adult2);
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                adult1, List.of(adultMedicalRecord1),
                adult2, List.of(adultMedicalRecord2)
        );

        when(personRepository.getPersonsByAddress("123 Main St")).thenReturn(householdMembers);
        when(medicalRecordRepository.getMedicalRecordsByPersons(householdMembers)).thenReturn(medicalRecordsMap);
        when(dateService.isChildren(adultMedicalRecord1.getDateOfBirth())).thenReturn(false);
        when(dateService.isChildren(adultMedicalRecord2.getDateOfBirth())).thenReturn(false);

        // Act
        List<ChildAlertDTO> result = personService.getChildAlerts("123 Main St");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when no children");
        verify(childAlertMapper, never()).toDTO(any(), any());
    }

    /**
     * Test: getChildAlerts should return empty list when address has no residents.
     *
     * Edge case: Non-existent or empty address.
     */
    @Test
    void getChildAlerts_ShouldReturnEmptyList_WhenNoResidentsAtAddress() {
        // Arrange
        when(personRepository.getPersonsByAddress("999 Empty St")).thenReturn(Set.of());
        when(medicalRecordRepository.getMedicalRecordsByPersons(Set.of())).thenReturn(Map.of());

        // Act
        List<ChildAlertDTO> result = personService.getChildAlerts("999 Empty St");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dateService, never()).isChildren(any());
        verify(childAlertMapper, never()).toDTO(any(), any());
    }

    /**
     * Test: getChildAlerts should correctly exclude the child from their own household members list.
     *
     * Important: Each child's DTO should contain OTHER household members, not themselves.
     * This tests the filtering logic: p -> !p.hasFullName(childMedicalRecord.getFirstName(), ...)
     */
    @Test
    void getChildAlerts_ShouldExcludeChildFromTheirOwnHouseholdList() {
        // Arrange
        Set<Person> householdMembers = Set.of(adult1, child1);
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                adult1, List.of(adultMedicalRecord1),
                child1, List.of(childMedicalRecord1)
        );

        when(personRepository.getPersonsByAddress("123 Main St")).thenReturn(householdMembers);
        when(medicalRecordRepository.getMedicalRecordsByPersons(householdMembers)).thenReturn(medicalRecordsMap);
        when(dateService.isChildren(childMedicalRecord1.getDateOfBirth())).thenReturn(true);
        when(dateService.isChildren(adultMedicalRecord1.getDateOfBirth())).thenReturn(false);

        // Capture the otherHouseholdMembers list passed to mapper
        when(childAlertMapper.toDTO(eq(childMedicalRecord1), anyList())).thenAnswer(invocation -> {
            List<Person> otherMembers = invocation.getArgument(1);
            // Verify that child1 is NOT in the list of other household members
            assertFalse(otherMembers.contains(child1), "Child should not be in their own household members list");
            assertTrue(otherMembers.contains(adult1), "Adult should be in the list");
            assertEquals(1, otherMembers.size(), "Should only contain adult1");
            return new ChildAlertDTO("Tommy", "Doe", 8, otherMembers);
        });

        // Act
        List<ChildAlertDTO> result = personService.getChildAlerts("123 Main St");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(childAlertMapper, times(1)).toDTO(eq(childMedicalRecord1), anyList());
    }

    // ========================================
    // getFloodDTO Tests
    // ========================================

    /**
     * Test: getFloodDTO should return null (not yet implemented).
     *
     * Edge case: Testing incomplete functionality.
     * This test documents the current behavior and will need updating when implemented.
     */
    @Test
    void getFloodDTO_ShouldReturnNull_WhenNotYetImplemented() {
        // Act
        List<FloodDTO> result = personService.getFloodDTO(List.of(1, 2, 3));

        // Assert
        assertNull(result, "Method not yet implemented, should return null");
        // Verify no repository interactions since method is not implemented
        verifyNoInteractions(fireStationRepository, personRepository, medicalRecordRepository);
    }
}
