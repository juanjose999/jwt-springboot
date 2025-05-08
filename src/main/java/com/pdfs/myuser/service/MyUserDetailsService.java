package com.pdfs.myuser.service;

import com.pdfs.myuser.repository.MyUserRepository;
import com.pdfs.myuser.entity.MyUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserRepository myUserRepository;

    public MyUserDetailsService(final MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> myUser = myUserRepository.findByEmail(username);
        if(myUser.isEmpty()) throw new UsernameNotFoundException("User not found with username: " + username);
        return myUser.get();
    }
}
