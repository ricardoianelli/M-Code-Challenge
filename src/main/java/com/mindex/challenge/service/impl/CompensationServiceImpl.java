package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.CompensationAdapter;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private CompensationAdapter compensationAdapter;


    @Override
    public CompensationDto create(CompensationDto compensationDto) {
        return null;
    }

    @Override
    public CompensationDto read(String employeeId) {
        return null;
    }
}
