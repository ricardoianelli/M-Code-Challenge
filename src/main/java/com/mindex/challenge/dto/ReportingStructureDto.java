package com.mindex.challenge.dto;

public class ReportingStructureDto {
    public EmployeeDto employee;
    public int numberOfReports;

    public ReportingStructureDto() {
    }

    public ReportingStructureDto(EmployeeDto employee) {
        this.employee = employee;
        this.numberOfReports = employee.directReports.size();
    }
}