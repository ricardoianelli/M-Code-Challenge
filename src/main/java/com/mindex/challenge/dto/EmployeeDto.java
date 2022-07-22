package com.mindex.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String employeeId;
    @NotBlank
    @Size(min = 2, message = "First name should have at least 2 characters")
    public String firstName;
    @NotBlank
    @Size(min = 2, message = "Last name should have at least 2 characters")
    public String lastName;
    @NotBlank
    @Size(min = 2, message = "Position name should have at least 2 characters")
    public String position;
    @NotBlank
    @Size(min = 2, message = "Department name should have at least 2 characters")
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
