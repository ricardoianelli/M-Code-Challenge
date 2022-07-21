package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
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
    public ResponseEntity<?> create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);
        try {
            Employee createdEmployee = employeeService.create(employee);
            return ResponseEntity
                    .created(new URI("/employee/" + createdEmployee.getEmployeeId()))
                    .body(createdEmployee);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return ResponseEntity.ok(employeeService.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        try {
            employee.setEmployeeId(id);
            Employee updatedEmployee = employeeService.update(employee);
            return ResponseEntity
                    .ok()
                    .location(new URI("/employee/" + updatedEmployee.getEmployeeId()))
                    .body(updatedEmployee);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
