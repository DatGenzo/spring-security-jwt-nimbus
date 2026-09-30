package com.thanhdat.jwtnimbus.exceptions;

import java.util.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException e) {
        Map<String,String> fields = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> fields.putIfAbsent(x.getField(), x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("status",400,"message","Dữ liệu không hợp lệ","fieldErrors",fields));
    }
    @ExceptionHandler(DuplicateResourceException.class)
    ResponseEntity<?> duplicate(DuplicateResourceException e) {
        return ResponseEntity.status(409).body(Map.of("status",409,"message",e.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> badLogin() {
        return ResponseEntity.status(401).body(Map.of("status",401,"message","Email hoặc mật khẩu không chính xác"));
    }
    @ExceptionHandler({DisabledException.class, LockedException.class})
    ResponseEntity<?> forbidden() {
        return ResponseEntity.status(403).body(Map.of("status",403,"message","Tài khoản bị khóa hoặc chưa kích hoạt"));
    }
}
