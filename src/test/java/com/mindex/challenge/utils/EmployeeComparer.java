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
                () -> assertEquals("First name match", employee.getFirstName(), dto.firstName),
                () -> assertEquals("Last name match", employee.getLastName(), dto.lastName),
                () -> assertEquals("Position match", employee.getPosition(), dto.position),
                () -> assertEquals("Department match", employee.getDepartment(), dto.department),
                () -> assertTrue("Direct reports match", compareDirectReportsFromEntityAndDto(employee, dto))
        );
    }

    public static void compareEntities(Employee employee1, Employee employee2) {
        assertAll("Should match two employee entities",
                () -> assertEquals("Id match", employee1.getEmployeeId(),employee2.getEmployeeId()),
                () -> assertEquals("First name match", employee1.getFirstName(), employee2.getFirstName()),
                () -> assertEquals("Last name match", employee1.getLastName(), employee2.getLastName()),
                () -> assertEquals("Position match", employee1.getPosition(), employee2.getPosition()),
                () -> assertEquals("Department match", employee1.getDepartment(), employee2.getDepartment()),
                () -> assertTrue("Direct reports match", compareDirectReportsFromEntities(employee1, employee2))
        );
    }

    public static void compareDtos(EmployeeDto dto1, EmployeeDto dto2) {
        assertAll("Should match employee entity and DTO",
                () -> assertEquals("Id match", dto1.employeeId, dto2.employeeId),
                () -> assertEquals("First name match", dto1.firstName, dto2.firstName),
                () -> assertEquals("Last name match", dto1.lastName, dto2.lastName),
                () -> assertEquals("Position match", dto1.position, dto2.position),
                () -> assertEquals("Department match", dto1.department, dto2.department),
                () -> assertTrue("Direct reports match",compareDirectReportsFromDtos(dto1, dto2))
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

    private static boolean compareDirectReportsFromEntities(Employee employee1, Employee employee2) {
        if (employee1.getDirectReports().size() != employee2.getDirectReports().size()) {
            return false;
        }

        return employee1.getDirectReports().containsAll(employee2.getDirectReports());
    }

    private static boolean compareDirectReportsFromDtos(EmployeeDto employee1, EmployeeDto employee2) {
        if (employee1.directReports.size() != employee2.directReports.size()) {
            return false;
        }

        return employee1.directReports.containsAll(employee2.directReports);
    }
}
