package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.dtos.request.TaxRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.TaxResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaxService {

    TaxResponseDTO createTax(TaxRequestDTO taxRequestDTO);

    BaseResponse<List<TaxResponseDTO>> getAllTaxes(Pageable pageable);

    TaxResponseDTO getTaxById(UUID taxId);

    TaxResponseDTO updateTax(UUID taxId, TaxRequestDTO taxRequestDTO);

    void deleteTax(UUID taxId);
}