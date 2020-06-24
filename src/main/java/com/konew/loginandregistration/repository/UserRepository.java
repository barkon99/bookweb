package com.konew.loginandregistration.repository;

import com.konew.loginandregistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByEmail(String email);

    User getUserByUserName(String username);
    User findById(long id);
}
