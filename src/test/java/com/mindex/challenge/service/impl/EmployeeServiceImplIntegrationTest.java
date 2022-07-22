package com.mindex.challenge.service.impl;

import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplIntegrationTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    //Since employeeId is defined as readonly (for documentation reasons), restTemplate can't put the employeeId on it during deserialization.
    //That issue only happens here, in this test, so that's why I have extra logic to manually put it.
    private String getEmployeeIdFromHttpEntity(HttpEntity<EmployeeDto> httpEntity) {
        String employeeLocation = httpEntity.getHeaders().get("location").get(0);
        return employeeLocation.substring(employeeLocation.lastIndexOf('/') + 1);
    }

    @Test
    @DisplayName("Integration test for Employee endpoints")
    public void testCreateReadUpdate() {

        EmployeeDto testEmployee = new EmployeeDto(null, "John", "Doe", "Engineering", "Developer");

        // Create checks
        HttpEntity<EmployeeDto> request = new HttpEntity<>(testEmployee);
        ResponseEntity<EmployeeDto> createdEmployeeResponse = restTemplate.postForEntity(employeeUrl, request, EmployeeDto.class);
        EmployeeDto createdEmployee = createdEmployeeResponse.getBody();

        //Since employeeId is defined as readonly (for documentation reasons), restTemplate can't put the employeeId on it during deserialization.
        //That issue only happens here, in this test, so that's why I have extra logic to manually put it.
        createdEmployee.employeeId = getEmployeeIdFromHttpEntity(createdEmployeeResponse);

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.employeeId);
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        // Read checks
        ResponseEntity<EmployeeDto> readEmployeeResponse = restTemplate.getForEntity(employeeIdUrl, EmployeeDto.class, createdEmployee.employeeId);
        EmployeeDto readEmployee = readEmployeeResponse.getBody();
        readEmployee.employeeId = getEmployeeIdFromHttpEntity(createdEmployeeResponse);

        assertNotNull(readEmployee);
        assertEquals(createdEmployee.employeeId, readEmployee.employeeId);
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.position = "Development Manager";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeDto updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(readEmployee, headers),
                        EmployeeDto.class,
                        readEmployee.employeeId).getBody();

        assertNotNull(updatedEmployee);
        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(EmployeeDto expected, EmployeeDto actual) {
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
        assertEquals(expected.position, actual.position);
        assertEquals(expected.department, actual.department);
    }
}
