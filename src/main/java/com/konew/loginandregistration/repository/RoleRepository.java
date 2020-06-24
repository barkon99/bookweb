package com.konew.loginandregistration.repository;

import com.konew.loginandregistration.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT id from role where name=:name ", nativeQuery = true)
    int findByName(@Param("name") String name);
}
