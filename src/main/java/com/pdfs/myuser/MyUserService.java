package com.pdfs.myuser;

import com.pdfs.roles.Roles;
import com.pdfs.roles.RolesRepository;
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

    public MyUser saveUser(MyUser myUser){
        Optional<Roles> findRoleUser = rolesRepository.findByName("ROLE_USER");
        if(findRoleUser.isEmpty()) throw new RuntimeException("Role USE not found.");
        myUser.setRoles(findRoleUser.get());
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        return myUserRepository.save(myUser);
    }

}
