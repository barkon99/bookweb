package com.konew.loginandregistration.service;

import com.konew.loginandregistration.dto.UserDto;
import com.konew.loginandregistration.entity.User;


public interface UserService
{
    User findByUsername(String username);

    void save(UserDto userDto);

    User findByEmail(String email);

    void save(User user);
}
