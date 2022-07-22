package com.mindex.challenge.controller;

import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;
import com.mindex.challenge.service.CompensationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Tag(name = "Compensation")
@RequestMapping("/employee/{id}/compensation")
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping
    @Operation(summary = "Add new employee compensation", responses = {
            @ApiResponse(description = "Compensation included Successfully", responseCode = "201",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompensationDto.class))),
            @ApiResponse(description = "Employee not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(description = "Invalid input", responseCode = "400",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    public ResponseEntity<?> create(@PathVariable String id, @Valid @RequestBody CompensationDto compensationDto) {
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
    @Operation(summary = "Get employee compensations", responses = {
            @ApiResponse(description = "Returned compensations successfully", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompensationDto.class))),
            @ApiResponse(description = "Employee not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    public ResponseEntity<?> read(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        LOG.debug("Received compensation read request for employee id {}", id);
        Pageable pagination = PageRequest.of(page, size);
        Compensations compensations = compensationService.read(pagination, id);

        HttpHeaders responseHeaders = new HttpHeaders();
        addPaginationInfoToHeaders(responseHeaders, page, compensations.totalPages, compensations.totalCompensations);

        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(compensations);
    }

    private void addPaginationInfoToHeaders(HttpHeaders httpHeaders, int page, int totalPages, long totalElements) {
        httpHeaders.set("firstPage", "0");
        httpHeaders.set("prevPage", "" + Math.max(0, (Math.min(totalPages, (page - 1)))));
        httpHeaders.set("nextPage", "" +  Math.max(0, (Math.min(totalPages, (page + 1)))));
        httpHeaders.set("lastPage", "" + totalPages);
        httpHeaders.set("totalElements", "" + totalElements);
    }
}
