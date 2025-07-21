package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.dtos.request.CustomerCreateRequestDTO;
import com.hcs_idn.api_assessment.dtos.request.CustomerProfileUpdateDTO;
import com.hcs_idn.api_assessment.dtos.request.CustomerUpdateRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.CustomerResponseDTO;
import com.hcs_idn.api_assessment.dtos.response.UserSummaryDTO;
import com.hcs_idn.api_assessment.entities.Account;
import com.hcs_idn.api_assessment.entities.Customer;
import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.entities.User;
import com.hcs_idn.api_assessment.enums.UserRole;
import com.hcs_idn.api_assessment.exceptions.customs.BadRequest;
import com.hcs_idn.api_assessment.exceptions.customs.Forbidden;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.repositories.AccountRepository;
import com.hcs_idn.api_assessment.repositories.CustomerRepository;
import com.hcs_idn.api_assessment.repositories.RoleRepository;
import com.hcs_idn.api_assessment.repositories.UserRepository;
import com.hcs_idn.api_assessment.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerCreateRequestDTO createRequestDTO) {

        if (accountRepository.findByUsername(createRequestDTO.getUsername()).isPresent() ||
                accountRepository.findByEmail(createRequestDTO.getEmail()).isPresent()) {
            throw new BadRequest("Username or Email already exists!");
        }

        Role customerRole = roleRepository.findByName(UserRole.CUSTOMER)
                .orElseThrow(() -> new NotFound("FATAL: CUSTOMER Role not found in database."));

        Account account = new Account();
        account.setUsername(createRequestDTO.getUsername());
        account.setEmail(createRequestDTO.getEmail());
        account.setPassword(passwordEncoder.encode(createRequestDTO.getPassword()));
        account.setRoles(Collections.singleton(customerRole));

        User loggedInStaff = getCurrentLoggedInStaff();

        Customer customer = new Customer();
        customer.setName(createRequestDTO.getName());
        customer.setBirthdate(createRequestDTO.getBirthdate());
        customer.setBirthplace(createRequestDTO.getBirthplace());
        customer.setAccount(account);
        customer.setCreatedBy(loggedInStaff);
        customer.setUpdatedBy(loggedInStaff);

        Customer savedCustomer = customerRepository.save(customer);

        return mapToCustomerResponseDTO(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFound("Customer not found with id: " + customerId));
        return mapToCustomerResponseDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(UUID customerId, CustomerUpdateRequestDTO updateRequestDTO) {
        Customer customerToUpdate = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFound("Customer not found with id: " + customerId));

        User loggedInStaff = getCurrentLoggedInStaff();

        customerToUpdate.setName(updateRequestDTO.getName());
        customerToUpdate.setBirthdate(updateRequestDTO.getBirthdate());
        customerToUpdate.setBirthplace(updateRequestDTO.getBirthplace());
        customerToUpdate.setUpdatedBy(loggedInStaff);

        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        return mapToCustomerResponseDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFound("Customer not found with id: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCurrentCustomerProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Logged in user not found in database."));

        if (account.getCustomer() == null) {
            throw new Forbidden("This endpoint is for customers only.");
        }
        return mapToCustomerResponseDTO(account.getCustomer());
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCurrentCustomerProfile(CustomerProfileUpdateDTO profileUpdateDTO) {
        Customer customerToUpdate = getCurrentCustomerProfileEntity();

        customerToUpdate.setName(profileUpdateDTO.getName());
        customerToUpdate.setBirthdate(profileUpdateDTO.getBirthdate());
        customerToUpdate.setBirthplace(profileUpdateDTO.getBirthplace());

        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        return mapToCustomerResponseDTO(updatedCustomer);
    }

    // --- Helper Methods ---

    private User getCurrentLoggedInStaff() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Logged in user not found."))
                .getUser();
    }

    private Customer getCurrentCustomerProfileEntity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Logged in user not found."))
                .getCustomer();
    }

    private CustomerResponseDTO mapToCustomerResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setBirthdate(customer.getBirthdate());
        dto.setBirthplace(customer.getBirthplace());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        if (customer.getAccount() != null) {
            dto.setUsername(customer.getAccount().getUsername());
            dto.setEmail(customer.getAccount().getEmail());
        }

        if (customer.getCreatedBy() != null) {
            dto.setCreatedBy(mapUserToSummaryDTO(customer.getCreatedBy()));
        }
        if (customer.getUpdatedBy() != null) {
            dto.setUpdatedBy(mapUserToSummaryDTO(customer.getUpdatedBy()));
        }

        return dto;
    }

    private UserSummaryDTO mapUserToSummaryDTO(User user) {
        UserSummaryDTO summary = new UserSummaryDTO();
        summary.setId(user.getId());
        summary.setFullName(user.getFullName());
        return summary;
    }
}
