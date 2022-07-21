package com.mindex.challenge.dto;

public class ReportingStructure {
    public EmployeeDto employee;
    public int numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(EmployeeDto employee) {
        this.employee = employee;
        this.numberOfReports = employee.directReports.size();
    }
}