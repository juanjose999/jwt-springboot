package com.pdfs.myuser.repository;

import com.pdfs.jwt.entity.Jwt;
import com.pdfs.myuser.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);

}
