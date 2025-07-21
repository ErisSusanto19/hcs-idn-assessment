package com.hcs_idn.api_assessment.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerUpdateRequestDTO {
    private String name;
    private LocalDate birthdate;
    private String birthplace;
}
