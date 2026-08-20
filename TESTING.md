# Testing Strategies in Spring Boot

## Repository and Service Testing Options

When testing services like `PersonService.calculateAge()`, you have multiple approaches for handling dependencies like `PersonRepository` and `FireStationRepository`.

### Option 1: Pure unit test (fastest, recommended for service tests)

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FireStationRepository fireStationRepository;

    @Mock  // or inject real Clock
    private Clock clock;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        // Manually create service with mocked dependencies
        personService = new PersonService(personRepository, fireStationRepository, clock);
    }

    @Test
    void calculateAge_shouldReturn35() {
        // Use personService with mocked repos
    }
}
```

### Option 2: Use @InjectMocks (Mockito auto-wires mocks)

```java
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FireStationRepository fireStationRepository;

    @Mock
    private Clock clock;

    @InjectMocks  // Mockito automatically injects the mocks above
    private PersonService personService;

    @Test
    void calculateAge_shouldReturn35() {
        // personService already has mocks injected
    }
}
```

### Option 3: Spring context with real service (integration test)

```java
@SpringBootTest
class PersonServiceTest {

    @MockBean  // Spring mock
    private PersonRepository personRepository;

    @MockBean
    private FireStationRepository fireStationRepository;

    @Autowired  // Real service with mocked dependencies
    private PersonService personService;

    @Test
    void calculateAge_shouldReturn35() {
        // Uses real PersonService with mocked repos
    }
}
```

---

## Use Cases for Each Testing Approach

### Option 1: Manual instantiation (Pure Unit Test)

**Use when:**
- Testing a single class in complete isolation
- You need full control over object creation
- Dependencies have complex setup logic
- You want the fastest possible tests
- The class under test has multiple constructors

**Pros:**
- Explicit and clear
- No magic/annotations
- Easiest to debug
- Works without any test framework

**Cons:**
- More verbose
- Manual wiring can be tedious with many dependencies

**Example:**
```java
@BeforeEach
void setUp() {
    Clock fixedClock = Clock.fixed(Instant.parse("2025-11-20T00:00:00Z"), ZoneId.systemDefault());
    personService = new PersonService(mockRepo1, mockRepo2, fixedClock);
}
```

---

### Option 2: @InjectMocks (Mockito Unit Test)

**Use when:**
- Testing service layer with mocked dependencies
- You want automatic mock injection
- The class has straightforward constructor/field injection
- You want fast tests without Spring context

**Pros:**
- Less boilerplate than manual instantiation
- Still fast (no Spring context)
- Good balance of convenience and speed
- Standard Mockito approach

**Cons:**
- Annotation "magic" can hide what's happening
- Doesn't work well with complex initialization logic
- Can be confusing when injection fails silently

**Best for:** Service layer unit tests (most common case)

---

### Option 3: @SpringBootTest + @MockBean (Integration Test)

**Use when:**
- Testing Spring-specific features (AOP, transactions, security)
- Testing auto-configuration
- Verifying Spring beans wire correctly
- Testing `@Value`, `@ConfigurationProperties`
- Testing controllers with `@WebMvcTest`
- You need real Spring infrastructure (like Jackson ObjectMapper with custom config)

**Pros:**
- Tests real Spring configuration
- Catches wiring issues
- Tests how components work together
- Good for end-to-end scenarios

**Cons:**
- **Much slower** (loads Spring context - seconds vs milliseconds)
- More memory intensive
- Can hide unit-level bugs
- Harder to isolate failures

**Best for:** Integration/slice tests, testing Spring-specific behavior

---

## Decision Tree

```
Are you testing Spring-specific behavior?
├─ YES → Use @SpringBootTest + @MockBean (Option 3)
│         Examples: @Transactional, @Cacheable, @Async, REST controllers
│
└─ NO → Are you testing pure business logic?
          ├─ YES → Use @InjectMocks (Option 2)
          │         Examples: Service methods, calculations, validation
          │
          └─ Need complex setup? → Manual instantiation (Option 1)
                    Examples: Multiple constructor variations, builders
```

---

## Real-World Example

```java
// Option 2: Fast unit test for business logic
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock PersonRepository repo;
    @InjectMocks PersonService service;

    @Test
    void calculateAge_shouldReturn35() { /* ... */ }
}

// Option 3: Integration test for REST endpoint
@WebMvcTest(PersonController.class)
class PersonControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean PersonService service;

    @Test
    void getPersonAge_shouldReturn200() throws Exception {
        mockMvc.perform(get("/persons/age"))
               .andExpect(status().isOk());
    }
}
```

---

## Recommendation

**Rule of thumb:** Start with Option 2 (@InjectMocks) for 90% of service tests. Use Option 3 only when you need to verify Spring integration.
