package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.dtos.request.ProductRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.PaginationResponse;
import com.hcs_idn.api_assessment.dtos.response.ProductResponseDTO;
import com.hcs_idn.api_assessment.dtos.response.TaxResponseDTO;
import com.hcs_idn.api_assessment.entities.Product;
import com.hcs_idn.api_assessment.entities.Tax;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.repositories.ProductRepository;
import com.hcs_idn.api_assessment.repositories.TaxRepository;
import com.hcs_idn.api_assessment.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TaxRepository taxRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());

        if (productRequestDTO.getTaxIds() != null && !productRequestDTO.getTaxIds().isEmpty()) {
            Set<Tax> taxes = new HashSet<>(taxRepository.findAllById(productRequestDTO.getTaxIds()));
            if (taxes.size() != productRequestDTO.getTaxIds().size()) {
                throw new NotFound("One or more taxes not found.");
            }
            product.setTaxes(taxes);
        }

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<ProductResponseDTO>> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponseDTO> productDTOs = productPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        PaginationResponse pagination = PaginationResponse.builder()
                .currentPage(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalElements((int) productPage.getTotalElements())
                .build();

        return BaseResponse.<List<ProductResponseDTO>>builder()
                .message("Successfully retrieved all products.")
                .code(HttpStatus.OK.value())
                .data(productDTOs)
                .pagination(pagination)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFound("Product not found with id: " + productId));
        return mapToResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequestDTO) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new NotFound("Product not found with id: " + productId));

        productToUpdate.setName(productRequestDTO.getName());
        productToUpdate.setPrice(productRequestDTO.getPrice());

        productToUpdate.getTaxes().clear();
        if (productRequestDTO.getTaxIds() != null && !productRequestDTO.getTaxIds().isEmpty()) {
            Set<Tax> taxes = new HashSet<>(taxRepository.findAllById(productRequestDTO.getTaxIds()));
            if (taxes.size() != productRequestDTO.getTaxIds().size()) {
                throw new NotFound("One or more taxes not found.");
            }
            productToUpdate.setTaxes(taxes);
        }

        Product updatedProduct = productRepository.save(productToUpdate);
        return mapToResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFound("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());

        if (product.getTaxes() != null) {
            dto.setTaxes(product.getTaxes().stream().map(tax -> {
                TaxResponseDTO taxDTO = new TaxResponseDTO();
                taxDTO.setId(tax.getId());
                taxDTO.setName(tax.getName());
                taxDTO.setRate(tax.getRate());
                return taxDTO;
            }).collect(Collectors.toSet()));
        }
        return dto;
    }
}
