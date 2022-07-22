package com.mindex.challenge.adapter;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;

import java.util.List;

public interface CompensationAdapter {
    Compensation dtoToEntity(String employeeId, CompensationDto dto);
    CompensationDto entityToDto(Compensation compensation);
    Compensations listOfEntityToCompensations(String employeeId, List<Compensation> listOfCompensations);
}
