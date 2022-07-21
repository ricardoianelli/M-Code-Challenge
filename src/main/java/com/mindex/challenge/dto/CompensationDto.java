package com.mindex.challenge.dto;

public class CompensationDto {
    public String employeeId;
    public String salary;
    public String effectiveDate;

    public CompensationDto() {
    }

    public CompensationDto(String employeeId, String salary, String effectiveDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }
}
