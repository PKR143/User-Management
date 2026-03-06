package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralResponse <T>{

    private boolean success;
    private String status;
    private T data;
    private Error error;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Error{
        private String code;
        private String message;
    }

}
