package com.demo.service;

import com.demo.model.LoginRequest;
import com.demo.model.LoginResponse;
import com.demo.model.SignupRequest;
import com.demo.model.SignupResponse;

public interface AuthService {

        SignupResponse signup(SignupRequest request);
        LoginResponse login(LoginRequest request);

}
