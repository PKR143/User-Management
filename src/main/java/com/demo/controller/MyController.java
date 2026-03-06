package com.demo.controller;

import com.demo.model.Dashboard;
import com.demo.model.HeartBeatResponse;
import com.demo.model.UserResponse;
import com.demo.service.DashboardService;
import com.demo.util.Util;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/um")
@RequiredArgsConstructor
@EnableMethodSecurity
public class MyController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> home(Authentication auth){
        Dashboard response = dashboardService.getDashboard(auth);
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess(response,"OK"));
    }

    @GetMapping("/findUser/{username}")
    public ResponseEntity<?> searchUser(
            @NotBlank(message = "Please provide username!!")
            @PathVariable
            String username
            ){
        UserResponse response = dashboardService.searchUser(username);
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess(response,"OK"));
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy
            ){
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess(dashboardService.findAll(page,size,sortBy, orderBy),"OK"));
    }

    @GetMapping("/demo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> demo(){
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess("SUCCESS","OK"));
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<?> teacher(){
        return ResponseEntity.ok(Util.mapToGeneralResponseSuccess("SUCCESS","OK"));
    }

}
