package com.hcs_idn.api_assessment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private Integer currentPage;
    private Integer totalElements;
    private Integer totalPages;
    private Integer pageSize;
}
