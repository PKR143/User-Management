package com.demo.service;

import com.demo.entity.UserEntity;
import com.demo.exception.UserException;
import com.demo.model.Dashboard;
import com.demo.model.UserResponse;
import com.demo.repo.UserRepo;
import com.demo.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DashboardService {

    @Autowired
    UserRepo userRepo;

    public Dashboard getDashboard(Authentication auth){
        AbstractAuthenticationToken token = (AbstractAuthenticationToken) auth;
        String username = token.getName();
        log.info("user logged in: {}",username);
        UserEntity user = userRepo.findByUsername(username).orElseThrow();
        return Util.mapToDashboard(user, "200", "User Details Fetched Successfully.");
    }

    public UserResponse searchUser(String username){

        log.info("User search request with username: {}",username);
        UserEntity user = userRepo.findByUsername(username).orElseThrow(()->new UserException("UM_ERR_001", "User not exists!"));
        UserResponse response = Util.mapToUserResponse(user,"200","User Fetched Successfully.");
        return response;
    }

    public List<UserResponse> findAll(int page, int size, String sortBy, String orderBy){
        log.info("Processing find All User Operation.");
        Sort sort = orderBy.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<UserEntity> pages = userRepo.findAll(pageable);
        return pages.stream()
                .map(user-> new UserResponse(user.getUsername(), user.getEmail(),null,null))
                .toList();
    }

}
