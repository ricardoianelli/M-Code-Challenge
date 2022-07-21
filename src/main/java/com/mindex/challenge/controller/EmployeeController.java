package com.mindex.challenge.controller;

import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EmployeeDto employee) {
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
    public ResponseEntity<?> read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);
        EmployeeDto existingEmployee = employeeService.read(id);

        return ResponseEntity.ok(existingEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody EmployeeDto employee) {
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
