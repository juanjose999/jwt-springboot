package com.pdfs.myuser.entity;

import com.pdfs.jwt.entity.Jwt;
import com.pdfs.roles.entity.Roles;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class MyUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String dateCreated = LocalDate.now().toString();

    @OneToMany(mappedBy = "user")
    private Set<Jwt> jwt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_rol",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Roles> roles = new HashSet<>();

    public MyUser() {}

    public MyUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateCreated = LocalDate.now().toString();
        this.roles = new HashSet<>();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(Roles rol : this.roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+rol.getName()));
            authorities.addAll(rol.getPermissions().stream()
                    .map(permissions -> new SimpleGrantedAuthority(permissions.getName()))
                    .collect(Collectors.toSet()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Roles rol) {
        if(this.roles == null) this.roles = new HashSet<>();
        this.roles.add(rol);
    }

    public void setJwt(Jwt jwt) {
        if(this.jwt == null) this.jwt = new HashSet<>();
        this.jwt.add(jwt);
    }

    public Set<Jwt> getJwt() {
        return jwt;
    }
}
