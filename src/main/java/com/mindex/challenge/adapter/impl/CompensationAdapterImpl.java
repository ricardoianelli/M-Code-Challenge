package com.mindex.challenge.adapter.impl;

import com.mindex.challenge.adapter.CompensationAdapter;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompensationAdapterImpl implements CompensationAdapter {

    @Autowired
    CompensationRepository compensationRepository;

    @Override
    public Compensation dtoToEntity(CompensationDto dto) {
        return null;
    }

    @Override
    public CompensationDto entityToDto(Compensation compensation) {
        return null;
    }
}
