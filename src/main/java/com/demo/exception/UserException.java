package com.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class UserException extends RuntimeException {
    private String code;
    private String message;
//    public UserException(String code, String message) {
//        super(message);
//        this.code = code;
//        this.message = message;
//    }
}
