package com.mindex.challenge.controller;

import com.mindex.challenge.dto.EmployeeDto;
import com.mindex.challenge.dto.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Reporting Structure")
@RequestMapping("/report")
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/{id}")
    @Operation(summary = "Generate employee reporting structure", responses = {
            @ApiResponse(description = "Generated Successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportingStructure.class))),
            @ApiResponse(description = "Employee not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    public ResponseEntity<?> generate(@PathVariable String id) {
        LOG.debug("Received employee reporting structure request for id [{}]", id);
        ReportingStructure report = reportingStructureService.generate(id);

        LOG.debug("Responding report structure for employee id [{}]", id);
        return ResponseEntity.ok(report);
    }
}
