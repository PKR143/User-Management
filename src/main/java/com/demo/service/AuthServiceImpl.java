package com.demo.service;

import com.demo.entity.UserEntity;
import com.demo.exception.UserException;
import com.demo.model.LoginRequest;
import com.demo.model.LoginResponse;
import com.demo.model.SignupRequest;
import com.demo.model.SignupResponse;
import com.demo.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        request.setPassword(encoder.encode(request.getPassword()));
        log.info("Processing User signup request: {}",request);
        Optional<UserEntity> userOpt = userRepo.findByUsername(request.getUsername());
        if(userOpt.isPresent()){
            log.info("Username already exists!");
            throw new UserException("AUTH_ERR_001","Username already exists!");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .role("USER")
                .build();
        user = userRepo.save(user);
        log.info("New User inserted successfully: {}",request.getUsername());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return SignupResponse.builder()
                .email(user.getEmail())
                .username(request.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .code("201")
                .message("Signup successful, now log in to continue.")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Optional<UserEntity> userOpt = userRepo.findByUsername(request.getUsername());
        if(userOpt.isEmpty()){
            log.info("Invalid Username provided: {}",request.getUsername());
            throw new UserException("AUTH_ERR_002","Invalid Username or Password!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .code("200")
                .message("Login Successful!")
                .username(request.getUsername())
                .build();
    }

    public LoginResponse refreshToken(String refreshToken){
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(!jwtService.isTokenValid(refreshToken, userDetails)){
            throw new UserException("AUTH_ERR_003","Invalid or Expired refresh Token");
        }
        String newAccessToken = jwtService.generateToken(userDetails);
        UserEntity user = userRepo.findByUsername(username).orElseThrow();
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .code("200")
                .message("New Access Token generated Successfully!")
                .username(user.getUsername())
                .build();
    }

}
