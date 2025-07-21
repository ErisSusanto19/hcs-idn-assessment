package com.hcs_idn.api_assessment.dtos.request;

import java.time.LocalDate;

public class CustomerCreateRequestDTO {
    private String name;
    private LocalDate birthdate;
    private String birthplace;

    private String username;
    private String email;
    private String password;
}
