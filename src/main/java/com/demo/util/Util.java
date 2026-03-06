package com.demo.util;


import com.demo.entity.UserEntity;
import com.demo.model.Dashboard;
import com.demo.model.GeneralResponse;
import com.demo.model.UserResponse;

public class Util<T>{

    public static <T> GeneralResponse<T> mapToGeneralResponseSuccess(T data, String status){
        return GeneralResponse.<T>builder()
                .success(true)
                .error(null)
                .status(status)
                .data(data)
                .build();
    }

    public static <T> GeneralResponse<T> mapToGeneralResponseError(String code, String message, String status){
            GeneralResponse.Error error = GeneralResponse.Error.builder()
                    .code(code)
                    .message(message)
                    .build();

        return GeneralResponse.<T>builder()
                .status(status)
                .success(false)
                .data(null)
                .error(error)
                .build();
    }

    public static Dashboard mapToDashboard(UserEntity user, String code, String msg) {
        return Dashboard.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .code(code)
                .message(msg)
                .build();
    }


    public static UserResponse mapToUserResponse(UserEntity user, String code, String msg) {
        return UserResponse.builder()
                .code(code)
                .email(user.getEmail())
                .username(user.getUsername())
                .message(msg)
                .build();
    }
}
