package com.pdfs.roles.entity;

import com.pdfs.permissions.MyPermissions;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_permission",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<MyPermissions> permissions = new HashSet<>();

    public Roles() {}

    public Roles(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Roles(String name, String description, Set<MyPermissions> permissions) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MyPermissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<MyPermissions> permissions) {
        if(this.permissions == null) this.permissions = new HashSet<>();
        this.permissions = permissions;
    }
}
