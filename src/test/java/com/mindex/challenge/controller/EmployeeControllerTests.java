package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
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
import static org.mockito.Mockito.when;
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

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Smoke test for Spring DI")
    public void contextLoads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("GET /employee/1 - Ok")
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
    @DisplayName("GET /employee/1 - Not Found")
    public void read_givenANotExistentId_ShouldReturn404() throws Exception {
        when(employeeService.read(anyString())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get(BASE_ROUTE + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    @DisplayName("POST /employee - Created")
    public void create_givenAValidInput_ShouldReturnEmployeeAnd201() throws Exception {

        final String employeeId = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.create(any())).thenReturn(exampleEmployee);

        mockMvc.perform(post(BASE_ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(exampleEmployee)))

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
    @DisplayName("PUT /employee/1 - Ok")
    public void update_givenAValidInput_ShouldReturnEmployeeAnd200() throws Exception {

        final String employeeId = "1";
        EmployeeDto exampleEmployee = new EmployeeDto(employeeId, "Ricardo", "Ianelli", "Software Engineer", "IT");

        when(employeeService.update(any())).thenReturn(exampleEmployee);

        mockMvc.perform(put(BASE_ROUTE + "/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(exampleEmployee)))

                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, BASE_ROUTE + "/" + employeeId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.firstName").value(exampleEmployee.firstName))
                .andExpect(jsonPath("$.lastName").value(exampleEmployee.lastName))
                .andExpect(jsonPath("$.position").value(exampleEmployee.position))
                .andExpect(jsonPath("$.department").value(exampleEmployee.department));
    }

    //TODO: Add test case for not existent direct report

    @Test
    @DisplayName("PUT /employee/1 - Not Found")
    public void update_givenANotExistentEmployee_ShouldReturnErrorAnd404() throws Exception {
        when(employeeService.update(any())).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(put(BASE_ROUTE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Employee())))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }
}
