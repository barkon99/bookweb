package com.konew.loginandregistration.service;

import com.konew.loginandregistration.dto.UserDto;
import com.konew.loginandregistration.entity.Role;
import com.konew.loginandregistration.entity.Token;
import com.konew.loginandregistration.entity.User;
import com.konew.loginandregistration.repository.RoleRepository;
import com.konew.loginandregistration.repository.TokenRepository;
import com.konew.loginandregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    MailService mailService;
    TokenRepository tokenRepository;
    RoleRepository roleRepository;

    @Value("${server.port}")
    private int port;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService, TokenRepository tokenRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findByUsername(String username)
    {
        return userRepository.findByUserName(username);
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User();
        Role role = new Role();

        role.setName("ROLE_ADMIN");
        int id = roleRepository.findByName(role.getName());
        role.setId(id);

        user.setUserName(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Arrays.asList(role));
        user.setEmail(userDto.getEmail());

        userRepository.save(user);
        sendToken(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    public void sendToken(User user)
    {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepository.save(token);

        String url = "http://localhost:" + port + "/registration/token?value="+tokenValue;

        try {
            mailService.sendMail(user.getEmail(),"Confirm your email", url,false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
