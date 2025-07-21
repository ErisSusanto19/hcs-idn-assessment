package com.hcs_idn.api_assessment.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String login;
    private String password;
}
