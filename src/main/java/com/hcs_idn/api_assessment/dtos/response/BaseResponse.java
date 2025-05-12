package com.hcs_idn.api_assessment.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private String message;
    private Integer code;
    private T data;
    private PaginationResponse pagination;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
