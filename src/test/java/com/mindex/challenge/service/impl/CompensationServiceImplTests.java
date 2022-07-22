package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        CompensationDto dto = new CompensationDto(compensation.getSalary(), compensation.getEffectiveDate());

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);
        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(null);
        when(compensationRepository.insert(compensation)).thenReturn(compensation);

        CompensationDto persisted = compensationService.create(employeeId, dto);
        CompensationComparer.compareDtos(persisted, dto);
        verify(compensationRepository, times(1)).insert(compensation);
    }

    @Test(expected = EmployeeNotFoundException.class)
    @DisplayName("Create compensation with invalid employee id throw exception")
    public void create_givenInvalidEmployeeId_ShouldThrowException() {

        final String employeeId = "1";
        CompensationDto dto = new CompensationDto(compensation.getSalary(), compensation.getEffectiveDate());

        when(compensationRepository.findByEmployeeId(employeeId)).thenReturn(null);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(null);

        compensationService.create(employeeId, dto);

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
        verify(compensationRepository, times(1)).findByEmployeeId(employeeId);
        verify(compensationRepository, times(0)).insert(compensation);
    }

    @Test
    @DisplayName("Read compensation with a valid employee id returns compensations from DB")
    public void read_givenAValidEmployeeId_ShouldReturnCompensations() {

        final String employeeId = "1";
        List<Compensation> compensationList = new ArrayList<>();
        compensationList.add(compensation);

        Pageable pagination = PageRequest.of(0, 2);
        Page<Compensation> compensationPage = new PageImpl<>(compensationList);

        when(compensationRepository.findAllByEmployeeId(any(), any())).thenReturn(compensationPage);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);

        Compensations compensations = compensationService.read(pagination, employeeId);
        CompensationComparer.compareCompensationsWithCompensationList(compensations, compensationList);

        verify(compensationRepository, times(1)).findAllByEmployeeId(any(), any());
        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test(expected = EmployeeNotFoundException.class)
    @DisplayName("Read compensations for an invalid employee should throw exception")
    public void read_givenANotPersistedCompensation_ShouldThrowEmployeeNotFoundException() {
        final String employeeId = "1";

        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(null);

        Pageable pagination = PageRequest.of(0, 2);

        compensationService.read(pagination, employeeId);

        verify(compensationRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test
    @DisplayName("Read compensation for an employee without any compensations returns empty Compensations")
    public void read_givenAnEmployeeWithoutCompensations_ShouldReturnEmptyCompensations() {

        final String employeeId = "1";
        List<Compensation> compensationList = new ArrayList<>();

        Pageable pagination = PageRequest.of(0, 2);
        Page<Compensation> compensationPage = new PageImpl<>(compensationList);

        when(compensationRepository.findAllByEmployeeId(any(), any())).thenReturn(compensationPage);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);

        Compensations compensations = compensationService.read(pagination, employeeId);

        assertEquals(compensations.employee, employeeId);
        assertTrue(compensations.compensations.isEmpty());

        verify(compensationRepository, times(1)).findAllByEmployeeId(any(), any());
        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }
}
