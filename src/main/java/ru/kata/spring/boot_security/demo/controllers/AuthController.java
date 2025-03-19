package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.util.MyUserValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final MyUserValidator myUserValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService
            , MyUserValidator myUserValidator
    ) {
        this.myUserValidator = myUserValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("myUser") MyUser myUser) {
        return "auth/registration";
    }

    @PostMapping("/registration")

    public String performRegistration(@ModelAttribute("myUser") MyUser myUser

            , BindingResult bindingResult) {

        myUserValidator.validate(myUser, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(myUser);

        return "/auth/login";
    }
}
