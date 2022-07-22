package com.mindex.challenge.controller;

import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Tag(name="Employee")
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create new employee", responses = {
            @ApiResponse(description = "Created Successfully", responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class)))
    })
    public ResponseEntity<?> create(@Valid @RequestBody EmployeeDto employee) {
        LOG.debug("Received employee create request for [{}]", employee);
        try {
            EmployeeDto createdEmployee = employeeService.create(employee);
            return ResponseEntity
                    .created(new URI("/employee/" + createdEmployee.employeeId))
                    .body(createdEmployee);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Read employee information", responses = {
            @ApiResponse(description = "Returned successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(description = "Employee not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    public ResponseEntity<?> read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);
        EmployeeDto existingEmployee = employeeService.read(id);

        return ResponseEntity.ok(existingEmployee);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee information", responses = {
            @ApiResponse(description = "Updated successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(description = "Invalid direct reports", responseCode = "400",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(description = "Invalid input", responseCode = "400",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody EmployeeDto employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        try {
            employee.employeeId = id;
            EmployeeDto updatedEmployee = employeeService.update(employee);
            return ResponseEntity
                    .ok()
                    .location(new URI("/employee/" + updatedEmployee.employeeId))
                    .body(updatedEmployee);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
