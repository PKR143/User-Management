package com.demo.exception;

import com.demo.model.AuthenticationErrorResponse;
import com.demo.model.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.info("Handling Method argument not valid exception, due to: {}",e.getMessage());
        Map map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error->map.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapToGeneralResponse("400","VALIDATION ERROR",map.values().toString()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e){
        log.info("Handling Username not found exception, due to: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapToGeneralResponse("401","UNAUTHENTICATED","Invalid Username or Password!"));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException e){
        log.info("Handling AuthorizationDeniedException, due to: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapToGeneralResponse("403","UNAUTHORIZED","Access Denied!"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e){
        log.info("Handling AuthenticationException, due to: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapToGeneralResponse("401","UNAUTHENTICATED","Invalid Credentials!"));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleException(UserException e){
        log.info("Handling UserException, due to: {}",e.getMessage());
        return ResponseEntity.badRequest().body(mapToGeneralResponse(e.getCode(),"BAD REQUEST",e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        log.info("Handling Exception, due to: {}, class: {}",e.getMessage(),e.getClass());
        return ResponseEntity.badRequest().body(mapToGeneralResponse("400","BAD REQUEST",e.getMessage()));
    }

    private GeneralResponse<?> mapToGeneralResponse(String code, String status, String message){
        return GeneralResponse.<Object>builder()
                .success(false)
                .status(status)
                .data(null)
                .error(GeneralResponse.Error.builder()
                        .code(code)
                        .message(message)
                        .build())
                .build();
    }

}
