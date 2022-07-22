package com.mindex.challenge.controller;

import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/employee/{id}/compensation")
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping
    public ResponseEntity<?> create(@PathVariable String id, @RequestBody CompensationDto compensationDto) {
        LOG.debug("Received compensation create request for employee id {}", id);
        try {
            CompensationDto createdCompensation = compensationService.create(id, compensationDto);
            return ResponseEntity
                    .created(new URI("/employee/" + id + "/compensation"))
                    .body(createdCompensation);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> read(@PathVariable String id) {
        LOG.debug("Received compensation read request for employee id {}", id);
        Compensations compensations = compensationService.read(id);
        return ResponseEntity.ok(compensations);
    }
}
