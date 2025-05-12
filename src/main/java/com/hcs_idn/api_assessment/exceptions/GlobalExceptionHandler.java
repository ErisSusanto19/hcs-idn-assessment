package com.hcs_idn.api_assessment.exceptions;

import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.exceptions.customs.BadRequest;
import com.hcs_idn.api_assessment.exceptions.customs.Forbidden;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.exceptions.customs.Unauthorized;
import com.hcs_idn.api_assessment.utils.ResponseGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<BaseResponse<Object>> handleBadRequest(BadRequest e){
        return ResponseGenerator.generate(e.getMessage(), HttpStatus.BAD_REQUEST, null, null);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<BaseResponse<Object>> handleNotFound(NotFound e){
        return ResponseGenerator.generate(e.getMessage(), HttpStatus.NOT_FOUND, null, null);
    }

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<BaseResponse<Object>> handleForbidden(Forbidden e){
        return ResponseGenerator.generate(e.getMessage(), HttpStatus.FORBIDDEN, null, null);
    }

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<BaseResponse<Object>> handleUnauthorized(Unauthorized e){
        return ResponseGenerator.generate(e.getMessage(), HttpStatus.UNAUTHORIZED, null, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleUnknownError(Exception e){
        return ResponseGenerator.generate(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null);
    }
}
