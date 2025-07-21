package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.dtos.request.TaxRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.PaginationResponse;
import com.hcs_idn.api_assessment.dtos.response.TaxResponseDTO;
import com.hcs_idn.api_assessment.entities.Tax;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.repositories.TaxRepository;
import com.hcs_idn.api_assessment.services.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Override
    @Transactional
    public TaxResponseDTO createTax(TaxRequestDTO taxRequestDTO) {
        // Optional: Tambahkan validasi untuk mencegah nama pajak duplikat
        if (taxRepository.findByName(taxRequestDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Tax with name '" + taxRequestDTO.getName() + "' already exists.");
        }

        Tax tax = new Tax();
        tax.setName(taxRequestDTO.getName());
        tax.setRate(taxRequestDTO.getRate());

        Tax savedTax = taxRepository.save(tax);
        return mapToResponseDTO(savedTax);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<TaxResponseDTO>> getAllTaxes(Pageable pageable) {
        Page<Tax> taxPage = taxRepository.findAll(pageable);
        List<TaxResponseDTO> taxDTOs = taxPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        PaginationResponse pagination = PaginationResponse.builder()
                .currentPage(taxPage.getNumber())
                .pageSize(taxPage.getSize())
                .totalPages(taxPage.getTotalPages())
                .totalElements((int) taxPage.getTotalElements())
                .build();

        return BaseResponse.<List<TaxResponseDTO>>builder()
                .message("Successfully retrieved all taxes.")
                .code(HttpStatus.OK.value())
                .data(taxDTOs)
                .pagination(pagination)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TaxResponseDTO getTaxById(UUID taxId) {
        Tax tax = taxRepository.findById(taxId)
                .orElseThrow(() -> new NotFound("Tax not found with id: " + taxId));
        return mapToResponseDTO(tax);
    }

    @Override
    @Transactional
    public TaxResponseDTO updateTax(UUID taxId, TaxRequestDTO taxRequestDTO) {
        Tax taxToUpdate = taxRepository.findById(taxId)
                .orElseThrow(() -> new NotFound("Tax not found with id: " + taxId));

        taxToUpdate.setName(taxRequestDTO.getName());
        taxToUpdate.setRate(taxRequestDTO.getRate());

        Tax updatedTax = taxRepository.save(taxToUpdate);
        return mapToResponseDTO(updatedTax);
    }

    @Override
    @Transactional
    public void deleteTax(UUID taxId) {
        if (!taxRepository.existsById(taxId)) {
            throw new NotFound("Tax not found with id: " + taxId);
        }
        taxRepository.deleteById(taxId);
    }

    private TaxResponseDTO mapToResponseDTO(Tax tax) {
        TaxResponseDTO dto = new TaxResponseDTO();
        dto.setId(tax.getId());
        dto.setName(tax.getName());
        dto.setRate(tax.getRate());
        return dto;
    }
}
