package com.mindex.challenge.dto;

public class CompensationDto {
    public String salary;
    public String effectiveDate;

    public CompensationDto() {
    }

    public CompensationDto(String salary, String effectiveDate) {
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }
}
