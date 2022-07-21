package com.mindex.challenge.adapter;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;

public interface CompensationAdapter {
    Compensation dtoToEntity(CompensationDto dto);
    CompensationDto entityToDto(Compensation compensation);
}
