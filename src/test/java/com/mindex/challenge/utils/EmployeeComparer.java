package com.mindex.challenge.utils;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

public class EmployeeComparer {
    public static void compareEntityAndDto(Employee employee, EmployeeDto dto) {
        assertAll("Should match employee entity and DTO",
                () -> assertEquals("Id match", employee.getEmployeeId(), dto.employeeId),
                () -> assertEquals("First name match",employee.getFirstName(), dto.firstName),
                () -> assertEquals("Last name match",employee.getLastName(), dto.lastName),
                () -> assertEquals("Position match",employee.getPosition(), dto.position),
                () -> assertEquals("Department match",employee.getDepartment(), dto.department),
                () -> assertTrue("Direct reports match",compareDirectReportsFromEntityAndDto(employee, dto))
        );
    }

    private static boolean compareDirectReportsFromEntityAndDto(Employee employee, EmployeeDto dto) {
        if (employee.getDirectReports().size() != dto.directReports.size()) {
            return false;
        }

        for (Employee directReport : employee.getDirectReports()) {
            if (!dto.directReports.contains(directReport.getEmployeeId())) {
                return false;
            }
        }

        return true;
    }
}
