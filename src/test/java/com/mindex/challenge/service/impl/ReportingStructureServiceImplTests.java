package com.mindex.challenge.service.impl;

import com.mindex.challenge.adapter.EmployeeAdapter;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.dto.ReportingStructure;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.utils.EmployeeComparer;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportingStructureServiceImplTests {

    @Autowired
    ReportingStructureService reportingStructureService;

    @MockBean
    EmployeeAdapter employeeAdapter;

    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(reportingStructureService).isNotNull();
    }

    @Test
    @DisplayName("Read employee reporting structure with a valid id returns with success")
    public void read_givenAValidId_ShouldReturnReportingStructureDto() {

        final String employeeId = "1";
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        ReportingStructure expected = new ReportingStructure(employeeDto);

        when(employeeAdapter.entityToDto(any())).thenReturn(employeeDto);
        when(employeeRepository.findByEmployeeId(anyString())).thenReturn(new Employee());

        ReportingStructure fetched = reportingStructureService.read(employeeId);
        EmployeeComparer.compareDtos(fetched.employee, expected.employee);
        assertEquals(fetched.numberOfReports, expected.numberOfReports);

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }

    @Test
    @DisplayName("Read employee with a invalid id throws EmployeeNotFound exception")
    public void read_givenAnInvalidId_ShouldThrowEmployeeNotFoundException() {
        final String employeeId = "1";

        when(employeeRepository.findByEmployeeId(anyString())).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> reportingStructureService.read(employeeId));

        verify(employeeRepository, times(1)).findByEmployeeId(employeeId);
    }
}
