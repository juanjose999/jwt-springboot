package com.pdfs.myuser.controller;

import com.pdfs.jwt.repository.JwtRepository;
import com.pdfs.jwt.service.JwtService;
import com.pdfs.myuser.entity.dto.MyUserRequestDto;
import com.pdfs.myuser.service.MyUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyUserController {

    private final MyUserService myUserService;

    public MyUserController(final MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody MyUserRequestDto myUserRequestDto) {
        return ResponseEntity.ok(myUserService.saveUser(myUserRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE')")
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody MyUserRequestDto myUserRequestDto) {
        return ResponseEntity.ok(myUserService.createAdmin(myUserRequestDto));
    }
}
