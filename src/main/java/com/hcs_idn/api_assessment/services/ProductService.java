package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.dtos.request.ProductRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.ProductResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    BaseResponse<List<ProductResponseDTO>> getAllProducts(Pageable pageable);

    ProductResponseDTO getProductById(UUID productId);

    ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequestDTO);

    void deleteProduct(UUID productId);
}