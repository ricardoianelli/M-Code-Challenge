package com.mindex.challenge.service;

import com.mindex.challenge.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto create(EmployeeDto employeeDto);
    EmployeeDto read(String id);
    EmployeeDto update(EmployeeDto employeeDto);
}
