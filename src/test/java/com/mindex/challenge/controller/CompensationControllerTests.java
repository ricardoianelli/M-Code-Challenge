package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.util.JsonMapper;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CompensationController.class)
@AutoConfigureMockMvc
public class CompensationControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CompensationService compensationService;

    final String BASE_ROUTE = "/employee/";

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("GET /employee/1/compensation with a valid id should return compensations and Ok")
    public void read_givenAValidId_ShouldReturnCompensationsAnd200() throws Exception {

        final String employeeId = "1";
        Compensations compensations = new Compensations(employeeId);
        when(compensationService.read(employeeId)).thenReturn(compensations);

        mockMvc.perform(get(BASE_ROUTE + employeeId + "/compensation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employee").value(employeeId));
    }

    @Test
    @DisplayName("GET /employee/1/compensation with a not existent employee should return Not Found")
    public void read_givenANotExistentId_ShouldReturn404() throws Exception {
        when(compensationService.read(anyString())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get(BASE_ROUTE + "1/compensation"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /employee/{id}/compensation with not existent employee id should return Not Found")
    public void create_givenANotExistentEmployee_ShouldReturn404() throws Exception {

        final String employeeId = "1";
        CompensationDto compensationDto = new CompensationDto(new BigDecimal("24.9"), LocalDate.parse("2021-08-01"));
        when(compensationService.create(any(), any())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(post(BASE_ROUTE + employeeId + "/compensation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonMapper.asJsonString(compensationDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /employee/{id}/compensation with invalid salary returns BadRequest")
    public void create_givenAnInvalidSalary_ShouldReturn400() throws Exception {

        final String employeeId = "1";
        CompensationDto compensationDto = new CompensationDto(new BigDecimal("-10"), LocalDate.parse("2021-08-01"));

        mockMvc.perform(post(BASE_ROUTE + employeeId + "/compensation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(compensationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /employee/{id}/compensation with null salary returns BadRequest")
    public void create_givenNullSalary_ShouldReturn400() throws Exception {

        final String employeeId = "1";
        CompensationDto compensationDto = new CompensationDto(null, LocalDate.now().minusDays(1));

        mockMvc.perform(post(BASE_ROUTE + employeeId + "/compensation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(compensationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /employee/{id}/compensation with effectiveDate in the future returns BadRequest")
    public void create_givenAnEffectiveDateInTheFuture_ShouldReturn400() throws Exception {

        final String employeeId = "1";
        CompensationDto compensationDto = new CompensationDto(new BigDecimal("24.90"), LocalDate.now().plusYears(1));

        mockMvc.perform(post(BASE_ROUTE + employeeId + "/compensation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(compensationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /employee/{id}/compensation with null effectiveDate returns BadRequest")
    public void create_givenNullEffectiveDate_ShouldReturn400() throws Exception {

        final String employeeId = "1";
        CompensationDto compensationDto = new CompensationDto(new BigDecimal("24.90"), null);

        mockMvc.perform(post(BASE_ROUTE + employeeId + "/compensation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(compensationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /employee/{id}/compensation with valid employee should return Created")
    public void create_givenAValidInput_ShouldReturnCompensationDtoAnd201() throws Exception {

        final String employeeId = "1";
        CompensationDto compensationDto = new CompensationDto(new BigDecimal("24.9"), LocalDate.parse("2021-08-01"));

        when(compensationService.create(any(), any())).thenReturn(compensationDto);

        mockMvc.perform(post(BASE_ROUTE + employeeId + "/compensation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(compensationDto)))

                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, BASE_ROUTE + employeeId + "/compensation"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.salary").value(compensationDto.salary.toString()))
                .andExpect(jsonPath("$.effectiveDate").value(compensationDto.effectiveDate.toString()));
    }
}
