package com.mindex.challenge.utils;

import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import com.mindex.challenge.util.JsonMapper;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;

public class JsonMappingTests {

    @Test
    @DisplayName("Converting object to Json should return correctly")
    public void asJsonString_givenAnObject_shouldReturnCorrectJson() {
        EmployeeDto employeeDto = new EmployeeDto("1", "Ricardo", "Ianelli", "Software Engineer", "IT");
        employeeDto.directReports.add("2");
        employeeDto.directReports.add("3");

        String expectedJson = "{\"employeeId\":\"1\",\"firstName\":\"Ricardo\",\"lastName\":\"Ianelli\",\"position\":\"Software Engineer\",\"department\":\"IT\",\"directReports\":[\"2\",\"3\"]}";

        String result = JsonMapper.asJsonString(employeeDto);

        assertEquals(expectedJson, result);
    }
}
