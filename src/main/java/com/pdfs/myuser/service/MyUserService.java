package com.pdfs.myuser.service;

import com.pdfs.myuser.entity.dto.MyUserMapper;
import com.pdfs.myuser.entity.dto.MyUserRequestDto;
import com.pdfs.myuser.repository.MyUserRepository;
import com.pdfs.myuser.entity.MyUser;
import com.pdfs.roles.entity.Roles;
import com.pdfs.roles.repository.RolesRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserService {

    private final MyUserRepository myUserRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUserService(final MyUserRepository myUserRepository, final RolesRepository rolesRepository, final PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MyUser saveUser(MyUserRequestDto userRequestDto) {
        MyUser myUser = MyUserMapper.myUserRequestDtoToMyUser(userRequestDto);
        Optional<Roles> findRoleUser = rolesRepository.findByName("USER");
        if(findRoleUser.isEmpty()) throw new RuntimeException("Role user not found.");
        myUser.setRoles(findRoleUser.get());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        return myUserRepository.save(myUser);
    }

    public MyUser createAdmin(MyUserRequestDto userRequestDto) {
        MyUser myUser = MyUserMapper.myUserRequestDtoToMyUser(userRequestDto);
        Optional<Roles> findRoleAdmin = rolesRepository.findByName("ADMIN");
        if(findRoleAdmin.isEmpty()) throw new RuntimeException("Role admin not found.");
        myUser.setRoles(findRoleAdmin.get());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        return myUserRepository.save(myUser);
    }

}
