package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
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

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee id [{}]", employee.getEmployeeId());

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        LOG.debug("Employee with id [{}] was created successfully", employee);
        return employee;
    }

    @Override
    public Employee read(String id) {

        LOG.debug("Reading employee with id [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Could not find employee with id " + id);
        }

        LOG.debug("Returning employee with id [{}]", employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee id [{}]", employee.getEmployeeId());

        Employee existingEmployee = employeeRepository.findByEmployeeId(employee.getEmployeeId());

        if (existingEmployee == null) {
            LOG.debug("Employee not found during update. Inserting a new one with id [{}]", employee.getEmployeeId());
        }

        employeeRepository.save(employee);
        LOG.debug("Employee with id [{}] updated or inserted successfully", employee.getEmployeeId());
        return employee;
    }
}
