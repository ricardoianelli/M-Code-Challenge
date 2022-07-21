package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceImplTests {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeAdapter employeeAdapter;

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(employeeService).isNotNull();
    }

    @Test
    @DisplayName("Create employee returns a new employee")
    public void create_givenAValidEmployeeDto_ShouldPersistAndReturnEmployeeDto() {

        final String employeeId = "1";
        Employee employee = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        EmployeeDto dto = employeeAdapter.entityToDto(employee);

        when(employeeRepository.insert(employee)).thenReturn(employee);

        EmployeeDto persisted = employeeService.create(dto);
        assertThat(persisted.equals(dto)).isTrue();

        verify(employeeRepository, times(1)).insert(employee);
    }

    @Test
    @DisplayName("Read employee with a valid id returns an employee from DB")
    public void read_givenAValidId_ShouldReturnEmployeeDto() {

        final String employeeId = "1";
        Employee employee = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        EmployeeDto dto = employeeAdapter.entityToDto(employee);

        when(employeeRepository.findByEmployeeId(anyString())).thenReturn(employee);

        EmployeeDto fetched = employeeService.read(employeeId);
        assertThat(fetched.equals(dto)).isTrue();

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test
    @DisplayName("Read employee with a invalid id throws EmployeeNotFound exception")
    public void read_givenAnInvalidId_ShouldThrowEmployeeNotFoundException() {
        final String employeeId = "1";

        when(employeeRepository.findByEmployeeId(anyString())).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.read(employeeId));

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test
    @DisplayName("Update or create employee returns updated employee")
    public void update_givenAnEmployeeDto_ShouldUpdateOrInsertNewOne() {

        final String employeeId = "1";
        Employee oldVersion = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        Employee updated = new Employee(employeeId, "Ricardo", "Ianelli", "Tech Lead", "IT");
        EmployeeDto dto = employeeAdapter.entityToDto(updated);

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(oldVersion);
        when(employeeRepository.save(updated)).thenReturn(updated);

        EmployeeDto returnedEmployee = employeeService.update(dto);
        assertThat(returnedEmployee.equals(dto)).isTrue();

        verify(employeeRepository, times(1)).save(updated);
    }

    //TODO: Add test case for not existent direct report
}
