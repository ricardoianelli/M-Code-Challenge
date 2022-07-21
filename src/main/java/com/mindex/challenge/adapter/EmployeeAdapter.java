package com.mindex.challenge.adapter;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;

public interface EmployeeAdapter {
    Employee dtoToEntity(EmployeeDto dto);
    EmployeeDto entityToDto(Employee employee);
}
