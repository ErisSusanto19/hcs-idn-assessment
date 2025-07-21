package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.request.TaxRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.TaxResponseDTO;
import com.hcs_idn.api_assessment.services.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/taxes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TaxController {

    private final TaxService taxService;

    @PostMapping
    public ResponseEntity<BaseResponse<TaxResponseDTO>> createTax(@RequestBody TaxRequestDTO taxRequestDTO) {
        TaxResponseDTO createdTax = taxService.createTax(taxRequestDTO);
        BaseResponse<TaxResponseDTO> response = BaseResponse.<TaxResponseDTO>builder()
                .message("Tax created successfully.")
                .code(HttpStatus.CREATED.value())
                .data(createdTax)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<TaxResponseDTO>>> getAllTaxes(Pageable pageable) {
        BaseResponse<List<TaxResponseDTO>> response = taxService.getAllTaxes(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaxResponseDTO>> getTaxById(@PathVariable UUID id) {
        TaxResponseDTO tax = taxService.getTaxById(id);
        BaseResponse<TaxResponseDTO> response = BaseResponse.<TaxResponseDTO>builder()
                .message("Tax retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(tax)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<TaxResponseDTO>> updateTax(@PathVariable UUID id, @RequestBody TaxRequestDTO taxRequestDTO) {
        TaxResponseDTO updatedTax = taxService.updateTax(id, taxRequestDTO);
        BaseResponse<TaxResponseDTO> response = BaseResponse.<TaxResponseDTO>builder()
                .message("Tax updated successfully.")
                .code(HttpStatus.OK.value())
                .data(updatedTax)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteTax(@PathVariable UUID id) {
        taxService.deleteTax(id);
        BaseResponse<String> response = BaseResponse.<String>builder()
                .message("Tax deleted successfully.")
                .code(HttpStatus.OK.value())
                .data("Tax with id " + id + " was deleted.")
                .build();
        return ResponseEntity.ok(response);
    }
}
