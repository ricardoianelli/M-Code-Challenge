package com.mindex.challenge.adapter;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(SpringRunner.class)
public class EmployeeAdapterImplTests {

    @Autowired
    EmployeeAdapter employeeAdapter;

    @MockBean
    EmployeeRepository employeeRepository;

    static EmployeeDto dto;
    static Employee entity;
    static Employee directReport1;
    static Employee directReport2;

    @BeforeClass
    public static void prepareObjects() {
        dto = new EmployeeDto("123", "Ricardo", "Ianelli", "Software Engineer", "IT");
        dto.directReports = new HashSet<>(Arrays.asList("1", "2"));

        directReport1 = new Employee("1", "Bruce", "Wayne", "Security Analyst", "IT");
        directReport2 = new Employee("2", "Agatha", "Christie", "Sales Lead", "Sales");
        entity = new Employee("123", "Ricardo", "Ianelli", "Software Engineer", "IT");

        entity.addDirectReport(directReport1);
        entity.addDirectReport(directReport2);
    }

    @Test
    @DisplayName("Convert Employee DTO to Entity")
    public void dtoToEntity_givenAValidDto_ReturnsMatchingEntity() {
        when(employeeRepository.findByEmployeeId(dto.employeeId)).thenReturn(entity);
        when(employeeRepository.findByEmployeeId(directReport1.getEmployeeId())).thenReturn(directReport1);
        when(employeeRepository.findByEmployeeId(directReport2.getEmployeeId())).thenReturn(directReport2);

        Employee resultEntity = employeeAdapter.dtoToEntity(dto);

        compareEntityAndDto(resultEntity, dto);
    }

    @Test(expected = DirectReportEmployeeNotFoundException.class)
    @DisplayName("Direct report not existent when converting Employee DTO to Entity")
    public void dtoToEntity_givenAnInvalidDirectReport_ThrowsException() {
        String notExistentEmployeeId = "2";

        when(employeeRepository.findByEmployeeId(dto.employeeId)).thenReturn(entity);
        when(employeeRepository.findByEmployeeId(directReport1.getEmployeeId())).thenReturn(directReport1);
        when(employeeRepository.findByEmployeeId(notExistentEmployeeId)).thenReturn(null);

        employeeAdapter.dtoToEntity(dto);
    }

    @Test
    @DisplayName("Convert Employee to DTO")
    public void entityToDto_givenAValidEntity_ReturnsMatchingDto() {
        EmployeeDto resultDto = employeeAdapter.entityToDto(entity);
        compareEntityAndDto(entity, resultDto);
    }

    private void compareEntityAndDto(Employee employee, EmployeeDto dto) {
        assertAll("Should match employee entity and DTO",
                () -> assertEquals("Id match", employee.getEmployeeId(), dto.employeeId),
                () -> assertEquals("First name match",employee.getFirstName(), dto.firstName),
                () -> assertEquals("Last name match",employee.getLastName(), dto.lastName),
                () -> assertEquals("Position match",employee.getPosition(), dto.position),
                () -> assertEquals("Department match",employee.getDepartment(), dto.department),
                () -> assertTrue("Direct reports match",compareDirectReportsFromEntityAndDto(employee, dto))
        );
    }

    private boolean compareDirectReportsFromEntityAndDto(Employee employee, EmployeeDto dto) {
        if (employee.getDirectReports().size() != dto.directReports.size()) {
            return false;
        }

        for (Employee directReport : employee.getDirectReports()) {
            if (!dto.directReports.contains(directReport.getEmployeeId())) {
                return false;
            }
        }

        return true;
    }

}
