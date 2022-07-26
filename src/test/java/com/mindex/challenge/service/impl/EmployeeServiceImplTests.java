package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.utils.EmployeeComparer;
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
        EmployeeComparer.compareDtos(persisted, dto);
        employee.setEmployeeId(dto.employeeId);
        verify(employeeRepository, times(1)).insert(employee);
    }

    @Test(expected = DirectReportEmployeeNotFoundException.class)
    @DisplayName("Create employee with invalid direct reports throw exception")
    public void create_givenInvalidDirectReports_ShouldThrowException() {

        final String employeeId = "1";
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        employeeDto.directReports.add("1");

        employeeService.create(employeeDto);
    }

    @Test
    @DisplayName("Read employee with a valid id returns an employee from DB")
    public void read_givenAValidId_ShouldReturnEmployeeDto() {

        final String employeeId = "1";
        Employee employee = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        EmployeeDto dto = employeeAdapter.entityToDto(employee);

        when(employeeRepository.findByEmployeeId(anyString())).thenReturn(employee);

        EmployeeDto fetched = employeeService.read(employeeId);
        EmployeeComparer.compareDtos(fetched, dto);
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
        EmployeeComparer.compareDtos(returnedEmployee, dto);

        verify(employeeRepository, times(1)).save(updated);
    }

    @Test(expected = DirectReportEmployeeNotFoundException.class)
    @DisplayName("Update employee with invalid direct reports throw exception")
    public void update_givenInvalidDirectReports_ShouldThrowException() {

        final String employeeId = "1";
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        employeeDto.directReports.add("1");

        employeeService.update(employeeDto);
    }
}
