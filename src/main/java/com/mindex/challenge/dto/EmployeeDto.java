package com.mindex.challenge.dto;

import java.util.HashSet;
import java.util.Set;

public class EmployeeDto {
    public String employeeId;
    public String firstName;
    public String lastName;
    public String position;
    public String department;
    public Set<String> directReports = new HashSet<>();

    public EmployeeDto() {
    }

    public EmployeeDto(String employeeId, String firstName, String lastName, String position, String department) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
    }

    public EmployeeDto(String employeeId, String firstName, String lastName, String position, String department, HashSet<String> directReports) {
        this(employeeId, firstName, lastName, position, department);
        this.directReports = directReports;
    }
}
