package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
public class EmployeeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    final String BASE_ROUTE = "/employee";

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("GET /employee/1 with a valid id should return employee and Ok")
    public void read_givenAValidId_ShouldReturnEmployeeAnd200() throws Exception {

        final String employeeId = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.read(employeeId)).thenReturn(exampleEmployee);

        mockMvc.perform(get(BASE_ROUTE + "/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(employeeId));
    }

    @Test
    @DisplayName("GET /employee/1 with a not existent employee should return Not Found")
    public void read_givenANotExistentId_ShouldReturn404() throws Exception {
        when(employeeService.read(anyString())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get(BASE_ROUTE + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /employee with valid employee should return Created")
    public void create_givenAValidInput_ShouldReturnEmployeeAnd201() throws Exception {

        final String employeeId = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(null, "Ricardo", "Ianelli", "Software Engineer", "IT");
        EmployeeDto expectedResponse = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.create(any())).thenReturn(expectedResponse);

        mockMvc.perform(post(BASE_ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, BASE_ROUTE + "/" + employeeId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.firstName").value(exampleEmployee.firstName))
                .andExpect(jsonPath("$.lastName").value(exampleEmployee.lastName))
                .andExpect(jsonPath("$.position").value(exampleEmployee.position))
                .andExpect(jsonPath("$.department").value(exampleEmployee.department));
    }

    @Test
    @DisplayName("POST and PUT /employee with employee first name too short should return Bad Request")
    public void createAndUpdate_givenEmployeeFirstNameTooShort_ShouldReturn400() throws Exception {

        EmployeeDto exampleEmployee = new EmployeeDto(null, "R", "Ianelli", "Software Engineer", "IT");

        mockMvc.perform(post(BASE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());

        mockMvc.perform(put(BASE_ROUTE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST and PUT /employee with employee last name too short should return Bad Request")
    public void createAndUpdate_givenEmployeeLastNameTooShort_ShouldReturn400() throws Exception {

        EmployeeDto exampleEmployee = new EmployeeDto(null, "Ricardo", "I", "Software Engineer", "IT");

        mockMvc.perform(post(BASE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());

        mockMvc.perform(put(BASE_ROUTE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST and PUT /employee with employee position too short should return Bad Request")
    public void createAndUpdate_givenEmployeePositionTooShort_ShouldReturn400() throws Exception {

        EmployeeDto exampleEmployee = new EmployeeDto(null, "Ricardo", "Ianelli", "E", "IT");

        mockMvc.perform(post(BASE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());

        mockMvc.perform(put(BASE_ROUTE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST and PUT /employee with employee department too short should return Bad Request")
    public void createAndUpdate_givenEmployeeDepartmentTooShort_ShouldReturn400() throws Exception {

        EmployeeDto exampleEmployee = new EmployeeDto(null, "Ricardo", "Ianelli", "Engineer", "I");

        mockMvc.perform(post(BASE_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());

        mockMvc.perform(put(BASE_ROUTE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /employee/1 with valid employee should return employee and Ok")
    public void update_givenAValidInput_ShouldReturnEmployeeAnd200() throws Exception {

        final String employeeId = "1";
        EmployeeDto employeeRequest = new EmployeeDto(null, "Ricardo", "Ianelli", "Software Engineer", "IT");
        EmployeeDto expectedResponse = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.update(any(EmployeeDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(put(BASE_ROUTE + "/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(employeeRequest)))

                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, BASE_ROUTE + "/" + employeeId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.firstName").value(employeeRequest.firstName))
                .andExpect(jsonPath("$.lastName").value(employeeRequest.lastName))
                .andExpect(jsonPath("$.position").value(employeeRequest.position))
                .andExpect(jsonPath("$.department").value(employeeRequest.department));

        verify(employeeService, times(1)).update(any(EmployeeDto.class));
    }

    @Test
    @DisplayName("PUT /employee/{id} with invalid employee should return Not Found")
    public void update_givenANotExistentEmployee_ShouldReturnErrorAnd404() throws Exception {
        final String id = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(null, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.update(any(EmployeeDto.class))).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(put(BASE_ROUTE + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonMapper.asJsonString(exampleEmployee)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(employeeService, times(1)).update(any(EmployeeDto.class));
    }

    @Test
    @DisplayName("PUT /employee/1 with invalid direct reports should return Bad Request")
    public void update_givenInvalidDirectReports_ShouldReturnErrorAnd400() throws Exception {
        final String id = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(id, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.update(any())).thenThrow(DirectReportEmployeeNotFoundException.class);

        mockMvc.perform(put(BASE_ROUTE + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.asJsonString(exampleEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(employeeService, times(1)).update(any(EmployeeDto.class));
    }
}
