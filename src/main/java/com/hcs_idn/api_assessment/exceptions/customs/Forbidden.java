package com.hcs_idn.api_assessment.exceptions.customs;

public class Forbidden extends RuntimeException {
    public Forbidden(String message){
        super(message);
    }
}
