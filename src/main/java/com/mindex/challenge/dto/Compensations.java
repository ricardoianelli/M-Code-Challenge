package com.mindex.challenge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Compensations {
    public String employee;
    public List<CompensationDto> compensations = new ArrayList<>();

    @JsonIgnore
    public long totalCompensations;
    @JsonIgnore
    public int totalPages;

    public Compensations() {
    }

    public Compensations(String employee) {
        this.employee = employee;
    }

    public Compensations(String employee, List<CompensationDto> compensations) {
        this.employee = employee;
        this.compensations = compensations;
    }
}
