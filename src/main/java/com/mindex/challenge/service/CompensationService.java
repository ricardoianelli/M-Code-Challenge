package com.mindex.challenge.service;

import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;

public interface CompensationService {
    CompensationDto create(String employeeId, CompensationDto compensationDto);
    Compensations read(String employeeId);
}
