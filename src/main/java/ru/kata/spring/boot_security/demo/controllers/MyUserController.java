package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.service.MyUserServiceImpl;

@Controller
public class MyUserController {

    private final MyUserServiceImpl myUserService;

    public MyUserController(MyUserServiceImpl myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping("/user")
    public String userHome(@AuthenticationPrincipal MyUser currentUser, Model model) {
        MyUser user = myUserService.showById(currentUser.getId());
        model.addAttribute("user", user);
        return "user";
    }
}
