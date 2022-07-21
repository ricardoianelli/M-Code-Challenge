package com.mindex.challenge.service;

import com.mindex.challenge.dto.CompensationDto;

public interface CompensationService {
    CompensationDto create(CompensationDto compensationDto);
    CompensationDto read(String employeeId);
}
