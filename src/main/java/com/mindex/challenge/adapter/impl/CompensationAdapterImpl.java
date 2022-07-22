package com.mindex.challenge.adapter.impl;

import com.mindex.challenge.adapter.CompensationAdapter;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class CompensationAdapterImpl implements CompensationAdapter {
    @Override
    public Compensation dtoToEntity(String employeeId, CompensationDto dto) {
        return new Compensation(employeeId, new BigDecimal(dto.salary), LocalDate.parse(dto.effectiveDate));
    }

    @Override
    public CompensationDto entityToDto(Compensation compensation) {
        return new CompensationDto(compensation.getSalary().toString(), compensation.getEffectiveDate().toString());
    }

    @Override
    public Compensations listOfEntityToCompensations(String employeeId, List<Compensation> listOfCompensations) {
        Compensations compensations = new Compensations(employeeId);
        for (Compensation compensation : listOfCompensations) {
            compensations.compensations.add(entityToDto(compensation));
        }

        return compensations;
    }
}
