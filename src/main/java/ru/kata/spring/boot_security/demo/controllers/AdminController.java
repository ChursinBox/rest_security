package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.MyUserRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.MyUserServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final MyUserRepository myUserRepository;
    private final RoleRepository roleRepository;
    private final MyUserServiceImpl myUserService;

    @Autowired
    public AdminController(PasswordEncoder passwordEncoder, MyUserRepository myUserRepository, RoleRepository roleRepository, MyUserServiceImpl myUserService) {
        this.passwordEncoder = passwordEncoder;
        this.myUserRepository = myUserRepository;
        this.roleRepository = roleRepository;
        this.myUserService = myUserService;
    }

    @GetMapping("/all")
    public String index(Model model) {
        model.addAttribute("users", myUserService.showAll());
        return "admin/showAll";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        MyUser optionalUser = myUserService.showById(id);
        model.addAttribute("user", optionalUser);
        return "admin/showById";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        MyUser optionalUser = myUserService.showById(id);
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("user", optionalUser);
        model.addAttribute("allRoles", allRoles);
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String editUser(@PathVariable int id, @ModelAttribute MyUser updatedUser) {

        MyUser existingUser = myUserService.showById(id);

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getAge() != 0) {
            existingUser.setAge(updatedUser.getAge());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (!updatedUser.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getRoles() != null) {
            existingUser.setRoles(updatedUser.getRoles());
        }

        myUserRepository.save(existingUser);

        return "redirect:/admin/all";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        myUserService.deleteById(id);
        return "redirect:/admin/all";
    }
}