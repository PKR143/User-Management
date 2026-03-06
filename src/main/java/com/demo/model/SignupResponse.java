package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SignupResponse {

    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String code;
    private String message;

}
