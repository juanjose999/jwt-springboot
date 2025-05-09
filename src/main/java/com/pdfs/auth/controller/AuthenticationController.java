package com.pdfs.auth.controller;

import com.pdfs.auth.service.AuthService;
import com.pdfs.auth.service.ControlAccessToken;
import com.pdfs.jwt.entity.Jwt;
import com.pdfs.jwt.repository.JwtRepository;
import com.pdfs.jwt.service.JwtService;
import com.pdfs.myuser.entity.MyUser;
import com.pdfs.myuser.entity.dto.LoginForm;
import com.pdfs.myuser.entity.dto.MyUserRequestDto;
import com.pdfs.myuser.repository.MyUserRepository;
import com.pdfs.myuser.service.MyUserDetailsService;
import com.pdfs.myuser.service.MyUserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private final MyUserService myUserService;
    private final MyUserRepository myUserRepository;
    private final AuthService authService;

    public AuthenticationController(MyUserService myUserService, MyUserRepository myUserRepository, AuthService authService) {
        this.myUserService = myUserService;
        this.myUserRepository = myUserRepository;
        this.authService = authService;
    }

    @GetMapping("/users")
    public String helloUser(){
        return "ONLY USER";
    }

    @GetMapping("/admins")
    public String helloAdmin(){
        return "ONLY ADMIN";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(authService.login(loginForm, request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String token, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(authService.refreshToken(token, request));
    }

    @PostMapping("/invalid-all-tokens")
    public ResponseEntity<?> invalidAllTokensByEmail(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(authService.invalidAllTokensByEmail(email));
    }

    @GetMapping("/health")
    public ResponseEntity<?> health(){
        return ResponseEntity.ok("FUNCIONANDO CORRECTAMENTE");
    }


}
