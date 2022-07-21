package com.mindex.challenge.dto;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ReportingStructureTests {

    @Test
    @DisplayName("numberOfReports has the same size of EmployeeDto directReports set")
    public void construct_givenAnEmployeeWithDirectReports_hasCorrectNumberOfReports() {
        EmployeeDto employeeDto = new EmployeeDto("0", "Ricardo", "Ianelli", "Software Engineer", "IT");

        Random randomNumberGenerator = new Random();
        int numberOfReports = randomNumberGenerator.nextInt(20);

        for (int i = 1; i <= numberOfReports; i++) {
            employeeDto.directReports.add(i + "");
        }

        ReportingStructure reportingStructure = new ReportingStructure(employeeDto);

        assertEquals(reportingStructure.numberOfReports, numberOfReports);
    }
}
