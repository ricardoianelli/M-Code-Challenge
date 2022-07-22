package com.mindex.challenge.adapter.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmployeeAdapterImpl implements EmployeeAdapter {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee dtoToEntity(EmployeeDto dto) {
        Employee employee = new Employee(
                dto.employeeId,
                dto.firstName,
                dto.lastName,
                dto.position,
                dto.department);

        Set<Employee> directReports = new HashSet<>();
        Employee directReport;

        for (String employeeId : dto.directReports) {
            directReport = employeeRepository.findByEmployeeId(employeeId);
            if (directReport == null) {
                throw new DirectReportEmployeeNotFoundException(employeeId);
            }

            directReports.add(directReport);
        }

        employee.setDirectReports(directReports);

        return employee;
    }

    @Override
    public EmployeeDto entityToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPosition(),
                employee.getDepartment());

        dto.directReports = employee.getDirectReports()
                .stream()
                .map(Employee::getEmployeeId)
                .collect(Collectors.toSet());

        return dto;
    }
}
