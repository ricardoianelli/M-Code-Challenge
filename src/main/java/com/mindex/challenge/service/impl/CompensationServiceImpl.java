package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.CompensationAdapter;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.exceptions.CompensationNotFoundException;
import com.mindex.challenge.exceptions.DuplicateCompensationException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationAdapter compensationAdapter;

    @Override
    public CompensationDto create(CompensationDto compensationDto) {
        checkForDuplication(compensationDto);
        validateEmployee(compensationDto.employeeId);

        Compensation compensation = compensationAdapter.dtoToEntity(compensationDto);
        compensationRepository.insert(compensation);
        return compensationDto;
    }

    private void validateEmployee(String employeeId) {
        Employee existingEmployee = employeeRepository.findByEmployeeId(employeeId);
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    private void checkForDuplication(CompensationDto compensationDto) {
        Compensation preExisting = compensationRepository.findByEmployeeId(compensationDto.employeeId);
        if (preExisting != null) {
            throw new DuplicateCompensationException(compensationDto.employeeId);
        }
    }

    @Override
    public CompensationDto read(String employeeId) {
        validateEmployee(employeeId);

        Compensation preExisting = compensationRepository.findByEmployeeId(employeeId);
        if (preExisting == null) {
            throw new CompensationNotFoundException(employeeId);
        }

        return compensationAdapter.entityToDto(preExisting);
    }
}
