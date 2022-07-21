package com.mindex.challenge.dto;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDto {
    public String employeeId;
    public String firstName;
    public String lastName;
    public String position;
    public String department;
    public List<String> directReports = new ArrayList<>();

    public EmployeeDto() {
    }

    public EmployeeDto(String employeeId, String firstName, String lastName, String position, String department) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
    }

    public EmployeeDto(String employeeId, String firstName, String lastName, String position, String department, List<String> directReports) {
        this(employeeId, firstName, lastName, position, department);
        this.directReports = directReports;
    }
}
