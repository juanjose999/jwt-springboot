package com.pdfs.myuser;

import com.pdfs.jwt.Jwt;
import com.pdfs.jwt.JwtRepository;
import com.pdfs.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MyUserController {

    private final MyUserService myUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;

    public MyUserController(final MyUserService myUserService, AuthenticationManager authenticationManager, JwtService jwtService, JwtRepository jwtRepository) {
        this.myUserService = myUserService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtRepository = jwtRepository;
    }

    @PostMapping("/singup")
    public ResponseEntity<?> saveUser(@RequestBody MyUser myUser){
        return ResponseEntity.ok(myUserService.saveUser(myUser));
    }

    @GetMapping("/users")
    public String helloUser(){
        return "Hello USER";
    }

    @GetMapping("/admins")
    public String helloAdmin(){
        return "Hello ADMIN";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MyUser myUser) throws Exception {
        Authentication authenticacion = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(myUser.getEmail(), myUser.getPassword())
        );
        if(!authenticacion.isAuthenticated()) throw new Exception("Invalid credentials.");
        String token = jwtService.generateJwt(myUser.getEmail());
        Jwt jwt = new Jwt(token);
        jwtRepository.save(jwt);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<?> health(){
        return ResponseEntity.ok("FUNCIONANDO CORRECTAMENTE");
    }
}
