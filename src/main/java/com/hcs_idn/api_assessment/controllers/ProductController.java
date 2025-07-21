package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.request.ProductRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.ProductResponseDTO;
import com.hcs_idn.api_assessment.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<ProductResponseDTO>> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequestDTO);
        BaseResponse<ProductResponseDTO> response = BaseResponse.<ProductResponseDTO>builder()
                .message("Product created successfully.")
                .code(HttpStatus.CREATED.value())
                .data(createdProduct)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getAllProducts(Pageable pageable) {
        BaseResponse<List<ProductResponseDTO>> response = productService.getAllProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<ProductResponseDTO>> getProductById(@PathVariable UUID id) {
        ProductResponseDTO product = productService.getProductById(id);
        BaseResponse<ProductResponseDTO> response = BaseResponse.<ProductResponseDTO>builder()
                .message("Product retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<ProductResponseDTO>> updateProduct(@PathVariable UUID id, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequestDTO);
        BaseResponse<ProductResponseDTO> response = BaseResponse.<ProductResponseDTO>builder()
                .message("Product updated successfully.")
                .code(HttpStatus.OK.value())
                .data(updatedProduct)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        BaseResponse<String> response = BaseResponse.<String>builder()
                .message("Product deleted successfully.")
                .code(HttpStatus.OK.value())
                .data("Product with id " + id + " was deleted.")
                .build();
        return ResponseEntity.ok(response);
    }
}
