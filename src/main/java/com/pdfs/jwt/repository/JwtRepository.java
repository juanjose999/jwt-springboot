package com.pdfs.jwt.repository;

import com.pdfs.jwt.entity.Jwt;
import com.pdfs.myuser.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {
    List<Jwt> findByUserAndActiveTrue(MyUser user);
    Optional<Jwt> findByToken(String token);
}
