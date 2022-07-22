package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.CompensationAdapter;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public CompensationDto create(String employeeId, CompensationDto compensationDto) {
        validateEmployee(employeeId);

        Compensation compensation = compensationAdapter.dtoToEntity(employeeId, compensationDto);
        compensationRepository.insert(compensation);
        return compensationDto;
    }

    private void validateEmployee(String employeeId) {
        Employee existingEmployee = employeeRepository.findByEmployeeId(employeeId);
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Override
    public Compensations read(Pageable pagination, String employeeId) {
        validateEmployee(employeeId);
        Page<Compensation> compensationPage = compensationRepository.findAllByEmployeeId(employeeId, pagination);
        List<Compensation> compensationList = compensationPage.toList();

        Compensations compensations = compensationAdapter.listOfEntityToCompensations(employeeId, compensationList);
        compensations.totalCompensations = compensationPage.getTotalElements();
        compensations.totalPages = compensationPage.getTotalPages() -1;

        return compensations;
    }
}
