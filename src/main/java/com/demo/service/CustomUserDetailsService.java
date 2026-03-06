package com.demo.service;

import com.demo.entity.UserEntity;
import com.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

   @Autowired
   UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOpt = userRepo.findByUsername(username);
        if(userOpt.isEmpty()){
            log.info("User not exists with username: {}",username);
            throw new UsernameNotFoundException("User Not Found");
        }
        UserEntity user = userOpt.get();
//        System.out.printf("Username: %s, hash password: %s %n",user.getUsername(), user.getPassword());
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
