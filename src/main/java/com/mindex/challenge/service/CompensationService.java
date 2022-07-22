package com.mindex.challenge.service;

import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
import org.springframework.data.domain.Pageable;

public interface CompensationService {
    CompensationDto create(String employeeId, CompensationDto compensationDto);
    Compensations read(Pageable pagination, String employeeId);
}
