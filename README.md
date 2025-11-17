# SafetyNet Alerts


## Description

> *SafetyNet Alerts* is a lab where you create the backend of an emergency alerting system.

This is a Spring Boot 3.4.2 REST API application for SafetyNet, 
a system that manages information about persons, fire stations, 
and medical records. 

The project uses **Java 17** and follows standard Spring Boot conventions 
with **Maven** as the build tool.

The **GitHub Project** contains the issues and current state.
You can find it here: https://github.com/orgs/ebouchut-laplateforme/projects/5

## Tech Stack

- **Java** version 17
- **Spring Boot** version 3.4.2 (Backend framework)
- **Maven** 3.9.11 (build tool) 
    - Maven Dependencies:
        - Spring Web (RESTful services)
        - Spring Boot DevTools (hot reload)
        - Spring Boot Actuator (monitoring)
        - Lombok (boilerplate reduction)
        - Log4j2 (logging)
        - JUnit 5 (testing)
        - JaCoCo (code coverage)

Java and Maven are managed via [SDKMAN!](https://sdkman.io/).


## Lab Statement and Specifications

Sources:
- https://github.com/ebouchut-laplateforme/java-springboot-safetynet/blob/main/docs/safetynet-specifications-tech_stack.pdf
- https://github.com/ebouchut-laplateforme/java-springboot-safetynet/blob/main/docs/safetynet-lab_statement.pdf

The following partly paraphrases the above documents.

*SafetyNet Alerts* must meet the following **requirements**:

- [ ] The SafetyNet alert server starts up.
- [ ] All URL endpoints are functional. 
- [ ] The Actuators health, info, trace,
and metrics are functional.
- [ ] All URL endpoints log their requests and responses: 
    - [ ] **Successful** responses are logged at the `INFO` level
    - [ ] **Errors** or exceptions are logged at the `ERROR` level
    - [ ] **informative** steps or calculations are logged at the `DEBUG` level
- [ ] **Maven** is functional:
    - [ ] Maven **runs unit tests** 
    - [ ] Maven **runs code coverage**.
- [ ] All URL endpoints and all additional features are covered
by **unit tests**.
- [ ] Compilation generates a **Surefire test report on** JUnit **test results**.
- [ ] The build includes a **JaCoCo coverage report** and achieves **80% code coverage**.


## Architecture

*SafetyNet Alerts* is developed using **MVC** architecture.  
The code base adheres to **SOLID principles**:

- Single responsibility principle
- Open/Closed principle
- Liskov substitution principle
- Interface segregation principle
- Dependency inversion principle


## REST Endpoints

- http://localhost:8080/person  
  This endpoint will allow you to perform the following actions via `POST`/`PUT`/`DELETE` HTTP requests:  
    - **Add** a new person;
    - **Update** an existing person  
    For now, let's assume that the first name and last name do not change, 
    but that the other fields can be modified.
    - **Delete** a person  
    Use a combination of first name and last name as a unique identifier.  
    To use this endpoint, you must first **create an API key** 
    in the API section of your account.  
    Once you have created an API key, you can use it to make requests to the API.
- http://localhost:8080/firestation  
  This endpoint will allow you to perform the following actions via `POST`/`PUT`/`DELETE` HTTP requests:
    - **Add** a fire station/address mapping.
    - **Update** the fire station number for an address.
    - **Delete** the mapping for a fire station or address.
- http://localhost:8080/medicalRecord  
  This endpoint will allow you to perform the following actions via HTTP Post/Put/Delete:
  - **Add** a medical record  
    (first and last name do not change).
  - **Update** an existing medical record  
    (as mentioned above, assuming that the first and last name do not change).
  - **Delete** a medical record  
    (use a combination of first and last name as a unique identifier).


## Generated Files

*SafetyNet Alerts* must have endpoints that provide information about their status.  
When your application reads the data file containing names and addresses, 
*SafetyNet Alerts* must output a **JSON file** from the corresponding **URLs** described below:

- `http://localhost:8080/firestation?stationNumber=<station_number>`
  This URL must return a list of people covered by the corresponding fire station.
  So, if the station number is `1`, it should return the residents covered by station number 1. 
- The list should include the following specific information: first name, last name, address, phone number.  
- In addition, it should provide a breakdown of the number of adults and the number of children (any individual aged 18 or
  under) in the area served.
- `http://localhost:8080/childAlert?address=<address>`  
  This URL must return a list of children (any individual aged 18 or under) living at this address.
  The list must include each child's first name and last name, their age and a list of other
  members of the household. If there are no children, this URL may return an empty string.
- `http://localhost:8080/phoneAlert?firestation=<firestation_number>`  
  This URL should return a list of phone numbers of residents served by the fire station.
  We will use it to send emergency text messages to specific households.
- `http://localhost:8080/fire?address=<address>`  
  This URL should return a list of residents living at the given address and the number of the fire station
  serving that address. The list should include the name, telephone number, age and medical history
  (medications, dosages and allergies) of each person.
- `http://localhost:8080/flood/stations?stations=<a list of station_numbers>`    
  This URL should return a list of all households served by the fire station.  
  This list should group people by address.  
  It should also include the name, phone number and age of the residents, 
  and list their medical history (medications, dosage and allergies) 
  next to each name.
- `http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>`  
  This URL must return the name, address, age, email address, and medical history (medications,
  dosage, allergies) of each resident. If several people have the same name, they must
  all appear.
- `http://localhost:8080/communityEmail?city=<city>`  
  This URL should return the email addresses of all residents of the city.