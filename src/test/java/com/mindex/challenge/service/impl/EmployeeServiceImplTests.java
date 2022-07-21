package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
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

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(employeeService).isNotNull();
    }

    @Test
    @DisplayName("Create employee returns a new employee")
    public void create_givenAValidEmployee_ShouldPersistAndReturnEmployee() {

        final String employeeId = "1";
        Employee employee = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeRepository.insert(employee)).thenReturn(employee);

        Employee persisted = employeeService.create(employee);
        assertThat(persisted.equals(employee)).isTrue();

        verify(employeeRepository, times(1)).insert(employee);
    }

    @Test
    @DisplayName("Read employee with a valid id returns an employee from DB")
    public void read_givenAValidId_ShouldReturnEmployee() {

        final String employeeId = "1";
        Employee employee = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeRepository.findByEmployeeId(anyString())).thenReturn(employee);

        Employee fetched = employeeService.read(employeeId);
        assertThat(fetched.equals(employee)).isTrue();

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
    public void update_givenAnEmployee_ShouldUpdateOrInsertNewOne() {

        final String employeeId = "1";
        Employee oldVersion = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        Employee updated = new Employee(employeeId, "Ricardo", "Ianelli", "Tech Lead", "IT");

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(oldVersion);
        when(employeeRepository.save(updated)).thenReturn(updated);

        Employee returnedEmployee = employeeService.update(updated);
        assertThat(returnedEmployee.equals(updated)).isTrue();

        verify(employeeRepository, times(1)).save(updated);
    }
}
