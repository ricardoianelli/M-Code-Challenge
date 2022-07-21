package com.mindex.challenge.controller;

import com.mindex.challenge.dto.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/{id}")
    public ResponseEntity<?> generate(@PathVariable String id) {
        LOG.debug("Received employee reporting structure request for id [{}]", id);
        ReportingStructure report = reportingStructureService.generate(id);

        LOG.debug("Responding report structure for employee id [{}]", id);
        return ResponseEntity.ok(report);
    }
}
