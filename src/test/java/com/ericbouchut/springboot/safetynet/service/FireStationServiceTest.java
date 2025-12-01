package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.dto.FireStationDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link FireStationService}.
 *
 * This test class uses Mockito to mock all dependencies and test each method in isolation.
 * Key testing strategies:
 * - Mock all repository dependencies
 * - Mock DateService for age calculations
 * - Test both happy paths and edge cases
 * - Verify correct interactions with dependencies
 */
@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {

    @Mock
    private DateService dateService;

    @Mock
    private FireStationRepository fireStationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private FireStationService fireStationService;

    private FireStation fireStation1;
    private FireStation fireStation2;
    private Person person1;
    private Person person2;
    private Person person3;
    private MedicalRecord childMedicalRecord;
    private MedicalRecord adultMedicalRecord1;
    private MedicalRecord adultMedicalRecord2;

    /**
     * Setup test data before each test.
     * Creates a realistic scenario with:
     * - 2 fire stations with the same station number
     * - 3 persons living at different addresses
     * - Medical records for children and adults
     */
    @BeforeEach
    void setUp() {
        // Fire stations
        fireStation1 = new FireStation("123 Main St", 1);
        fireStation2 = new FireStation("456 Oak Ave", 1);

        // Persons
        person1 = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .city("Springfield")
                .zip("12345")
                .phone("555-1234")
                .email("john.doe@example.com")
                .build();

        person2 = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .address("456 Oak Ave")
                .city("Springfield")
                .zip("12345")
                .phone("555-5678")
                .email("jane.smith@example.com")
                .build();

        person3 = Person.builder()
                .firstName("Tommy")
                .lastName("Young")
                .address("123 Main St")
                .city("Springfield")
                .zip("12345")
                .phone("555-9999")
                .email("tommy.young@example.com")
                .build();

        // Medical records
        childMedicalRecord = new MedicalRecord(
                "Tommy",
                "Young",
                LocalDate.of(2015, 6, 15), // 8-9 years old
                Set.of("aznol:350mg"),
                Set.of("peanut")
        );

        adultMedicalRecord1 = new MedicalRecord(
                "John",
                "Doe",
                LocalDate.of(1980, 1, 1), // 44-45 years old
                Set.of("aspirin:100mg"),
                Set.of()
        );

        adultMedicalRecord2 = new MedicalRecord(
                "Jane",
                "Smith",
                LocalDate.of(1975, 5, 20), // 49-50 years old
                Set.of(),
                Set.of("shellfish")
        );
    }

    /**
     * Test: getAllFireStations should return all fire stations without duplicates.
     *
     * Design choice: Uses Set to prevent duplicates (as per repository contract).
     */
    @Test
    void getAllFireStations_ShouldReturnAllFireStations() {
        // Arrange
        Set<FireStation> expectedFireStations = Set.of(fireStation1, fireStation2);
        when(fireStationRepository.getAllFireStations()).thenReturn(expectedFireStations);

        // Act
        Set<FireStation> result = fireStationService.getAllFireStations();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(fireStation1));
        assertTrue(result.contains(fireStation2));
        verify(fireStationRepository, times(1)).getAllFireStations();
    }

    /**
     * Test: getAllFireStations should return empty set when no fire stations exist.
     *
     * Edge case: Empty data scenario.
     */
    @Test
    void getAllFireStations_ShouldReturnEmptySet_WhenNoFireStations() {
        // Arrange
        when(fireStationRepository.getAllFireStations()).thenReturn(Set.of());

        // Act
        Set<FireStation> result = fireStationService.getAllFireStations();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fireStationRepository, times(1)).getAllFireStations();
    }

    /**
     * Test: getFireStationsByNumber should return all fire stations with matching number.
     *
     * Important: Multiple fire stations can share the same station number.
     */
    @Test
    void getFireStationsByNumber_ShouldReturnMatchingFireStations() {
        // Arrange
        List<FireStation> expectedFireStations = List.of(fireStation1, fireStation2);
        when(fireStationRepository.getFireStationsByNumber(1)).thenReturn(expectedFireStations);

        // Act
        List<FireStation> result = fireStationService.getFireStationsByNumber(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(fireStation1));
        assertTrue(result.contains(fireStation2));
        verify(fireStationRepository, times(1)).getFireStationsByNumber(1);
    }

    /**
     * Test: getFireStationsByNumber should return empty list when no matching stations.
     *
     * Edge case: Non-existent station number.
     */
    @Test
    void getFireStationsByNumber_ShouldReturnEmptyList_WhenNoMatch() {
        // Arrange
        when(fireStationRepository.getFireStationsByNumber(999)).thenReturn(List.of());

        // Act
        List<FireStation> result = fireStationService.getFireStationsByNumber(999);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fireStationRepository, times(1)).getFireStationsByNumber(999);
    }

    /**
     * Test: getFireStationDTOByNumber should return correct DTO with persons and counts.
     *
     * This is the most complex method, testing:
     * - Address aggregation across multiple fire stations
     * - Person lookup by addresses
     * - Medical record lookup
     * - Age calculation and partitioning (children vs adults)
     * - DTO construction
     */
    @Test
    void getFireStationDTOByNumber_ShouldReturnCorrectDTO_WithChildrenAndAdults() {
        // Arrange
        Set<String> addresses = Set.of("123 Main St", "456 Oak Ave");
        Set<Person> persons = Set.of(person1, person2, person3);

        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                person1, List.of(adultMedicalRecord1),
                person2, List.of(adultMedicalRecord2),
                person3, List.of(childMedicalRecord)
        );

        when(fireStationRepository.getFireStationAddressesByNumber(1)).thenReturn(addresses);
        when(personRepository.getPersonsByAddresses(addresses)).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);

        // Mock age calculations
        when(dateService.isChildren(childMedicalRecord.getDateOfBirth())).thenReturn(true);
        when(dateService.isChildren(adultMedicalRecord1.getDateOfBirth())).thenReturn(false);
        when(dateService.isChildren(adultMedicalRecord2.getDateOfBirth())).thenReturn(false);

        // Act
        FireStationDTO result = fireStationService.getFireStationDTOByNumber(1);

        // Assert
        assertNotNull(result);

        // Verify counts
        assertEquals(2L, result.adultsCount(), "Should have 2 adults");
        assertEquals(1L, result.childrenCount(), "Should have 1 child");

        // Verify persons list
        assertEquals(3, result.persons().size(), "Should have 3 persons total");

        // Verify person DTOs contain correct data
        List<FireStationDTO.PersonDTO> personsDTO = result.persons();
        assertTrue(personsDTO.stream().anyMatch(p ->
                p.firstName().equals("John") &&
                p.lastName().equals("Doe") &&
                p.address().equals("123 Main St") &&
                p.phone().equals("555-1234")
        ));
        assertTrue(personsDTO.stream().anyMatch(p ->
                p.firstName().equals("Jane") &&
                p.lastName().equals("Smith")
        ));
        assertTrue(personsDTO.stream().anyMatch(p ->
                p.firstName().equals("Tommy") &&
                p.lastName().equals("Young")
        ));

        // Verify all dependencies were called
        verify(fireStationRepository, times(1)).getFireStationAddressesByNumber(1);
        verify(personRepository, times(1)).getPersonsByAddresses(addresses);
        verify(medicalRecordRepository, times(1)).getMedicalRecordsByPersons(persons);
        verify(dateService, times(3)).isChildren(any(LocalDate.class));
    }

    /**
     * Test: getFireStationDTOByNumber with only children.
     *
     * Edge case: All persons are children (adults count should be 0).
     */
    @Test
    void getFireStationDTOByNumber_ShouldReturnCorrectCounts_WhenOnlyChildren() {
        // Arrange
        Set<String> addresses = Set.of("123 Main St");
        Set<Person> persons = Set.of(person3);

        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                person3, List.of(childMedicalRecord)
        );

        when(fireStationRepository.getFireStationAddressesByNumber(1)).thenReturn(addresses);
        when(personRepository.getPersonsByAddresses(addresses)).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);
        when(dateService.isChildren(childMedicalRecord.getDateOfBirth())).thenReturn(true);

        // Act
        FireStationDTO result = fireStationService.getFireStationDTOByNumber(1);

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.adultsCount(), "Should have 0 adults");
        assertEquals(1L, result.childrenCount(), "Should have 1 child");
        assertEquals(1, result.persons().size());
    }

    /**
     * Test: getFireStationDTOByNumber with only adults.
     *
     * Edge case: All persons are adults (children count should be 0).
     */
    @Test
    void getFireStationDTOByNumber_ShouldReturnCorrectCounts_WhenOnlyAdults() {
        // Arrange
        Set<String> addresses = Set.of("123 Main St");
        Set<Person> persons = Set.of(person1);

        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                person1, List.of(adultMedicalRecord1)
        );

        when(fireStationRepository.getFireStationAddressesByNumber(1)).thenReturn(addresses);
        when(personRepository.getPersonsByAddresses(addresses)).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);
        when(dateService.isChildren(adultMedicalRecord1.getDateOfBirth())).thenReturn(false);

        // Act
        FireStationDTO result = fireStationService.getFireStationDTOByNumber(1);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.adultsCount(), "Should have 1 adult");
        assertEquals(0L, result.childrenCount(), "Should have 0 children");
        assertEquals(1, result.persons().size());
    }

    /**
     * Test: getFireStationDTOByNumber with no persons served.
     *
     * Edge case: Fire station exists but serves no addresses with persons.
     */
    @Test
    void getFireStationDTOByNumber_ShouldReturnEmptyDTO_WhenNoPersonsServed() {
        // Arrange
        Set<String> addresses = Set.of("789 Empty St");
        Set<Person> persons = Set.of();
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of();

        when(fireStationRepository.getFireStationAddressesByNumber(1)).thenReturn(addresses);
        when(personRepository.getPersonsByAddresses(addresses)).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);

        // Act
        FireStationDTO result = fireStationService.getFireStationDTOByNumber(1);

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.adultsCount(), "Should have 0 adults");
        assertEquals(0L, result.childrenCount(), "Should have 0 children");
        assertTrue(result.persons().isEmpty(), "Persons list should be empty");
    }

    /**
     * Test: getFireStationDTOByNumber with person having multiple medical records.
     *
     * Important: Tests the distinct() operation in the service to handle duplicates.
     * Note: In real scenarios, a person should only have one medical record,
     * but the code handles potential duplicates defensively.
     */
    @Test
    void getFireStationDTOByNumber_ShouldHandleDuplicateMedicalRecords() {
        // Arrange
        Set<String> addresses = Set.of("123 Main St");
        Set<Person> persons = Set.of(person1);

        // Simulate duplicate medical records (edge case)
        Map<Person, List<MedicalRecord>> medicalRecordsMap = Map.of(
                person1, List.of(adultMedicalRecord1, adultMedicalRecord1) // Duplicate
        );

        when(fireStationRepository.getFireStationAddressesByNumber(1)).thenReturn(addresses);
        when(personRepository.getPersonsByAddresses(addresses)).thenReturn(persons);
        when(medicalRecordRepository.getMedicalRecordsByPersons(persons)).thenReturn(medicalRecordsMap);
        when(dateService.isChildren(adultMedicalRecord1.getDateOfBirth())).thenReturn(false);

        // Act
        FireStationDTO result = fireStationService.getFireStationDTOByNumber(1);

        // Assert
        assertNotNull(result);
        // The distinct() in the service should handle duplicates
        assertEquals(1L, result.adultsCount(), "Should count adult only once despite duplicate records");
        assertEquals(0L, result.childrenCount());
        assertEquals(1, result.persons().size());
    }
}
