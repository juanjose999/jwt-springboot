package com.pdfs.permissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyPermissionsRepository extends JpaRepository<MyPermissions, Long> {
    Optional<MyPermissions> findByName(String name);
}
