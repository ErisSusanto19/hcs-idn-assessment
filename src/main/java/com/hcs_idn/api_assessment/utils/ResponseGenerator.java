package com.hcs_idn.api_assessment.utils;

import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.PaginationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseGenerator {
    public static <T>ResponseEntity<BaseResponse<T>> generate(String message, HttpStatus httpStatus, T data, PaginationResponse pagination){
        BaseResponse<T> response;

        if(pagination == null){
            response = BaseResponse.<T>builder()
                    .message(message)
                    .code(httpStatus.value())
                    .data(data)
                    .build();

        } else{
            response = BaseResponse.<T>builder()
                    .message(message)
                    .code(httpStatus.value())
                    .data(data)
                    .pagination(pagination)
                    .build();

        }

        return ResponseEntity.status(httpStatus.value()).body(response);
    }
}
