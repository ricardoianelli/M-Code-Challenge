package com.mindex.challenge.adapter.impl;

import com.mindex.challenge.adapter.CompensationAdapter;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CompensationAdapterImpl implements CompensationAdapter {
    @Override
    public Compensation dtoToEntity(CompensationDto dto) {
        return new Compensation(dto.employeeId, new BigDecimal(dto.salary), LocalDate.parse(dto.effectiveDate));
    }

    @Override
    public CompensationDto entityToDto(Compensation compensation) {
        return new CompensationDto(compensation.getEmployeeId(), compensation.getSalary().toString(), compensation.getEffectiveDate().toString());
    }
}
