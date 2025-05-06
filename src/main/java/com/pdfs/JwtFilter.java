package com.pdfs;

import com.pdfs.jwt.JwtService;
import com.pdfs.myuser.MyUser;
import com.pdfs.myuser.MyUserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

    private final ApplicationContext applicationContext;
    private final JwtService jwtService;

    public JwtFilter(ApplicationContext applicationContext, JwtService jwtService) {
        this.applicationContext = applicationContext;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) throw new JwtException("Authorization header is invalid");
        String token = authorization.substring(7);
        if(token.isEmpty()) throw new JwtException("Authorization header is invalid");
        String email = jwtService.getEmailFromJwt(token);
        if(email == null || email.isBlank()) throw new JwtException("Invalid email");
        Optional<MyUser> findUserByEmail = applicationContext.getBean(MyUserRepository.class).findByEmail(email);
        if(findUserByEmail.isEmpty()) throw new JwtException("User not found");
        MyUser myUser = findUserByEmail.get();
        if(!jwtService.validateJwt(token) && SecurityContextHolder.getContext().getAuthentication() != null){
            throw new JwtException("Invalid token");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(myUser, null, myUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
        return;
    }
}
