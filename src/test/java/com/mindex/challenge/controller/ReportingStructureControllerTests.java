package com.mindex.challenge.controller;

import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.dto.ReportingStructure;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ReportingStructureController.class)
@AutoConfigureMockMvc
public class ReportingStructureControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportingStructureService reportingStructureService;

    final String BASE_ROUTE = "/report";

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("GET /report/{id} with a valid id should return employee report structure and Ok")
    public void read_givenAValidId_ShouldReturnReportStructureAnd200() throws Exception {

        final String employeeId = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");
        exampleEmployee.directReports.add("2");
        exampleEmployee.directReports.add("3");

        ReportingStructure reportingStructure = new ReportingStructure(exampleEmployee);

        when(reportingStructureService.generate(employeeId)).thenReturn(reportingStructure);

        mockMvc.perform(get(BASE_ROUTE + "/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employee.employeeId").value(employeeId))
                .andExpect(jsonPath("$.numberOfReports").value(exampleEmployee.directReports.size()));
    }

    @Test
    @DisplayName("GET /report/{id} with an invalid id should return not found")
    public void generate_givenANotExistentId_ShouldReturn404() throws Exception {
        when(reportingStructureService.generate(anyString())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get(BASE_ROUTE + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
