package com.demo.controller;

import com.demo.model.*;
import com.demo.service.AuthService;
import com.demo.util.Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<GeneralResponse<?>> signUp(@RequestBody @Valid SignupRequest request){
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToGeneralResponse(response));
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<?>> login(@RequestBody @Valid LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess(response,"AUTHENTICATED"));
    }

    @GetMapping("/HbtChk")
    public ResponseEntity<?> home(){
//        return ResponseEntity.ok(Map.of("status","OK","message","User Management Module is UP and Running!"));
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess(new HeartBeatResponse("200","User Management module  is Up and Running."),"SUCCESS"));
    }

    private GeneralResponse<?> mapToGeneralResponse(SignupResponse response) {
        return GeneralResponse.<SignupResponse>builder()
                .success(true)
                .data(response)
                .error(null)
                .status("SUCCESS")
                .build();
    }

}
