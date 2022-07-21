package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeAdapter employeeAdapter;

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        LOG.debug("Creating new employee");

        employeeDto.employeeId = UUID.randomUUID().toString();
        Employee employee = employeeAdapter.dtoToEntity(employeeDto);
        employeeRepository.insert(employee);

        LOG.debug("Employee with id [{}] was created successfully", employeeDto.employeeId);
        return employeeDto;
    }

    @Override
    public EmployeeDto read(String id) {

        LOG.debug("Reading employee with id [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Could not find employee with id " + id);
        }

        EmployeeDto dto = employeeAdapter.entityToDto(employee);

        LOG.debug("Returning employee with id [{}]", employee);
        return dto;
    }

    @Override
    public EmployeeDto update(EmployeeDto dto) {
        LOG.debug("Updating employee id [{}]", dto.employeeId);

        Employee employee = employeeRepository.findByEmployeeId(dto.employeeId);

        if (employee == null) {
            LOG.debug("Employee not found during update. Inserting a new one with id [{}]", dto.employeeId);
        }

        employee = employeeAdapter.dtoToEntity(dto);

        employeeRepository.save(employee);
        LOG.debug("Employee with id [{}] updated or inserted successfully", dto.employeeId);
        return dto;
    }
}
