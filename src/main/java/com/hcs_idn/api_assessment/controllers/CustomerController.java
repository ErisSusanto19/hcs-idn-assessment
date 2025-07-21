package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.request.CustomerCreateRequestDTO;
import com.hcs_idn.api_assessment.dtos.request.CustomerProfileUpdateDTO;
import com.hcs_idn.api_assessment.dtos.request.CustomerUpdateRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.CustomerResponseDTO;
import com.hcs_idn.api_assessment.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> getCurrentCustomerProfile() {
        CustomerResponseDTO customerProfile = customerService.getCurrentCustomerProfile();
        BaseResponse<CustomerResponseDTO> response = BaseResponse.<CustomerResponseDTO>builder()
                .message("Customer profile retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(customerProfile)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> updateCurrentCustomerProfile(@RequestBody CustomerProfileUpdateDTO profileUpdateDTO) {
        CustomerResponseDTO updatedProfile = customerService.updateCurrentCustomerProfile(profileUpdateDTO);
        BaseResponse<CustomerResponseDTO> response = BaseResponse.<CustomerResponseDTO>builder()
                .message("Customer profile updated successfully.")
                .code(HttpStatus.OK.value())
                .data(updatedProfile)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> createCustomer(@RequestBody CustomerCreateRequestDTO createRequestDTO) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(createRequestDTO);
        BaseResponse<CustomerResponseDTO> response = BaseResponse.<CustomerResponseDTO>builder()
                .message("Customer created successfully.")
                .code(HttpStatus.CREATED.value())
                .data(createdCustomer)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<List<CustomerResponseDTO>>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        BaseResponse<List<CustomerResponseDTO>> response = BaseResponse.<List<CustomerResponseDTO>>builder()
                .message("All customers retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(customers)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> getCustomerById(@PathVariable UUID id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        BaseResponse<CustomerResponseDTO> response = BaseResponse.<CustomerResponseDTO>builder()
                .message("Customer retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<CustomerResponseDTO>> updateCustomer(@PathVariable UUID id, @RequestBody CustomerUpdateRequestDTO updateRequestDTO) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, updateRequestDTO);
        BaseResponse<CustomerResponseDTO> response = BaseResponse.<CustomerResponseDTO>builder()
                .message("Customer updated successfully.")
                .code(HttpStatus.OK.value())
                .data(updatedCustomer)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        BaseResponse<String> response = BaseResponse.<String>builder()
                .message("Customer deleted successfully.")
                .code(HttpStatus.OK.value())
                .data("Customer with id " + id + " was deleted.")
                .build();
        return ResponseEntity.ok(response);
    }
}
