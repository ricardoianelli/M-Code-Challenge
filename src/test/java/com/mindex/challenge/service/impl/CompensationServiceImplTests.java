package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.exceptions.CompensationNotFoundException;
import com.mindex.challenge.exceptions.DuplicateCompensationException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.utils.CompensationComparer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompensationServiceImplTests {

    @Autowired
    CompensationService compensationService;

    @Autowired
    EmployeeAdapter employeeAdapter;

    @MockBean
    CompensationRepository compensationRepository;

    @MockBean
    EmployeeRepository employeeRepository;

    static Employee employee;
    static Compensation compensation;

    @BeforeClass
    public static void setupObjects() {
        final String employeeId = "1";
        employee = new Employee(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        compensation = new Compensation(employeeId, new BigDecimal("10000"), LocalDate.parse("2022-08-01"));
    }

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(compensationService).isNotNull();
    }

    @Test
    @DisplayName("Create compensation returns a new compensation")
    public void create_givenAValidEmployeeId_ShouldPersistAndReturnCompensationDto() {
        final String employeeId = "1";
        CompensationDto dto = new CompensationDto(employeeId, compensation.getSalary().toString(), compensation.getEffectiveDate().toString());

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);
        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(null);
        when(compensationRepository.insert(compensation)).thenReturn(compensation);

        CompensationDto persisted = compensationService.create(dto);
        CompensationComparer.compareDtos(persisted, dto);
        verify(compensationRepository, times(1)).insert(compensation);
    }

    @Test(expected = EmployeeNotFoundException.class)
    @DisplayName("Create compensation with invalid employee id throw exception")
    public void create_givenInvalidEmployeeId_ShouldThrowException() {

        final String employeeId = "1";
        CompensationDto dto = new CompensationDto(employeeId, compensation.getSalary().toString(), compensation.getEffectiveDate().toString());

        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(null);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(null);

        compensationService.create(dto);

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
        verify(compensationRepository, times(1)).findByEmployeeId(employeeId);
        verify(compensationRepository, times(0)).insert(compensation);
    }

    @Test(expected = DuplicateCompensationException.class)
    @DisplayName("Create duplicate compensation should throw exception")
    public void create_givenAlreadyExistingCompensation_ShouldThrowException() {

        final String employeeId = "1";
        CompensationDto dto = new CompensationDto(employeeId, compensation.getSalary().toString(), compensation.getEffectiveDate().toString());

        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(compensation);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(null);

        compensationService.create(dto);

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
        verify(compensationRepository, times(1)).findByEmployeeId(employeeId);
        verify(compensationRepository, times(0)).insert(compensation);
    }

    @Test
    @DisplayName("Read compensation with a valid employee id returns a compensation from DB")
    public void read_givenAValidEmployeeId_ShouldReturnCompensationDto() {

        final String employeeId = "1";
        CompensationDto dto = new CompensationDto(employeeId, compensation.getSalary().toString(), compensation.getEffectiveDate().toString());

        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(compensation);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);

        CompensationDto persistedCompensation = compensationService.read(employeeId);
        CompensationComparer.compareDtos(persistedCompensation, dto);

        verify(compensationRepository, times(1)).findByEmployeeId(employeeId);
        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test(expected = CompensationNotFoundException.class)
    @DisplayName("Read compensation that doesn't exist throws CompensationNotFound exception")
    public void read_givenANotPersistedCompensation_ShouldThrowCompensationNotFoundException() {
        final String employeeId = "1";

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);
        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(null);

        compensationService.read(employeeId);

        verify(compensationRepository, times(1)).findByEmployeeId(employeeId);
    }
}
