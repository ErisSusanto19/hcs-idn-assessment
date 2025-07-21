package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.dtos.request.CustomerCreateRequestDTO;
import com.hcs_idn.api_assessment.dtos.request.CustomerProfileUpdateDTO;
import com.hcs_idn.api_assessment.dtos.request.CustomerUpdateRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.CustomerResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerCreateRequestDTO createRequestDTO);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(UUID customerId);

    CustomerResponseDTO updateCustomer(UUID customerId, CustomerUpdateRequestDTO updateRequestDTO);

    void deleteCustomer(UUID customerId);

    CustomerResponseDTO getCurrentCustomerProfile();

    CustomerResponseDTO updateCurrentCustomerProfile(CustomerProfileUpdateDTO profileUpdateDTO);
}
