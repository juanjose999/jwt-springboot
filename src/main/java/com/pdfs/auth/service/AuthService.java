package com.pdfs.auth.service;

import com.pdfs.jwt.entity.Jwt;
import com.pdfs.jwt.repository.JwtRepository;
import com.pdfs.jwt.service.JwtService;
import com.pdfs.myuser.entity.MyUser;
import com.pdfs.myuser.entity.dto.LoginForm;
import com.pdfs.myuser.repository.MyUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final MyUserRepository myUserRepository;
    private final JwtRepository jwtRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       MyUserRepository myUserRepository,
                       JwtRepository jwtRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.myUserRepository = myUserRepository;
        this.jwtRepository = jwtRepository;
        this.jwtService = jwtService;
    }

    public Map<String,String> login(LoginForm loginForm, HttpServletRequest request) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.email(), loginForm.password())
        );
        if(!authentication.isAuthenticated()) throw new UsernameNotFoundException("Invalid credentials");

        Optional<MyUser> findUserByEmail = myUserRepository.findByEmail(authentication.getName());
        if(findUserByEmail.isEmpty()) throw new UsernameNotFoundException("Invalid Credentials");

        String ipUser = request.getRemoteAddr();

        String accessToken = jwtService.generateJwtAccessToken(authentication.getName(), ipUser);
        ControlAccessToken.addAccessToken(new ControlAccessToken(findUserByEmail.get().getEmail()));
        String refreshToken = jwtService.generateJwtRefreshToken(authentication.getName(), ipUser);

        List<Jwt> findTokensByUser = jwtRepository.findByUserAndActiveTrue(findUserByEmail.get());
        invalidTokenOld(findTokensByUser);

        Jwt jwt = new Jwt(refreshToken, findUserByEmail.get());
        jwtRepository.save(jwt);

        Map<String, String> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("refreshToken", refreshToken);
        return response;
    }

    public Map<String, String> refreshToken(String refreshTokenOld, HttpServletRequest request) throws Exception {
        String userEmail = jwtService.getEmailFromJwt(refreshTokenOld);
        MyUser findUserByEmail = findUserByEmail(userEmail);

        String accessToken = jwtService.generateJwtAccessToken(findUserByEmail.getEmail(), request.getRemoteAddr());
        String refreshToken = jwtService.generateJwtRefreshToken(findUserByEmail.getEmail(), request.getRemoteAddr());

        List<Jwt> findTokensByUser = jwtRepository.findByUserAndActiveTrue(findUserByEmail);
        invalidTokenOld(findTokensByUser);

        jwtRepository.save(new Jwt(refreshToken, findUserByEmail));

        return Map.of("access_token", accessToken,"refresh_token", refreshToken);
    }

    public String invalidAllTokensByEmail(@RequestParam String email) throws Exception {
        MyUser findUserByEmail = findUserByEmail(email);
        ControlAccessToken.invalidateAllTokens(findUserByEmail.getEmail());
        List<Jwt> findTokensByUser = jwtRepository.findByUserAndActiveTrue(findUserByEmail);
        invalidTokenOld(findTokensByUser);
        return "the user has invalid all tokens";
    }

    private void invalidTokenOld(List<Jwt> tokensUser) {
        if(!tokensUser.isEmpty()){
            tokensUser.forEach(t -> {
                t.setActive(false);
                jwtRepository.save(t);
            });
        }
    }

    private MyUser findUserByEmail(String userEmail) {
        Optional<MyUser> findUserByEmail = Optional.ofNullable(myUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials.")));
        return findUserByEmail.get();
    }

}
