package com.konew.loginandregistration.controller;

import com.konew.loginandregistration.dto.UserDto;
import com.konew.loginandregistration.entity.Token;
import com.konew.loginandregistration.entity.User;
import com.konew.loginandregistration.repository.TokenRepository;
import com.konew.loginandregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private UserService userService;
    private TokenRepository tokenRepository;

    @Autowired
    public RegistrationController(UserService userService, TokenRepository tokenRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping
    public String registration(Model model)
    {
        UserDto userDto = new UserDto();
        userDto.setConfirmationOfEmailNeeded(false);
        model.addAttribute("userDto",userDto);
        return "register";
    }
    @GetMapping("/?success")
    public String registrationSuccess()
    {
        return "register";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("userDto")  UserDto userDto, BindingResult result)
    {
        User existingUsername = userService.findByUsername(userDto.getUsername());
        User existingEmail = userService.findByEmail(userDto.getEmail());
        if(existingUsername!=null)
        {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }
        if(existingEmail!=null)
        {
            result.rejectValue("email", null, "There is already an account registered with that email");
            System.out.println("email");
        }
        if(result.hasErrors())
        {
            return "register";
        }
        userDto.setConfirmationOfEmailNeeded(true);

        userService.save(userDto);
        return registrationSuccess();
    }
    @GetMapping("/token")
    public String getToken(@RequestParam String value)
    {
        Token token = tokenRepository.findByValue(value);
        User user = token.getUser();
        user.setEnabled(true);
        userService.save(user);
        return "welcome";
    }
}
